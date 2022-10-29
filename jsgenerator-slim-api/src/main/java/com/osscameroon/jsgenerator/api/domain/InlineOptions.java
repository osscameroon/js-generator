package com.osscameroon.jsgenerator.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, builderMethodName = "deprecatedBuilder")
public class InlineOptions extends Options {
    @Size(min = 1)
    private List<@NotNull String> contents = new ArrayList<>();
    private String pattern = "inline.{{ index }}{{ extension }}";
}
