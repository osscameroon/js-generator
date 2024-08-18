package com.osscameroon.jsgenerator.desktop.autoconfigure;

import com.osscameroon.jsgenerator.core.autoconfigure.JsGeneratorCoreAutoconfigure;
import com.osscameroon.jsgenerator.desktop.controller.FxmlNavigator;
import com.osscameroon.jsgenerator.desktop.controller.FxmlResolver;
import com.osscameroon.jsgenerator.desktop.controller.HelloViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@ImportAutoConfiguration(JsGeneratorCoreAutoconfigure.class)
@SpringBootApplication(scanBasePackageClasses = HelloViewController.class)
public class JsGeneratorDesktop extends Application {
    private static ApplicationContext context;
    private static FxmlResolver fxmlResolver;
    private static Scene scene;

    public static void main(String[] args) {
        context = SpringApplication.run(JsGeneratorDesktop.class, args);
        fxmlResolver = context.getBean(FxmlResolver.class);
        launch(JsGeneratorDesktop.class, args);
    }

    /**
     * Inject this bean to navigate from one view to another, like a router.
     *
     * @return
     */
    @Bean
    @Lazy
    public FxmlNavigator fxmlNavigator() {
        return scene::setRoot;
    }

    @Bean
    public FxmlResolver fxmlResolver() {
        return path -> {
            path = "com/osscameroon/jsgenerator/desktop/controller/%s.fxml".formatted(path);
            final var loader = new FXMLLoader(new ClassPathResource(path).getURL());
            loader.setControllerFactory(context::getBean);
            return (Parent) loader.load();
        };
    }

    @Override
    public void start(Stage stage) throws IOException {
        final var parent = fxmlResolver.resolve("hello-view");
        stage.setScene(scene = new Scene(parent));
        stage.show();
    }
}
