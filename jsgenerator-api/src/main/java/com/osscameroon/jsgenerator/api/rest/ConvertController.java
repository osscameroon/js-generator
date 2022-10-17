package com.osscameroon.jsgenerator.api.rest;

import com.osscameroon.jsgenerator.api.domain.Command;
import com.osscameroon.jsgenerator.api.domain.InlineOutput;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.OutputStreamResolver;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.osscameroon.jsgenerator.core.OutputStreamResolver.EXTENSION;
import static com.osscameroon.jsgenerator.core.OutputStreamResolver.INDEX;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequiredArgsConstructor
@RequestMapping("convert")
public class ConvertController {
    private static final Logger LOGGER = getLogger(ConvertController.class);

    private final OutputStreamResolver inlineOutputStreamResolver;
    private final Converter converter;

    @PostMapping
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
}
