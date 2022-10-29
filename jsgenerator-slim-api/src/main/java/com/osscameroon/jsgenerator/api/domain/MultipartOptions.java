package com.osscameroon.jsgenerator.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, builderMethodName = "deprecatedBuilder")
public class MultipartOptions extends Options {
    private String pattern = "{{ index }}.{{ original }}{{ extension }}";
}
