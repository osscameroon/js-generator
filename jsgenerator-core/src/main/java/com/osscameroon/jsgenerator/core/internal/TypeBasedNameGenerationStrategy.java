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
package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.NameGenerationStrategy;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;

/**
 * TypeBasedNameGenerationStrategy
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 03, 2022 @t 02:48:58
 */
public class TypeBasedNameGenerationStrategy implements NameGenerationStrategy {
    private final Map<String, AtomicLong> counters = new HashMap<>();

    @Override
    public String nextName(@NonNull String type) {
        return format("%s_%03d", type, counters.computeIfAbsent(type, __ -> new AtomicLong()).getAndIncrement());
    }
}
