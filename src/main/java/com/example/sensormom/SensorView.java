package com.example.sensormom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Locale;
import java.util.ResourceBundle;

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

            valueField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.matches("^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,]{1}[0-9]+$")) return;
                valueField.setText(newValue.replaceAll("[^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,]{1}[0-9]+$]", ""));
            });
            this.onHelloButtonClick(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        try {
            Double value = Double.parseDouble(valueField.getText());
            Context context = Context.getInstance();
            if (context.getMaxLimit().compareTo(value) < 0) {
                context.sendMensage("MAX_LIMIT_REACHED: " + value);
            }
            if (context.getMinLimit().compareTo(value) > 0) {
                context.sendMensage("MIN_LIMIT_REACHED: " + value);
            }
            context.setActualLimit(value);
            actualValue.setText(context.getActualLimit().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}