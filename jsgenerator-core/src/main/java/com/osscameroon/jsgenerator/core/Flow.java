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
package com.osscameroon.jsgenerator.core;

import com.osscameroon.jsgenerator.core.internal.FlowDefault;
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
 * Flow
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 02, 2022 @t 22:23:02
 */
public interface Flow {
    @NonNull
    OutputStream getOutputStream();

    @NonNull
    InputStream getInputStream();

    @NonNull
    static List<Flow> from(@NonNull final Configuration configuration) {
        return from(configuration, new String[0]);
    }

    @NonNull
    static List<Flow> from(@NonNull final Configuration configuration, @NonNull String[] arguments) {
        final ArrayList<Flow> flows = new ArrayList<>();
        final var output = configuration.getOutput();
        final var input = configuration.getInput();

        for (var i = 0; i < arguments.length; i++) {
            flows.add(new FlowDefault(
                resolveOutputStream(output, input, arguments[i], i),
                resolveInputStream(input, arguments[i])));
        }

        return unmodifiableList(flows);
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
