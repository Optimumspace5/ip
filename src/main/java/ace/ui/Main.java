package ace.ui;

import java.io.IOException;

import ace.Ace;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Ace using FXML.
 */
public class Main extends Application {

    private final Ace ace = new Ace();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();

            stage.setTitle("Ace");
            stage.setScene(new Scene(root));

            MainWindow controller = fxmlLoader.getController();
            controller.setAce(ace);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load MainWindow.fxml", e);
        }
    }
}