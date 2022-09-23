/*MIT License

Copyright (c) 2020 OSS Cameroon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/
package com.osscameroon.jsgenerator.cli;

import com.osscameroon.jsgenerator.cli.internal.InlineOutputFilenameResolver;
import com.osscameroon.jsgenerator.cli.internal.PathOutputFilenameResolver;
import com.osscameroon.jsgenerator.cli.internal.StdinOutputFilenameResolver;
import lombok.NonNull;

import java.util.Map;

/**
 * OutputFilenameResolver
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 04, 2022 @t 23:03:38
 */
@FunctionalInterface
public interface OutputFilenameResolver {
    String ORIGINAL_DIRECTORY = "original-directory";
    String ORIGINAL_EXTENSION = "original-extension";
    String ORIGINAL_BASENAME = "original-basename";
    String EXTENSION = "extension";
    String ORIGINAL = "original";
    String INDEX = "index";

    String resolve(@NonNull final String template, @NonNull final Map<String, Object> container);

    static OutputFilenameResolver ofInline() {
        return new InlineOutputFilenameResolver();
    }

    static OutputFilenameResolver ofStdin() {
        return new StdinOutputFilenameResolver();
    }

    static OutputFilenameResolver ofPath() {
        return new PathOutputFilenameResolver();
    }
}
