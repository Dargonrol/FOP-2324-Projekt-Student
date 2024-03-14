package projekt.view.menus;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import projekt.sound.BackgroundMusicPlayer;
import projekt.sound.SoundFXplayer;

public class SettingsMenuBuilder extends MenuBuilder {
    public final Runnable loadGameScene;
    public SettingsMenuBuilder(final Runnable loadGameScene) {
        super(true);
        this.loadGameScene = loadGameScene;
    }

    @Override
    protected Node initCenter() {

        VBox mainBox = new VBox();
        mainBox.setSpacing(10);
        HBox volumeSliderBox = new HBox();
        volumeSliderBox.setAlignment(Pos.CENTER);
        VBox volumeBox = new VBox();
        volumeBox.setAlignment(Pos.CENTER);
        volumeBox.setSpacing(5);
        Text volumeText = new Text("music volume");
        Text volumeValue = new Text();

        Slider volumeSlider = new Slider();
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(Math.ceil(BackgroundMusicPlayer.getInstance().getVolume() * 100));
        volumeValue.setText(String.valueOf(volumeSlider.getValue()));
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            BackgroundMusicPlayer.getInstance().changeVolume(newValue.doubleValue() / 100);
            volumeValue.setText(Math.ceil(newValue.doubleValue()) + "%");
        });
        volumeSliderBox.getChildren().addAll(volumeSlider, volumeValue);
        volumeBox.getChildren().addAll(volumeText, volumeSliderBox);

        VBox FXVolumeVBox = new VBox();
        FXVolumeVBox.setAlignment(Pos.CENTER);
        FXVolumeVBox.setSpacing(5);
        HBox FXVolumeSliderHBox = new HBox();
        FXVolumeSliderHBox.setAlignment(Pos.CENTER);
        Text FXVolumeText = new Text("FX volume");
        Text FXVolumeValue = new Text();
        Slider FXVolumeSlider = new Slider();
        FXVolumeSlider.setMin(0);
        FXVolumeSlider.setMax(100);
        FXVolumeSlider.setValue(Math.ceil(SoundFXplayer.getInstance().getVolume() * 100));
        FXVolumeValue.setText(String.valueOf(FXVolumeSlider.getValue()));
        FXVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SoundFXplayer.getInstance().changeVolume(newValue.doubleValue() / 100);
            FXVolumeValue.setText(Math.ceil(newValue.doubleValue()) + "%");
        });
        FXVolumeVBox.getChildren().addAll(FXVolumeText, FXVolumeSliderHBox);
        FXVolumeSliderHBox.getChildren().addAll(FXVolumeSlider, FXVolumeValue);

        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> loadGameScene.run());

        mainBox.getChildren().addAll(volumeBox, FXVolumeVBox, returnButton);
        mainBox.setAlignment(Pos.CENTER);


        return mainBox;
    }

    @Override
    protected Node initFooter() {
        return null;
    }

    @Override
    protected Node initHeader() {
        return null;
    }
}
