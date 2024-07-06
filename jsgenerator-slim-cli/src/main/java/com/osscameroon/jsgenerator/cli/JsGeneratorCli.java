package com.osscameroon.jsgenerator.cli;

import com.osscameroon.jsgenerator.cli.internal.CommandDefault;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.OutputStreamResolver;
import com.osscameroon.jsgenerator.core.autoconfigure.JsGeneratorCoreAutoconfigure;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import picocli.CommandLine;

import java.util.List;

import static java.lang.System.exit;
import static org.springframework.boot.SpringApplication.run;

@EnableAutoConfiguration
@SpringBootConfiguration
@Import(JsGeneratorCoreAutoconfigure.class)
public class JsGeneratorCli {
    public static void main(String[] args) {
        run(JsGeneratorCli.class, 0 == args.length ? new String[]{"--help"} : args);
    }

    @Bean
    public CommandLineRunner picoCliRunner(final Command command) {
        final var commandLine = new CommandLine(command);
        return args -> exit(commandLine.execute(args));
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
