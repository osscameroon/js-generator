package com.osscameroon.jsgenerator.cli.internal;

import com.osscameroon.jsgenerator.cli.Command;
import com.osscameroon.jsgenerator.core.Configuration;
import com.osscameroon.jsgenerator.core.OutputStreamResolver;
import com.osscameroon.jsgenerator.cli.Valid;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.VariableDeclaration;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.osscameroon.jsgenerator.core.OutputStreamResolver.*;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

/**
 * CommandDefault
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 04, 2022 @t 21:45:35
 */

@Data
@RequiredArgsConstructor
@CommandLine.Command(
        name = "jsgenerator",
        version = "0.0.1-SNAPSHOT",
        mixinStandardHelpOptions = true,
        description = "Translating files, stdin or inline from HTML to JS")
public class CommandDefault implements Command, Valid {
    private final OutputStreamResolver inlineFilenameResolver;
    private final OutputStreamResolver stdinFilenameResolver;
    private final OutputStreamResolver pathFilenameResolver;
    private final Converter converter;

    @Option(names = {"-k", "--keyword"}, description = "variable declaration keyword")

    //TODO:show options for variable declarations, test what we get based on user choice let const or var

    private VariableDeclaration variableDeclaration = VariableDeclaration.LET;
    @Option(names = {"-t", "--tty"}, description = "output to stdin, not files")
    private boolean tty;

    @Parameters(index = "0..*", description = "file paths to translate content, parsed as HTML")
    private List<Path> paths = new ArrayList<>();

    @Option(
            arity = "1..*", names = {"-i", "--inline"}, description = "args as HTML content, not files")
    private List<String> inlineContents = new ArrayList<>();

    @Option(names = {"-e", "--ext"}, defaultValue = ".jsgenerator.js", description = "output files' extension")
    private String extension = ".jsgenerator.js";

    @Option(
            names = "--stdin-pattern",
            defaultValue = "stdin{{ extension }}",
            description = "pattern for stdin output filenames")
    private String stdinPattern = format("stdin{{ %s }}", EXTENSION);

    @Option(
            names = "--path-pattern",
            defaultValue = "{{ original }}{{ extension }}",
            description = "pattern for path-based output filenames")
    private String pathPattern = format("{{ %s }}{{ %s }}", ORIGINAL, EXTENSION);

    @Option(
            names = "--inline-pattern",
            defaultValue = "inline.{{ index }}{{ extension }}",
            description = "Pattern for inline output filename")
    private String inlinePattern = format("inline.{{ %s }}{{ %s }}", INDEX, EXTENSION);

    @Override
    public Integer call() throws IOException {
        final var IS_VALID = isValid();
        if (0 > IS_VALID) return IS_VALID;
        OutputStream outputStream;

        // NOTE: Start with standard input, if not blank
        final var stdinReader = new BufferedReader(new InputStreamReader(System.in));
        final var builder = new StringBuilder();
        String line;
        while (0 < System.in.available() && null != (line = stdinReader.readLine()))
            builder.append(line).append("\r\n");

        if (0 < builder.length()) {
            System.err.println("\fTranslating [stdin]: <<");
            converter.convert(
                    new ByteArrayInputStream(builder.toString().getBytes(UTF_8)),
                    outputStream = resolveStdinOutputStream());
            outputStream.flush();
        }

        // NOTE: Continue with inline HTML content
        for (var i = 0; i < inlineContents.size(); i++) {
            if (0 < inlineContents.get(i).length()) {
                System.err.printf("\fTranslating [inline]: %d%n", i);
                converter.convert(
                        new ByteArrayInputStream(inlineContents.get(i).getBytes(UTF_8)),
                        outputStream = resolveInlineOutputStream(i));
                outputStream.flush();
            }
        }

        // NOTE: Process paths
        for (final var path : paths.stream().map(Path::toAbsolutePath).distinct().collect(toList())) {
            System.err.printf("\fTranslating: %s%n", path);
            converter.convert(
                    Files.newInputStream(path),
                    outputStream = resolvePathOutputStream(path),
                    new Configuration(variableDeclaration));
            outputStream.flush();
            outputStream.close();
        }

        return 0;
    }

    private OutputStream resolveStdinOutputStream() throws IOException {
        if (tty) return System.out;

        final var outputPathname = pathFilenameResolver.resolve(pathPattern, Map.of(EXTENSION, extension));
        final var outputPath = Path.of(outputPathname);

        return Files.newOutputStream(outputPath);
    }

    private OutputStream resolveInlineOutputStream(final int index) throws IOException {
        if (tty) return System.out;

        final var outputPathname = pathFilenameResolver.resolve(pathPattern, Map.of(
                EXTENSION, extension,
                INDEX, index
        ));
        final var outputPath = Path.of(outputPathname);

        return Files.newOutputStream(outputPath);
    }

    private OutputStream resolvePathOutputStream(Path path) throws IOException {
        if (tty) return System.out;

        final var nameCount = path.getNameCount();
        final var outputPathname = pathFilenameResolver.resolve(pathPattern, Map.of(
                ORIGINAL_EXTENSION, path.getName(nameCount - 1).toString()
                        .replace("^.*\\.(.*)$", "$1"),
                ORIGINAL_DIRECTORY, path.getName(nameCount - 2).toString(),
                ORIGINAL_BASENAME, path.getName(nameCount - 1).toString(),
                ORIGINAL, path.toString(),
                EXTENSION, extension
        ));
        final var outputPath = Path.of(outputPathname);

        return Files.newOutputStream(outputPath);
    }
}
