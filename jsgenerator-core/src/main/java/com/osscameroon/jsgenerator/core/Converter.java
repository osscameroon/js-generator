package com.osscameroon.jsgenerator.core;

import com.osscameroon.jsgenerator.core.internal.ConverterDefault;
import lombok.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

@FunctionalInterface
public interface Converter {
    default void convert(@NonNull final InputStream inputStream, @NonNull final OutputStream outputStream) throws IOException {
        convert(inputStream, outputStream, new Configuration());
    }

    /**
     * A helper method to work with language-native String and array of data structures.
     *
     * @param input         The input HTML string
     * @param configuration The object related to variable declaration (let, const or var) and query selector
     * @return Lines of output JS code
     */
    default String[] convert(@NonNull String input, Configuration configuration) throws IOException{
        final var inputStream = new ByteArrayInputStream(input.getBytes(UTF_8));
        final var outputStream = new ByteArrayOutputStream();

        convert(inputStream, outputStream, configuration);

        return outputAsStrippedLines(outputStream);
    }

    private String[] outputAsStrippedLines(ByteArrayOutputStream outputStream) {
        return outputStream
                .toString(UTF_8)
                .lines()
                .map(String::strip)
                .filter(line -> !line.isEmpty())
                .toArray(String[]::new);
    }


    void convert(@NonNull final InputStream inputStream, @NonNull final OutputStream outputStream, @NonNull Configuration configuration) throws IOException;

    static Converter of() {
        return new ConverterDefault();
    }
}
