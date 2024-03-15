package projekt.sound;


import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

public class BackgroundMusicPlayer {
    private static BackgroundMusicPlayer instance;
    private MediaPlayer mediaPlayer;
    private Media media;
    private DoubleProperty volume = new SimpleDoubleProperty(0); //0.35

    private BackgroundMusicPlayer() { }

    public static BackgroundMusicPlayer getInstance() {
        if (instance == null) {
            instance = new BackgroundMusicPlayer();
        }
        return instance;
    }

    public void init(URL url) {
        System.out.println("Initializing BackgroundMusicPlayer");
        try {
            if (url != null) {
                System.out.println("sound file found");;
                this.media = new Media(url.toString());
                this.mediaPlayer = new MediaPlayer(media);
                this.mediaPlayer.volumeProperty().bindBidirectional(this.volume);
            } else {
                this.mediaPlayer = null;
                System.out.println("No music file found");
            }
        } catch ( Exception ignored) {}
    }

    public MediaPlayer getMediaPlayer() {
        if (this.mediaPlayer != null)
            return mediaPlayer;
        return null;
    }

    public void changeMedia(URL url) {
        try {
            if (url != null) {
                System.out.println("Changing music file");
                this.media = new Media(url.toString());
                this.mediaPlayer = new MediaPlayer(media);
                this.mediaPlayer.setVolume(volume.get());
            } else {
                this.mediaPlayer = null;
                System.out.println("No music file found");
            }
        } catch ( Exception ignored) {}
    }

    public void changeVolume(double volume) {
        if (this.mediaPlayer != null)
            this.volume.set(volume);
    }

    public double getVolume() {
        if (this.mediaPlayer != null)
            return this.volume.get();
        return 0;
    }

    public void fadeOut(int durationInSeconds) {
        if (this.mediaPlayer != null) {
            MediaPlayer oldMediaPlayer = this.mediaPlayer;
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(durationInSeconds), new KeyValue(oldMediaPlayer.volumeProperty(), 0))
            );
            timeline.setOnFinished(event -> oldMediaPlayer.stop());
            timeline.play();
        }
    }

    public void fadeIn(int durationInSeconds) {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.setVolume(0);
            this.mediaPlayer.play();
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(durationInSeconds), new KeyValue(this.mediaPlayer.volumeProperty(), volume.doubleValue()))
            );
            timeline.play();
        }
    }
}
