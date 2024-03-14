package projekt;

import javafx.application.Application;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import projekt.controller.GameController;
import projekt.controller.gui.SceneSwitcher;
import projekt.controller.gui.SceneSwitcher.SceneType;
import projekt.sound.BackgroundMusicPlayer;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

/**
 * The main application of the game.
 */
@DoNotTouch
public class MyApplication extends Application {
    private final Consumer<GameController> gameLoopStart = gc -> {
        final Thread gameLoopThread = new Thread(gc::startGame);
        gameLoopThread.setName("GameLoopThread");
        gameLoopThread.setDaemon(true);
        gameLoopThread.start();
    };

    @Override
    public void start(final Stage stage) throws Exception {
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(final int b) {
                System.out.write(b);
            }
        }));

        stage.setMinWidth(1000);
        stage.setMinHeight(520);
        stage.setWidth(1200);
        stage.setHeight(720);
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED);

        BackgroundMusicPlayer backgroundMusicPlayer = BackgroundMusicPlayer.getInstance();
        backgroundMusicPlayer.init(getClass().getResource(Config.MAIN_MENU_MP3_PATH));
        backgroundMusicPlayer.getMediaPlayer().setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusicPlayer.getMediaPlayer().play();



        SceneSwitcher.getInstance(stage, gameLoopStart).loadScene(SceneType.MAIN_MENU);
    }

    /**
     * The main method of the application.
     *
     * @param args The launch arguments of the application.
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
