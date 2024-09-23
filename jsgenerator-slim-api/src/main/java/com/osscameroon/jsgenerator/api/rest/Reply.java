package com.osscameroon.jsgenerator.api.rest;

import java.util.List;

public sealed class Reply<T> {
    private final T content;
    private final Status status;

    private Reply(T content, Status status) {
        this.content = content;
        this.status = status;
    }

    public T getContent() {
        return content;
    }

    public Status getStatus() {
        return status;
    }

    public static <T> Reply<T> ofSuccess(final T content) {
        return new Success<>(content);
    }

    public static <T> Reply<? extends List<? extends T>> ofSuccesses(final List<T> contents) {
        return new Successes<>(contents);
    }

    public enum Status {
        SUCCESS, ERROR;
    }

    public static final class Success<T> extends Reply<T> {
        public Success(final T content) {
            super(content, Status.SUCCESS);
        }
    }

    public static final class Successes<T> extends Reply<List<? extends T>> {
        public Successes(final List<T> content) {
            super(content, Status.SUCCESS);
        }
    }
}
