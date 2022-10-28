package com.osscameroon.jsgenerator.api.rest;

import com.osscameroon.jsgenerator.api.domain.Command;
import com.osscameroon.jsgenerator.api.domain.InlineOutput;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.OutputStreamResolver;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.osscameroon.jsgenerator.api.rest.ConvertController.MAPPING;
import static com.osscameroon.jsgenerator.core.OutputStreamResolver.EXTENSION;
import static com.osscameroon.jsgenerator.core.OutputStreamResolver.INDEX;
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
    private final Converter converter;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<InlineOutput> inlineAction(@RequestBody @Valid final Command command) {
        LOGGER.info("{}", command);

        final var index = new AtomicInteger();
        final var configuration = command.toConfiguration();

        return command.getInlineContents().stream()
                .map(content -> {
                    final var outputStream = new ByteArrayOutputStream();
                    final var inputStream = new ByteArrayInputStream(content.getBytes(UTF_8));

                    try {
                        converter.convert(inputStream, outputStream, configuration);
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }

                    return outputStream.toString(UTF_8);
                })
                .map(content -> {
                    final var filename = inlineOutputStreamResolver.resolve(command.getInlinePattern(), Map.of(
                            INDEX, "%d".formatted(index.getAndIncrement()),
                            EXTENSION, command.getExtension()));

                    return new InlineOutput(filename, content);
                })
                .toList();
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public List<InlineOutput> inlineAction(@RequestPart("config")
                                           Optional<Command> optionalCommand,
                                           @RequestPart("files") @Valid @Min(1) @Min(30)
                                           List<MultipartFile> multipartFiles) throws IOException {
        return List.of();
    }
}
