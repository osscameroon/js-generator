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

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Valid
 * <p>
 * An interface to separate validation concern of {@link Command}.
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 04, 2022 @t 22:12:03
 */
public interface Valid {
    /**
     * Validate the {@link Command} this interface is implemented on.
     *
     * @return {@code 0} is the command is valid, {@code -1} is some paths doesn't exist, {@code -2} is some paths
     * weren't referencing regular files as per {@link java.nio.file.Files#isRegularFile(Path, LinkOption...)}
     * regular file understanding.
     */
    default int isValid() {
        if (!(this instanceof Command)) return -999;
        final var command = (Command) this;
        final var nonExistent = new ArrayList<Path>();
        final var nonRegularFiles = new ArrayList<Path>();

        for (final var path : command.getPaths()) {
            if (!Files.exists(path)) nonExistent.add(path.toAbsolutePath());
            else if (!Files.isRegularFile(path)) nonRegularFiles.add(path.toAbsolutePath());
        }

        if (!nonExistent.isEmpty()) {
            System.err.println("Some path does not exists:");

            for (final var path : nonExistent)
                System.err.printf("  - %s%n", path.toAbsolutePath());
        }

        if (!nonRegularFiles.isEmpty()) {
            System.err.println("Some path are not regular files:");

            for (final var path : nonRegularFiles)
                System.err.printf("  - %s%n", path.toAbsolutePath());
        }

        if (!nonExistent.isEmpty()) return -1;

        if (!nonRegularFiles.isEmpty()) return -2;

        return 0;
    }
}
