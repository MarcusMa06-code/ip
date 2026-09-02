package samantha;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    public void start(Stage stage) {
        Scene scene = new Scene(new Label("Samantha"));
        stage.setScene(scene);
        stage.setTitle("Samantha");
        stage.show();
    }
}
