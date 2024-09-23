package com.osscameroon.jsgenerator.test.api.helper;

import org.springframework.test.web.servlet.ResultMatcher;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public class MultipartResultMatcher {
    private static final MultipartResultMatcher INSTANCE = new MultipartResultMatcher();

    private MultipartResultMatcher() {
    }

    public IndexedMultipart nth(final int index) {
        return new IndexedMultipart(index);
    }

    public ResultMatcher size(final int size) {
        return match((getHeader, content) -> {
            final var partsCount = parts(getHeader, content).length;

            assertThat(size).withFailMessage(() ->
                    "Multipart got %s parts: expected %d parts".formatted(partsCount, size)
            ).isEqualTo(partsCount);
        });
    }

    public static MultipartResultMatcher multipart() {
        return INSTANCE;
    }

    public static MultipartResultMatcher withMultipart() {
        return multipart();
    }

    static String[] parts(Function<String, String> getHeader, String content) {
        final var parts = content.split(boundary(getHeader));
        return of(parts).subList(1, parts.length - 1).toArray(new String[0]);
    }

    static String boundary(Function<String, String> getHeader) {
        return "--" + getHeader.apply(CONTENT_TYPE).split("boundary=")[1];
    }

    static ResultMatcher match(final BiConsumer<Function<String, String>, String> biConsumer) {
        return result -> {
            final var response = result.getResponse();
            biConsumer.accept(response::getHeader, response.getContentAsString());
        };
    }

    public record IndexedMultipart(int index) {
        public ResultMatcher exists() {
            return match((getHeader, content) -> {
                final var partsCount = parts(getHeader, content).length;

                assertThat(index).withFailMessage(() ->
                        "Multipart nth(%s) does not exist: only got %d parts".formatted(index, partsCount)
                ).isGreaterThanOrEqualTo(0).isLessThan(partsCount);
            });
        }

        public <T> IndexedMappedMultipart<T> map(final Function<String, T> mapper) {
            return new IndexedMappedMultipart<>(mapper, index);
        }
    }

    public record IndexedMappedMultipart<T>(Function<String, T> mapper, int index) {

        public ResultMatcher passContent(final AssertionConsumer<T> consumer) {
            return match((getHeader, content) -> {
                final var parts = parts(getHeader, content);

                assertThat(index).withFailMessage(() ->
                        "Multipart nth(%s) cannot pass: only got %d parts".formatted(index, parts.length)
                ).isGreaterThanOrEqualTo(0).isLessThan(parts.length);

                final var part = parts[index].split("(\r?\n){2}", 2)[1];
                final var mappedPart = mapper.apply(part);

                try {
                    consumer.accept(mappedPart);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public interface AssertionConsumer<T> {
        void accept(final T value) throws Exception;
    }
}
