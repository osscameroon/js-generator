package com.osscameroon.jsgenerator.api.rest;

import com.osscameroon.jsgenerator.api.domain.InlineOptions;
import com.osscameroon.jsgenerator.api.domain.MultipartOptions;
import com.osscameroon.jsgenerator.api.domain.Output;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.OutputStreamResolver;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.osscameroon.jsgenerator.api.rest.ConvertController.MAPPING;
import static com.osscameroon.jsgenerator.core.OutputStreamResolver.EXTENSION;
import static com.osscameroon.jsgenerator.core.OutputStreamResolver.INDEX;
import static com.osscameroon.jsgenerator.core.OutputStreamResolver.ORIGINAL;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(MAPPING)
public class ConvertController {
    public static final String MAPPING = "/convert";
    private static final Logger LOGGER = getLogger(ConvertController.class);

    private final OutputStreamResolver inlineOutputStreamResolver;
    private final OutputStreamResolver pathOutputStreamResolver;
    private final Converter converter;

    //TODO: Make sure all these 4 cases are taken into account
    // code html to code js OK
    // code html to file js
    // file html to code js
    // file html to file js OK

    // code html to code js OK
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Reply<? extends List<? extends Output>> convertAction(@RequestBody @Valid final InlineOptions options) {
        LOGGER.info("{}", options);

        final var index = new AtomicInteger();
        final var configuration = options.toConfiguration();

        return Reply.ofSuccesses(options.getContents().stream()
                .map(content -> converter.convert(
                        configuration,
                        new ByteArrayOutputStream(),
                        //convertInlineContentWithCopyrightCharacterWithComment works after doing this, why ? What happened ?
                        new ByteArrayInputStream(content.getBytes(UTF_8))))
                .map(content -> {
                    final var filename = inlineOutputStreamResolver.resolve(options.getPattern(), Map.of(
                            INDEX, "%d".formatted(index.getAndIncrement()),
                            EXTENSION, options.getExtension()));

                    return new Output(filename, content);
                })
                .toList());
    }

    // file html to file js OK
    @PostMapping(path = "files", consumes = MULTIPART_FORM_DATA_VALUE, produces = MULTIPART_FORM_DATA_VALUE)
    public MultiValueMap<String, AbstractResource> convertAction(@RequestPart("options") @Valid
                                                                 Optional<MultipartOptions> optionalCommand,
                                                                 @RequestPart("files") @Size(min = 1, max = 30) @Valid
                                                                 List<MultipartFile> multipartFiles) throws IOException {
        new MultipartOptions().toBuilder().build();
        final var command = optionalCommand.orElseGet(MultipartOptions::new);
        final var map = new LinkedMultiValueMap<String, AbstractResource>();
        final var indexTracker = new AtomicInteger();

        multipartFiles.stream().map(multipartFile -> {
                    try {
                        return converter.convert(
                                command.toConfiguration(),
                                new ByteArrayOutputStream(),
                                new ByteArrayInputStream(multipartFile.getBytes()));
                    } catch (IOException e) {
                        throw new UnsupportedOperationException(e);
                    }
                })
                .map(content -> {
                    //noinspection ConstantConditions
                    final var filename = pathOutputStreamResolver.resolve(command.getPattern(), Map.of(
                            ORIGINAL, multipartFiles.get(indexTracker.get()).getOriginalFilename(),
                            INDEX, "%d".formatted(indexTracker.get()),
                            EXTENSION, command.getExtension()));

                    indexTracker.getAndIncrement();

                    return new Output(filename, content);
                })
                .forEach(output ->
                        map.add(output.getFilename(), new ByteArrayResource(output.getContent().getBytes())));

        return map;
    }

    private byte[] encodingAndDecodingInUTF8(MultipartFile file) throws IOException {

        // Encode the MultipartFile as UTF-8 bytes
        byte[] encodedBytes = file.getBytes();
        String encodedText = new String(encodedBytes,StandardCharsets.UTF_8);

        // Decode the UTF-8 bytes back to MultipartFile
        byte[] decodedBytes = encodedText.getBytes(StandardCharsets.UTF_8);

/*
        String content = new String (multipartFile.getBytes());

        byte[] encodedBytes = content.getBytes(UTF_8);

        return new String(encodedBytes, UTF_8).getBytes();*/

        return decodedBytes;
    }
}