package projekt.sound;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundFXplayer {
    private static SoundFXplayer instance;
    private MediaPlayer mediaPlayer;
    private Media media;
    private DoubleProperty volume = new SimpleDoubleProperty(0.25); //0.25

    private SoundFXplayer() { }

    public static SoundFXplayer getInstance() {
        if (instance == null) {
            instance = new SoundFXplayer();
        }
        return instance;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void playSound(URL url) {
        playSound(url, false);
    }

    public void playSound(URL url, boolean stopPrevious) {
        if (stopPrevious && this.mediaPlayer != null) { this.mediaPlayer.stop(); }
        try {
            if (url != null) {
                this.media = new Media(url.toString());
                this.mediaPlayer = new MediaPlayer(media);
                this.mediaPlayer.volumeProperty().bindBidirectional(this.volume);
                this.mediaPlayer.play();
            } else {
                System.out.println("No sound file found");
            }
        } catch (Exception ignored) { }
    }

    public void changeVolume(double volume) {
        this.volume.set(volume);
    }

    public double getVolume() {
        return this.volume.get();
    }
}
