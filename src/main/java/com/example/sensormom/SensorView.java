package com.example.sensormom;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class SensorView extends ResizableView {
    @FXML
    public TextField valueField;
    @FXML
    public Label maxValue;
    @FXML
    public Label minValue;
    @FXML
    public Label actualValue;
    @FXML
    public Label parameter;
    @FXML
    public Label sensorId;
    public CheckBox generateValue;

    private Thread t;

    ResourceBundle bundle = ResourceBundle.getBundle("com.example.sensormom.i18n", new Locale("pt_br", "pt_BR"));

    @FXML
    public void initialize() {

        try {
            Context context = Context.getInstance();
            parameter.setText(context.getSensorParameter());
            maxValue.setText(context.getMaxLimit().toString());
            minValue.setText(context.getMinLimit().toString());
            actualValue.setText(context.getActualLimit().toString());
            valueField.setText(context.getActualLimit().toString());
            sensorId.setText(context.getConnection().getConnectionInfo().getConnectionId().getValue());

            valueField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.matches("^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,]{1}[0-9]+$")) return;
                valueField.setText(newValue.replaceAll("[^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,]{1}[0-9]+$]", ""));
            });
            this.onHelloButtonClick(null);

            Timeline fiveSecondsWonder = new Timeline(
                    new KeyFrame(Duration.seconds(5),
                            event -> {
                                if (generateValue.isSelected()) {
                                    try {
                                        double randomNum = ThreadLocalRandom.current().nextDouble(context.getMinLimit() - 20, context.getMaxLimit() + 20);
                                        valueField.setText(Double.toString(randomNum));
                                        this.onHelloButtonClick(null);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }));
            fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
            fiveSecondsWonder.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onGenerateValueChange(ActionEvent event) {}

    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        try {
            Double value = Double.parseDouble(valueField.getText());
            Context context = Context.getInstance();
            if (context.getMaxLimit().compareTo(value) < 0) {
                context.sendMensage(
                        "{\"message\": \"MAX_LIMIT_REACHED\", \"value\":" + value +
                                ", \"min\": " + context.getMinLimit() +
                                ", \"max\": " + context.getMaxLimit() + "}"
                );
            }
            if (context.getMinLimit().compareTo(value) > 0) {
                context.sendMensage(
                        "{\"message\": \"MIN_LIMIT_REACHED\", \"value\":" + value +
                                ", \"min\": " + context.getMinLimit() +
                                ", \"max\": " + context.getMaxLimit() + "}"
                );
            }
            context.setActualLimit(value);
            actualValue.setText(context.getActualLimit().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}