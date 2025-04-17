package himma.pendidikan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class MainApp extends Application {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private double screenWidth = screenSize.getWidth();
    private double screenHeight = screenSize.getHeight();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/login/index.fxml"));
        Scene splashScene = new Scene(splashLoader.load());
//        stage.setMinWidth(screenWidth-5);
//        stage.setMinHeight(screenHeight-25);
        stage.setScene(splashScene);
        stage.setMaximized(true); // auto mengikuti ukuran layar
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
