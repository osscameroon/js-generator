package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.Flow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * DocumentDefault
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 02, 2022 @t 23:13:01
 */
@Getter
@AllArgsConstructor
public class FlowDefault implements Flow {
    @NonNull
    private OutputStream outputStream;
    @NonNull
    private InputStream inputStream;
}
