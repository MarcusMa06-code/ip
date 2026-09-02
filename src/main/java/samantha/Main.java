package samantha;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Starts the Samantha JavaFX user interface.
 */
public class Main extends Application {

    /**
     * Configures and displays Samantha's initial JavaFX stage.
     *
     * @param stage primary JavaFX stage
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane mainWindow = fxmlLoader.load();
        fxmlLoader.<samantha.ui.MainWindow>getController().setSamantha(new Samantha());

        Scene scene = new Scene(mainWindow);
        stage.setMinWidth(520);
        stage.setMinHeight(680);
        stage.setTitle("Samantha");
        stage.setScene(scene);
        stage.show();
    }
}
