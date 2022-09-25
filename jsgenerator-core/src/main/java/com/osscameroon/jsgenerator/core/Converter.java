package com.osscameroon.jsgenerator.core;

import com.osscameroon.jsgenerator.core.internal.ConverterDefault;
import lombok.NonNull;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Converter
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 02, 2022 @t 23:19:40
 */
@FunctionalInterface
public interface Converter {
    void convert(@NonNull final InputStream inputStream, @NonNull final OutputStream outputStream);

    static Converter of(@NonNull final VariableNameStrategy variableNameStrategy) {
        return new ConverterDefault(variableNameStrategy);
    }
}
