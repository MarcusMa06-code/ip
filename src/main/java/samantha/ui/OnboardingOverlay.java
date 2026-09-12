package samantha.ui;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Plays a short opening sequence before the conversation becomes interactive.
 */
public class OnboardingOverlay extends StackPane {
    private static final Duration NAME_DURATION = Duration.millis(480);
    private static final Duration LINE_DURATION = Duration.millis(420);
    private static final Duration HOLD_DURATION = Duration.millis(700);
    private static final Duration FADE_OUT_DURATION = Duration.millis(520);
    private static final Duration SKIP_FADE_DURATION = Duration.millis(220);
    private static final double NAME_START_OFFSET = 14;

    private final Runnable onFinished;
    private SequentialTransition intro;
    private Scene boundScene;
    private boolean isFinishing;
    private boolean isSkipping;

    /**
     * Creates an overlay that fades the product name in, then reveals the chat.
     *
     * @param onFinished action to run after the overlay has faded out
     */
    public OnboardingOverlay(Runnable onFinished) {
        assert onFinished != null : "Onboarding must have a completion action";
        this.onFinished = onFinished;
        getStyleClass().add("onboarding-overlay");
        getChildren().add(createContent());
        setOnMouseClicked(event -> skip());
        setFocusTraversable(true);
        addEventFilter(KeyEvent.KEY_PRESSED, this::handleSkipKey);
    }

    /**
     * Starts the opening animation and takes keyboard focus for skip shortcuts.
     */
    public void play() {
        intro.playFromStart();
        attachSceneSkipHandler();
        Platform.runLater(this::requestFocus);
    }

    /**
     * Listens for skip keys on the scene so they work even if the overlay
     * has not yet received focus.
     */
    private void attachSceneSkipHandler() {
        if (getScene() != null) {
            bindScene(getScene());
            return;
        }
        sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                bindScene(newScene);
            }
        });
    }

    /**
     * Registers skip keys on the live scene.
     *
     * @param scene scene that currently contains this overlay
     */
    private void bindScene(Scene scene) {
        boundScene = scene;
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleSkipKey);
    }

    /**
     * Builds the centered name and underline used by the opening sequence.
     *
     * @return animated onboarding content
     */
    private VBox createContent() {
        Label name = new Label("Samantha");
        name.getStyleClass().add("onboarding-name");
        name.setOpacity(0);
        name.setTranslateY(NAME_START_OFFSET);

        Region line = new Region();
        line.getStyleClass().add("onboarding-line");
        line.setScaleX(0);

        VBox content = new VBox(14, name, line);
        content.setAlignment(Pos.CENTER);
        intro = new SequentialTransition(
                createNameReveal(name),
                createLineReveal(line),
                new PauseTransition(HOLD_DURATION),
                createFadeOut(FADE_OUT_DURATION));
        intro.setOnFinished(event -> finish());
        return content;
    }

    /**
     * Fades the product name in while it rises into place.
     *
     * @param name product-name label
     * @return name reveal animation
     */
    private ParallelTransition createNameReveal(Label name) {
        FadeTransition fade = new FadeTransition(NAME_DURATION, name);
        fade.setToValue(1);
        fade.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition rise = new TranslateTransition(NAME_DURATION, name);
        rise.setToY(0);
        rise.setInterpolator(Interpolator.EASE_OUT);
        return new ParallelTransition(fade, rise);
    }

    /**
     * Grows the underline from the center of the product name.
     *
     * @param line decorative underline
     * @return line reveal animation
     */
    private ScaleTransition createLineReveal(Region line) {
        ScaleTransition grow = new ScaleTransition(LINE_DURATION, line);
        grow.setFromX(0);
        grow.setToX(1);
        grow.setInterpolator(Interpolator.EASE_OUT);
        return grow;
    }

    /**
     * Fades the whole overlay away so the conversation can be used.
     *
     * @param duration fade-out length
     * @return overlay fade animation
     */
    private FadeTransition createFadeOut(Duration duration) {
        FadeTransition fadeOut = new FadeTransition(duration, this);
        fadeOut.setToValue(0);
        fadeOut.setInterpolator(Interpolator.EASE_IN);
        return fadeOut;
    }

    /**
     * Skips remaining frames when the user clicks or presses a dismiss key.
     *
     * @param event key event raised on the overlay
     */
    private void handleSkipKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER
                || event.getCode() == KeyCode.SPACE
                || event.getCode() == KeyCode.ESCAPE) {
            skip();
            event.consume();
        }
    }

    /**
     * Stops the opening sequence and fades the overlay out immediately.
     */
    private void skip() {
        if (isFinishing || isSkipping) {
            return;
        }
        isSkipping = true;
        if (intro != null) {
            intro.stop();
        }
        FadeTransition fadeOut = createFadeOut(SKIP_FADE_DURATION);
        fadeOut.setOnFinished(event -> finish());
        fadeOut.play();
    }

    /**
     * Runs the completion action once after the overlay has disappeared.
     */
    private void finish() {
        if (isFinishing) {
            return;
        }
        isFinishing = true;
        setMouseTransparent(true);
        if (boundScene != null) {
            boundScene.removeEventFilter(KeyEvent.KEY_PRESSED, this::handleSkipKey);
            boundScene = null;
        }
        onFinished.run();
    }
}
