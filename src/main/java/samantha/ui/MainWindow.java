package samantha.ui;

import java.util.List;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import samantha.Samantha;

/**
 * Controls Samantha's main JavaFX conversation window.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private final Image userImage = loadImage("/images/Theodore.png");
    private final Image samanthaImage = loadImage("/images/SamanthaAvatar.svg");
    private Samantha samantha;
    private boolean hasShownInitialResponses;

    /**
     * Initializes the dialog container's automatic scroll behavior.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the backend used to process commands and displays the greeting.
     *
     * @param samantha backend application instance
     */
    public void setSamantha(Samantha samantha) {
        this.samantha = samantha;
        if (!hasShownInitialResponses) {
            List<String> initialResponses = samantha.getInitialResponses();
            for (String response : initialResponses) {
                dialogContainer.getChildren().add(DialogBox.getSamanthaDialog(response, samanthaImage));
            }
            hasShownInitialResponses = true;
        }
        userInput.requestFocus();
    }

    /**
     * Processes the command entered in the text field.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        String response = samantha.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getSamanthaDialog(response, samanthaImage));
        userInput.clear();
        userInput.requestFocus();

        if (samantha.isExitRequested()) {
            closeAfterFarewell();
        }
    }

    /**
     * Leaves the farewell visible before closing the JavaFX window.
     */
    private void closeAfterFarewell() {
        PauseTransition pause = new PauseTransition(Duration.millis(700));
        pause.setOnFinished(event -> {
            Stage stage = (Stage) userInput.getScene().getWindow();
            stage.close();
        });
        pause.play();
    }

    /**
     * Loads an image from the classpath resources.
     *
     * @param resourcePath absolute classpath path
     * @return loaded image
     */
    private static Image loadImage(String resourcePath) {
        return new Image(MainWindow.class.getResourceAsStream(resourcePath));
    }
}
