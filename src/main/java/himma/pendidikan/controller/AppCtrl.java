package himma.pendidikan.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AppCtrl {
    @FXML
    private AnchorPane contentPane;
    @FXML
    public VBox vbMenu;

    private static AppCtrl instance;

    public AppCtrl() {}

    @FXML
    public void initialize() {
        instance = this; // <--- ini yang penting!
    }

    public static AppCtrl getInstance() {
        return instance;
    }

    public AnchorPane getContentPane() {
        return contentPane;
    }

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void loadPage(String fxmlPath) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/login/index.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) contentPane.getScene().getWindow();
            Scene scene = new Scene(root, stage.getWidth(), stage.getHeight()); // supaya langsung matching
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
