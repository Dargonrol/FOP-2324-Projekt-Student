package projekt.view.menus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import projekt.Config;
import javafx.scene.media.Media;
import projekt.sound.BackgroundMusicPlayer;
import projekt.sound.SoundFXplayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A Builder to create the main menu.
 * The main menu has buttons to start a new game, open the settings, view the
 * highscores and open the about page.
 * The return button is used to quit the application.
 */
public class MainMenuBuilder extends MenuBuilder {
    private final Runnable loadGameScene;
    private final Runnable loadSettingsScene;
    private final Runnable loadHighscoreScene;
    private final Runnable loadAboutScene;

    /**
     * Creates a new MainMenuBuilder with the given handlers.
     *
     * @param quitHandler        The handler for the return button. Exits the
     *                           application.
     * @param createGameScene    The handler for the create game button. Opens the
     *                           game scene.
     * @param loadSettingsScene  The handler for the settings button. Opens the
     *                           settings scene.
     * @param loadHighscoreScene The handler for the highscores button. Opens the
     *                           highscores scene.
     * @param loadAboutScene     The handler for the about button. Opens the about
     *                           scene.
     */
    public MainMenuBuilder(
        final Runnable quitHandler, final Runnable createGameScene, final Runnable loadSettingsScene,
        final Runnable loadHighscoreScene, final Runnable loadAboutScene
    ) {
        //super("Main Menu", "Quit", quitHandler);
        super(true);
        this.loadGameScene = createGameScene;
        this.loadSettingsScene = loadSettingsScene;
        this.loadHighscoreScene = loadHighscoreScene;
        this.loadAboutScene = loadAboutScene;
    }

    @Override
    protected Node initCenter() {
        SoundFXplayer soundFXplayer = SoundFXplayer.getInstance();

        final VBox mainBox = new VBox();
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(0, 0, 0, 0));
        mainBox.setSpacing(10);
        if (getClass().getResource(Config.MENU_BACKGROUND_PATH) != null) {
            Image image = new Image(Objects.requireNonNull(getClass().getResource(Config.MENU_BACKGROUND_PATH)).toExternalForm());
            BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            root.setBackground(new Background(backgroundImage));
        }

        final Button startButton = new Button("Create Game");
        startButton.setOnAction(e -> {
            loadGameScene.run();
            soundFXplayer.playSound(getClass().getResource(Config.BUTTON_CLICK_MP3_PATH));
        });
        startButton.setOnMouseEntered(e -> {
            soundFXplayer.playSound(getClass().getResource(Config.HOVER_BUTTON_WAV_PATH));
            startButton.setTextFill(Color.GOLD);
        });
        startButton.setOnMouseExited(e -> startButton.setTextFill(Color.WHITE));

        final Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> {
            loadSettingsScene.run();
            soundFXplayer.playSound(getClass().getResource(Config.BUTTON_CLICK_MP3_PATH));
        });
        settingsButton.setOnMouseEntered(e -> {
            soundFXplayer.playSound(getClass().getResource(Config.HOVER_BUTTON_WAV_PATH));
            settingsButton.setTextFill(Color.GOLD);
        });
        settingsButton.setOnMouseExited(e -> settingsButton.setTextFill(Color.WHITE));

        final Button scoresButton = new Button("Highscores");
        scoresButton.setOnAction(e -> {
            loadHighscoreScene.run();
            soundFXplayer.playSound(getClass().getResource(Config.BUTTON_CLICK_MP3_PATH));
        });
        scoresButton.setOnMouseEntered(e -> {
            soundFXplayer.playSound(getClass().getResource(Config.HOVER_BUTTON_WAV_PATH));
            scoresButton.setTextFill(Color.GOLD);
        });
        scoresButton.setOnMouseExited(e -> scoresButton.setTextFill(Color.WHITE));

        final Button aboutButton = new Button("About");
        aboutButton.setOnAction(e -> {
            loadAboutScene.run();
            soundFXplayer.playSound(getClass().getResource(Config.BUTTON_CLICK_MP3_PATH));
        });
        aboutButton.setOnMouseEntered(e -> {
            soundFXplayer.playSound(getClass().getResource(Config.HOVER_BUTTON_WAV_PATH));
            aboutButton.setTextFill(Color.GOLD);
        });
        aboutButton.setOnMouseExited(e -> aboutButton.setTextFill(Color.WHITE));

        if (getClass().getResource(Config.MAIN_MENU_BUTTON_PATH) != null) {
            Image buttonBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Config.MAIN_MENU_BUTTON_PATH)));

            double buttonWidth = buttonBackground.getWidth() * 0.15;
            double buttonHeight = buttonBackground.getHeight() * 0.15;

            ImageView startImageView = new ImageView(buttonBackground);
            startButton.setGraphic(startImageView);
            startButton.contentDisplayProperty().set(ContentDisplay.CENTER);
            startImageView.fitWidthProperty().set(buttonWidth);
            startImageView.setPreserveRatio(true);
            startButton.setMaxSize(buttonWidth, buttonHeight);
            startButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 1.7em;");

            ImageView settingsImageView = new ImageView(buttonBackground);
            settingsButton.setGraphic(settingsImageView);
            settingsButton.contentDisplayProperty().set(ContentDisplay.CENTER);
            settingsImageView.fitWidthProperty().set(buttonWidth);
            settingsImageView.setPreserveRatio(true);
            settingsButton.setMaxSize(buttonWidth, buttonHeight);
            settingsButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 1.7em;");

            ImageView scoresImageView = new ImageView(buttonBackground);
            scoresButton.setGraphic(scoresImageView);
            scoresButton.contentDisplayProperty().set(ContentDisplay.CENTER);
            scoresImageView.fitWidthProperty().set(buttonWidth);
            scoresImageView.setPreserveRatio(true);
            scoresButton.setMaxSize(buttonWidth, buttonHeight);
            scoresButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 1.7em;");

            ImageView aboutImageView = new ImageView(buttonBackground);
            aboutButton.setGraphic(aboutImageView);
            aboutButton.contentDisplayProperty().set(ContentDisplay.CENTER);
            aboutImageView.fitWidthProperty().set(buttonWidth);
            aboutImageView.setPreserveRatio(true);
            aboutButton.setMaxSize(buttonWidth, buttonHeight);
            aboutButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 1.7em;");

            startButton.setTextFill(Color.WHITE);
            settingsButton.setTextFill(Color.WHITE);
            scoresButton.setTextFill(Color.WHITE);
            aboutButton.setTextFill(Color.WHITE);
        }

        mainBox.getChildren().addAll(startButton, settingsButton, scoresButton, aboutButton);

        return mainBox;
    }
    @Override
    protected Node initHeader() {
        if (getClass().getResource(Config.LOGO_PATH) == null) {
            return null;
        }
        ImageView imageView = new ImageView(String.valueOf(getClass().getResource(Config.LOGO_PATH)));
        HBox hBox = new HBox(imageView);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(25, 50, 0, 50));
        //hBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, null, new BorderWidths(1))));
        return hBox;
    }

    @Override
    protected Node initFooter(Runnable returnHandler) {
        final GridPane footer = new GridPane();
        footer.setAlignment(Pos.CENTER);
        footer.setHgap(10);
        footer.setVgap(10);
        return footer;
    }
}
