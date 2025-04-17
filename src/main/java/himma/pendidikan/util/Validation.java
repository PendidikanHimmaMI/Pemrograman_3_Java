package himma.pendidikan.util;

import javafx.scene.control.TextField;

import java.sql.*;

public class Validation {
    public String getString(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return rs.getString(columnName);
        } catch (SQLException e) {
            return null;
        }
    }

    public Integer getInt(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return rs.getInt(columnName);
        } catch (SQLException e) {
            return null;
        }
    }

    public static void setLetters(TextField textField) {
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("[a-zA-Z\\s]*")) {
                textField.setText(oldText);
            }
        });
    }

    public static void setLetters(TextField textField, int maxLength) {
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("[a-zA-Z\\s]*") || newText.length() > maxLength) {
                textField.setText(oldText);
            }
        });
    }

    public static void setNumbers(TextField textField) {
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*")) {
                textField.setText(oldText);
            }
        });
    }

    public static void setNumbers(TextField textField, int maxLength) {
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*") || newText.length() > maxLength) {
                textField.setText(oldText);
            }
        });
    }
}
