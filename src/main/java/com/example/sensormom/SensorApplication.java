package com.example.sensormom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SensorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SensorApplication.class.getResource("hello-view.fxml"));
        loader.setResources(ResourceBundle.getBundle("com.example.sensormom.i18n", new Locale("pt_br", "pt_BR")));
        Scene scene = new Scene(loader.load(), 723, 365);
        Image image = new Image("file:sensor.png");
        stage.getIcons().add(image);
        stage.setTitle("Sensor MOM!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        System.out.println("Stage is closing");
        try {
            Context.getInstance().closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}