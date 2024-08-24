package com.osscameroon.jsgenerator.desktop.controller;

import com.osscameroon.jsgenerator.core.Converter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public final class HelloViewController {
    private final Converter converter;

    @FXML
    private TextArea inputArea;
    @FXML
    private Label outputLabel;

    public HelloViewController(Converter converter) {
        this.converter = converter;
    }

    @FXML
    private void convert() throws IOException {
        try (var stream = new ByteArrayOutputStream()) {
            converter.convert(new ByteArrayInputStream(inputArea.textProperty().getValue().getBytes()), stream);
            outputLabel.setText(stream.toString(StandardCharsets.UTF_8));
        }
    }
}
