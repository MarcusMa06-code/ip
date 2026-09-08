package samantha.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Represents a conversation bubble with a speaker avatar and text.
 */
public class DialogBox extends HBox {
    private static final double AVATAR_RADIUS = 22;
    private static final double AVATAR_HORIZONTAL_CROP_POSITION = 0.70;

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Loads the FXML-backed dialog layout.
     *
     * @param text message text
     * @param image speaker avatar
     */
    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load the dialog box layout.", e);
        }

        dialog.setText(text);
        displayPicture.setImage(image);
        displayPicture.setViewport(createAvatarViewport(image));
        displayPicture.setClip(new Circle(AVATAR_RADIUS, AVATAR_RADIUS, AVATAR_RADIUS));
        getStyleClass().add("dialog-box");
    }

    /**
     * Creates a user message aligned to the right.
     *
     * @param text message text
     * @param image user avatar
     * @return right-aligned user dialog
     */
    public static DialogBox getUserDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.getStyleClass().add("user-dialog");
        return dialogBox;
    }

    /**
     * Creates a Samantha message aligned to the left.
     *
     * @param text message text
     * @param image Samantha avatar
     * @return left-aligned Samantha dialog
     */
    public static DialogBox getSamanthaDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        dialogBox.getStyleClass().add("samantha-dialog");
        return dialogBox;
    }

    /**
     * Reverses the avatar and text order for Samantha's left-aligned reply.
     */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Returns a square viewport that keeps the approved horizontal avatar crop.
     *
     * @param image source avatar image
     * @return square viewport for the avatar
     */
    private static Rectangle2D createAvatarViewport(Image image) {
        double cropSize = Math.min(image.getWidth(), image.getHeight());
        double x = AVATAR_HORIZONTAL_CROP_POSITION * (image.getWidth() - cropSize);
        double y = (image.getHeight() - cropSize) / 2;
        return new Rectangle2D(x, y, cropSize, cropSize);
    }
}
