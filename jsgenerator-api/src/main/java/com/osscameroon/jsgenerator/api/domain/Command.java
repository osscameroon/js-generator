package com.osscameroon.jsgenerator.api.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Command
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 13, 2022 @t 05:15:54
 */
@Data
public class Command {
    private String extension = ".jsgenerator.js";
    private String pathPattern = "{{ original }}{{ extension }}";
    @Size(min = 1)
    private List<@NotNull String> inlineContents = new ArrayList<>();
    private String inlinePattern = "inline.{{ index }}{{ extension }}";
}
