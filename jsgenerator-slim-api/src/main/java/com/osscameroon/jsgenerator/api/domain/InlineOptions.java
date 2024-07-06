package com.osscameroon.jsgenerator.api.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public final class InlineOptions extends Options<InlineOptions> {
    @Size(min = 1)
    private List<@NotNull String> contents = new ArrayList<>();

    public List<String> getContents() {
        return contents;
    }

    public InlineOptions setContents(List<String> contents) {
        this.contents = contents;
        return this;
    }
}
