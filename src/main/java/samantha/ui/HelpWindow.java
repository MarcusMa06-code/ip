package samantha.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

/**
 * Controls Samantha's standalone command guide.
 */
public class HelpWindow {

    /**
     * Copies the command example attached to the clicked button.
     *
     * @param event copy-button action event
     */
    @FXML
    private void copyCommand(ActionEvent event) {
        Button copyButton = (Button) event.getSource();
        String command = (String) copyButton.getUserData();
        ClipboardContent content = new ClipboardContent();
        content.putString(command);
        Clipboard.getSystemClipboard().setContent(content);
        copyButton.setText("Copied");
    }

    /**
     * Closes the help window.
     */
    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
