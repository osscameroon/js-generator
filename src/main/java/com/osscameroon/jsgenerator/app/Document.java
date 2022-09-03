package com.osscameroon.jsgenerator.app;

import com.osscameroon.jsgenerator.app.internal.DocumentDefault;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;
import static java.util.Collections.unmodifiableList;

/**
 * Document
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 02, 2022 @t 22:23:02
 */
public interface Document {
    @NonNull
    OutputStream getOutputStream();

    @NonNull
    InputStream getInputStream();

    @NonNull
    static List<Document> from(@NonNull final Configuration configuration) {
        return from(configuration, new String[0]);
    }

    @NonNull
    static List<Document> from(@NonNull final Configuration configuration, @NonNull String[] arguments) {
        final ArrayList<Document> documents = new ArrayList<>();
        final var output = configuration.getOutput();
        final var input = configuration.getInput();

        for (var i = 0; i < arguments.length; i++) {
            documents.add(new DocumentDefault(
                resolveOutputStream(output, input, arguments[i], i),
                resolveInputStream(input, arguments[i])));
        }

        return unmodifiableList(documents);
    }

    @SneakyThrows
    static InputStream resolveInputStream(@NonNull final Configuration.Input input, final String argument) {
        switch (input) {
            case INLINE:
                return new ByteArrayInputStream(argument.getBytes(UTF_8));
            case STDIN:
                return System.in;
            case FILES:
                return newInputStream(get(argument));
            default:
                throw new UnsupportedOperationException();
        }
    }

    @SneakyThrows
    static OutputStream resolveOutputStream(@NonNull final Configuration.Output output,
                                            @NonNull final Configuration.Input input,
                                            final String argument,
                                            final int index) {
        switch (output) {
            case STDOUT:
                return System.out;
            case FILES:
                var filename = format("%s.js-generator.js", argument);

                switch (input) {
                    case INLINE:
                        filename = format("inline-%d.js-generator.js", index);
                        break;
                    case STDIN:
                        filename = "stdin.js-generator.js";
                        break;
                }

                return newOutputStream(get(filename));
            default:
                throw new UnsupportedOperationException();
        }
    }
}
