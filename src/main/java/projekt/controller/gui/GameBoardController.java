package projekt.controller.gui;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import projekt.Config;
import projekt.controller.PlayerController;
import projekt.model.GameState;
import projekt.model.Player;
import projekt.model.ResourceType;
import projekt.sound.BackgroundMusicPlayer;
import projekt.sound.SoundFXplayer;
import projekt.view.GameBoardBuilder;

import java.util.Map;

/**
 * The controller for the game board scene.
 */
@DoNotTouch
public class GameBoardController implements SceneController {
    private final GameState gameState;
    private final PlayerActionsController playerActionsController;
    private final HexGridController hexGridController;
    private final GameBoardBuilder gameBoardBuilder;

    /**
     * Creates a new game board controller.
     * Updates the player information on the game board when the active player
     * changes.
     * Updates the dice roll on the game board when the dice roll changes.
     * Shows an alert when a player wins.
     *
     * <b>Do not touch this constructor!</b>
     *
     * @param gameState                      the game state
     * @param activePlayerControllerProperty the active player controller property
     * @param diceRollProperty               the dice roll property
     * @param winnerProperty                 the winner property
     */
    @DoNotTouch
    public GameBoardController(
        final GameState gameState,
        final Property<PlayerController> activePlayerControllerProperty, final IntegerProperty diceRollProperty,
        final Property<Player> winnerProperty, final IntegerProperty roundCounterProperty
    ) {
        this.gameState = gameState;
        this.playerActionsController = new PlayerActionsController(
            this,
            activePlayerControllerProperty
        );
        this.hexGridController = new HexGridController(gameState.getGrid());
        this.gameBoardBuilder = new GameBoardBuilder(hexGridController.buildView(), playerActionsController::buildView);
        activePlayerControllerProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            Platform.runLater(() -> updatePlayerInformation(newValue.getPlayer(), Map.of()));
        });
        diceRollProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            Platform.runLater(() -> gameBoardBuilder.setDiceRoll(newValue.intValue()));
        });
        winnerProperty.subscribe((oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            Platform.runLater(() -> {
                SoundFXplayer.getInstance().playSound(getClass().getResource(Config.WINSOUND));
                new Alert(Alert.AlertType.INFORMATION, String.format("Player %s won!", newValue.getName()))
                    .showAndWait();
                BackgroundMusicPlayer.getInstance().fadeOut(4);
                BackgroundMusicPlayer.getInstance().changeMedia(getClass().getResource(Config.MAIN_MENU_MP3_PATH));
                BackgroundMusicPlayer.getInstance().fadeIn(1);
                SceneController.loadMainMenuScene();
            });
        });
        roundCounterProperty.subscribe((oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            Platform.runLater(() -> gameBoardBuilder.setRoundCounter(newValue.intValue()));
        });
    }

    /**
     * Returns the hex grid controller.
     *
     * @return the hex grid controller
     */
    public HexGridController getHexGridController() {
        return hexGridController;
    }

    /**
     * Updates the player information on the game board.
     *
     * @param player           the current player
     * @param changedResources the resources that have changed for the current
     *                         player
     */
    public void updatePlayerInformation(final Player player, final Map<ResourceType, Integer> changedResources) {
        Platform.runLater(
            () -> gameBoardBuilder.updatePlayerInformation(player, gameState.getPlayers(), changedResources));
    }

    @Override
    public String getTitle() {
        return "Catan";
    }

    @Override
    public Builder<Region> getBuilder() {
        return gameBoardBuilder;
    }

    @Override
    public Region buildView() {
        Region view = gameBoardBuilder.build();
        eventHandler(view);
        return view;
    }

    public void eventHandler(Region view) {
        view.setOnKeyPressed(keyEvent -> {
            KeyCodeCombination combination = new KeyCodeCombination(KeyCode.F10, KeyCombination.SHIFT_DOWN);
            if (combination.match(keyEvent)) {
                Config.debugModeProperty.set(!Config.debugModeProperty.get());
            }
            switch (keyEvent.getCode()) {
                case DIGIT1 -> this.playerActionsController.getBuildRoadButton().fire();
                case DIGIT2 -> this.playerActionsController.getBuildVillageButton().fire();
                case Q -> this.playerActionsController.getEndTurnButton().fire();
                case R -> this.playerActionsController.getRollDiceButton().fire();
                case T -> this.playerActionsController.getTradeButton().fire();
                case C, ESCAPE -> this.playerActionsController.getAbortButton().fire();
            }
        });
        view.requestFocus();
    }

    public PlayerActionsController getPlayerActionsController() {
        return playerActionsController;
    }
}
