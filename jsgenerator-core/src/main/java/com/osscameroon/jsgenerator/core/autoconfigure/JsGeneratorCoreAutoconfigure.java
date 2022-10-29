package com.osscameroon.jsgenerator.core.autoconfigure;

import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.OutputStreamResolver;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class JsGeneratorCoreAutoconfigure {
    @Bean
    public OutputStreamResolver pathOutputStreamResolver() {
        return OutputStreamResolver.ofPath();
    }

    @Bean
    public OutputStreamResolver stdinOutputStreamResolver() {
        return OutputStreamResolver.ofStdin();
    }

    @Bean
    public OutputStreamResolver inlineOutputStreamResolver() {
        return OutputStreamResolver.ofInline();
    }

    @Bean
    public Converter converter() {
        return Converter.of();
    }
}
