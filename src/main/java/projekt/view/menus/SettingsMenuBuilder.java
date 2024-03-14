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
        Text volumeText = new Text("music volume");
        Text volumeValue = new Text();

        Slider volumeSlider = new Slider();
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(50);
        volumeValue.setText(String.valueOf(volumeSlider.getValue()));
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            BackgroundMusicPlayer.getInstance().changeVolume(newValue.doubleValue() / 100);
            volumeValue.setText(Math.ceil(newValue.doubleValue()) + "%");
        });
        volumeSliderBox.getChildren().addAll(volumeSlider, volumeValue);
        volumeBox.getChildren().addAll(volumeText, volumeSliderBox);

        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> loadGameScene.run());

        mainBox.getChildren().addAll(volumeBox, returnButton);
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
