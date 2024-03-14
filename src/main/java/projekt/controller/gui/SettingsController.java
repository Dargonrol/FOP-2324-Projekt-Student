package projekt.controller.gui;

import javafx.scene.layout.Region;
import javafx.util.Builder;
import projekt.view.menus.SettingsMenuBuilder;

/**
 * The controller for the settings scene.
 */
public class SettingsController implements SceneController {
    @Override
    public Builder<Region> getBuilder() {
        return new SettingsMenuBuilder(SceneController::loadMainMenuScene);
    }

    @Override
    public String getTitle() {
        return "Settings";
    }
}
