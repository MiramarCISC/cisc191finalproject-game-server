package edu.sdccd.cisc191.client.ui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;

public class NumberHelper {
    public static TextFormatter<? extends TextInputControl> numberFormatter() {
        return new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        });
    }

    public static long parseLongOrAlert(String s, String inputName) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("The " + inputName + " provided is invalid.");
            alert.showAndWait();

            throw e;
        }
    }
}
