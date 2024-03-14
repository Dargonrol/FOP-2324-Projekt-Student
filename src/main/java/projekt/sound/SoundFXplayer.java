package projekt.sound;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundFXplayer {
    private static SoundFXplayer instance;
    private MediaPlayer mediaPlayer;
    private Media media;
    private double volume = 1;

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
        if (url != null) {
            System.out.println("Play sound file: " + url);
            this.media = new Media(url.toString());
            this.mediaPlayer = new MediaPlayer(media);
            this.mediaPlayer.setVolume(volume);
            this.mediaPlayer.play();
        } else {
            System.out.println("No sound file found");
        }
    }

    public void changeVolume(double volume) {
        this.mediaPlayer.setVolume(volume);
    }
}
