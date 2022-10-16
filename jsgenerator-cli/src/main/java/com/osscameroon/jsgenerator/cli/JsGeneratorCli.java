package com.osscameroon.jsgenerator.cli;

import com.osscameroon.jsgenerator.cli.internal.CommandDefault;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.OutputStreamResolver;
import com.osscameroon.jsgenerator.core.VariableNameStrategy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import picocli.CommandLine;

import static java.lang.System.exit;
import static org.springframework.boot.SpringApplication.run;

@SpringBootConfiguration
@SuppressWarnings("SpringFacetCodeInspection")
public class JsGeneratorCli {
    public static void main(String[] args) {
        //noinspection resource
        run(JsGeneratorCli.class, args);
    }

    @Bean
    public CommandLineRunner picoCliRunner(final Command command) {
        final var commandLine = new CommandLine(command);
        return args -> exit(commandLine.execute(args));
    }

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
    public VariableNameStrategy typeBasedNameGenerationStrategy() {
        return VariableNameStrategy.ofTypeBased();
    }

    @Bean
    public Converter converter(final VariableNameStrategy typeBasedVariableNameStrategy) {
        return Converter.of();
    }

    @Bean
    public Command command(final Converter converter,
                           final OutputStreamResolver pathOutputStreamResolver,
                           final OutputStreamResolver stdinOutputStreamResolver,
                           final OutputStreamResolver inlineOutputStreamResolver) {
        return new CommandDefault(
                inlineOutputStreamResolver,
                stdinOutputStreamResolver,
                pathOutputStreamResolver,
                converter);
    }
}
