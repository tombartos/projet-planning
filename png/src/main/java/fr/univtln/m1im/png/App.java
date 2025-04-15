package fr.univtln.m1im.png;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import fr.univtln.m1im.png.gui.LoginPage;

/**
 * Hello world!
 */
@Slf4j
public final class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)  {
        int width = 1200;
        int height = 800;
        LoginPage.showLoginPage(stage, width, height);
    }

    @Override
    public void stop() {
        var emf = Utils.getEntityManagerFactory();
        if (emf != null) emf.close();
    }
}

// Pour executer cette classe,
//
//     mvn exec:java
//
// ce qui revient à
//
//     mvn exec:java "-Dexec.mainClass=fr.univtln.m1im.png.App"
