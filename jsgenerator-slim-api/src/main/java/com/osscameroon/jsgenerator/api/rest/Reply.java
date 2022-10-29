package com.osscameroon.jsgenerator.api.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor(access = PRIVATE)
public class Reply<T> {
    private T content;
    private Status status;

    public static <T> Reply<T> ofSuccess(final T content) {
        return new SuccessReply<>(content);
    }

    public static <T> Reply<? extends List<? extends T>> ofSuccesses(final List<T> contents) {
        return new SuccessReplies<>(contents);
    }

    public enum Status {
        SUCCESS, ERROR;
    }

    public static class SuccessReply<T> extends Reply<T> {
        public SuccessReply(final T content) {
            super(content, Status.SUCCESS);
        }
    }

    public static class SuccessReplies<T> extends Reply<List<? extends T>> {
        public SuccessReplies(final List<T> content) {
            super(content, Status.SUCCESS);
        }
    }
}
