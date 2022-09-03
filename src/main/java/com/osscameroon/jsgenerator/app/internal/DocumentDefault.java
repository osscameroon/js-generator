package com.osscameroon.jsgenerator.app.internal;

import com.osscameroon.jsgenerator.app.Document;
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
public class DocumentDefault implements Document {
    @NonNull
    private OutputStream outputStream;
    @NonNull
    private InputStream inputStream;
}
