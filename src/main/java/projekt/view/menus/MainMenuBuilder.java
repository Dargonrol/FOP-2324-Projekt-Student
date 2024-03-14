package projekt.view.menus;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import projekt.Config;
import javafx.scene.media.Media;
import projekt.sound.BackgroundMusicPlayer;
import projekt.sound.SoundFXplayer;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

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
        super("Main Menu", "Quit", quitHandler);
        this.loadGameScene = createGameScene;
        this.loadSettingsScene = loadSettingsScene;
        this.loadHighscoreScene = loadHighscoreScene;
        this.loadAboutScene = loadAboutScene;
    }

    @Override
    protected Node initCenter() {
        BackgroundMusicPlayer backgroundMusicPlayer = BackgroundMusicPlayer.getInstance();
        backgroundMusicPlayer.init(getClass().getResource(Config.MAIN_MENU_MP3_PATH));
        backgroundMusicPlayer.getMediaPlayer().setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusicPlayer.getMediaPlayer().play();
        SoundFXplayer soundFXplayer = SoundFXplayer.getInstance();

        final VBox mainBox = new VBox();
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(10);

        final Button startButton = new Button("Create Game");
        startButton.setOnAction(e -> {
            loadGameScene.run();
            soundFXplayer.playSound(getClass().getResource(Config.BUTTON_CLICK_MP3_PATH));
        });
        startButton.setOnMouseEntered(e -> soundFXplayer.playSound(getClass().getResource(Config.HOVER_BUTTON_WAV_PATH)));

        final Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> {
            loadSettingsScene.run();
            soundFXplayer.playSound(getClass().getResource(Config.BUTTON_CLICK_MP3_PATH));
        });
        settingsButton.setOnMouseEntered(e -> soundFXplayer.playSound(getClass().getResource(Config.HOVER_BUTTON_WAV_PATH)));

        final Button scoresButton = new Button("Highscores");
        scoresButton.setOnAction(e -> {
            loadHighscoreScene.run();
            soundFXplayer.playSound(getClass().getResource(Config.BUTTON_CLICK_MP3_PATH));
        });
        scoresButton.setOnMouseEntered(e -> soundFXplayer.playSound(getClass().getResource(Config.HOVER_BUTTON_WAV_PATH)));

        final Button aboutButton = new Button("About");
        aboutButton.setOnAction(e -> {
            loadAboutScene.run();
            soundFXplayer.playSound(getClass().getResource(Config.BUTTON_CLICK_MP3_PATH));
        });
        aboutButton.setOnMouseEntered(e -> soundFXplayer.playSound(getClass().getResource(Config.HOVER_BUTTON_WAV_PATH)));

        mainBox.getChildren().addAll(startButton, settingsButton, scoresButton, aboutButton);

        return mainBox;
    }
}
