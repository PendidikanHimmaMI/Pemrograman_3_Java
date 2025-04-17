package himma.pendidikan.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class SwalAlert {

    public boolean showAlert(AlertType type, String title, String message, boolean withConfirmation) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (withConfirmation && type == AlertType.CONFIRMATION) {
            return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
        } else {
            alert.showAndWait();
            return true;
        }
    }
}
