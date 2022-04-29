package com.example.sensormom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.jms.InvalidClientIDException;
import javax.jms.JMSException;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SensorController extends ResizableView {
    @FXML
    public ChoiceBox choiceBox;
    @FXML
    public TextField maxField;
    @FXML
    public TextField minField;
    @FXML
    public TextField valueField;
    @FXML
    public TextField idField;
    @FXML
    private Label welcomeText;

    ResourceBundle bundle = ResourceBundle.getBundle("com.example.sensormom.i18n", new Locale("pt_br", "pt_BR"));

    @FXML
    public void initialize() {
        choiceBox.getItems().add(bundle.getString("parameter.one"));
        choiceBox.getItems().add(bundle.getString("parameter.two"));
        choiceBox.getItems().add(bundle.getString("parameter.three"));

        maxField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,]{1}[0-9]+$")) return;
            maxField.setText(newValue.replaceAll("[^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,]{1}[0-9]+$]", ""));
        });
        minField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,]{1}[0-9]+$")) return;
            minField.setText(newValue.replaceAll("[^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,]{1}[0-9]+$]", ""));
        });

        valueField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,]{1}[0-9]+$")) return;
            valueField.setText(newValue.replaceAll("[^[\\\\+\\\\-]{0,1}[0-9]+[\\\\.\\\\,]{1}[0-9]+$]", ""));
        });
    }

    @FXML
    protected void onHelloButtonClick(ActionEvent event) throws IOException, JMSException {
        try {
            if(idField.getText() == null || idField.getText().equals("")) {
                throw new Exception("ID is null!");
            }
            if (choiceBox.getValue() == null) {
                throw new Exception("Choice is null!");
            }
            Double.parseDouble(maxField.getText());
            Double.parseDouble(minField.getText());
            Double.parseDouble(valueField.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("hello.startTitleError"));
            alert.getDialogPane().setContent( new Label(bundle.getString("hello.startTextError")));
            alert.show();
            return;
        }
        try {
            Context.clientID = idField.getText();
            Context context = Context.getInstance();
            context.setSensorParameter((String) choiceBox.getValue());
            context.setActualLimit(Double.parseDouble(valueField.getText()));
            context.setMinLimit(Double.parseDouble(minField.getText()));
            context.setMaxLimit(Double.parseDouble(maxField.getText()));
            this.switchBetweenScreen(((Node) event.getSource()).getScene(), "sensor-view.fxml");
        } catch (InvalidClientIDException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("hello.sensorIDTitleError"));
            alert.getDialogPane().setContent( new Label(bundle.getString("hello.sensorIDTextError")));
            alert.show();
        }
    }
}