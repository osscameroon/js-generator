package com.osscameroon.jsgenerator.core;

import com.osscameroon.jsgenerator.core.internal.ConverterDefault;
import lombok.NonNull;

import java.io.InputStream;
import java.io.OutputStream;

@FunctionalInterface
public interface Converter {
    default void convert(@NonNull final InputStream inputStream, @NonNull final OutputStream outputStream) {
        convert(inputStream, outputStream, new Configuration());
    }

    void convert(@NonNull final InputStream inputStream, @NonNull final OutputStream outputStream, @NonNull Configuration configuration);

    static Converter of(@NonNull final VariableNameStrategy variableNameStrategy) {
        return new ConverterDefault(variableNameStrategy);
    }
}
