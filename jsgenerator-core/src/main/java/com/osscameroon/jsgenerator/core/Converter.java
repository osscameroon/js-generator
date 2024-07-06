package com.osscameroon.jsgenerator.core;

import com.osscameroon.jsgenerator.core.internal.ConverterDefault;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@FunctionalInterface
public interface Converter {
    default void convert(@NonNull final InputStream inputStream, @NonNull final OutputStream outputStream) throws IOException {
        convert(inputStream, outputStream, new Configuration());
    }

    void convert(@NonNull final InputStream inputStream, @NonNull final OutputStream outputStream, @NonNull Configuration configuration) throws IOException;

    static Converter of() {
        return new ConverterDefault();
    }
}
