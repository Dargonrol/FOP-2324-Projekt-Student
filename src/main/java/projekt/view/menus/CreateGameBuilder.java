package projekt.view.menus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.Config;
import projekt.model.PlayerImpl;
import projekt.model.PlayerImpl.Builder;
import projekt.sound.BackgroundMusicPlayer;
import projekt.sound.SoundFXplayer;

import java.util.function.Supplier;

/**
 * A Builder to create the create game view.
 * The create game view lets users add and remove players and start the game.
 * It is possible to give each player a name, a color and to select whether the
 * player is a bot or not.
 */
public class CreateGameBuilder extends MenuBuilder {
    private final ObservableList<PlayerImpl.Builder> observablePlayers;
    private final Supplier<Boolean> startGameHandler;

    /**
     * Creates a new CreateGameBuilder with the given players and handlers.
     *
     * @param players          The list of players to display and modify.
     * @param returnHandler    The handler to call when the user wants to return to
     *                         the main menu
     * @param startGameHandler The handler to call when the user wants to start the
     *                         game
     */
    @DoNotTouch
    public CreateGameBuilder(
        final ObservableList<PlayerImpl.Builder> players,
        final Runnable returnHandler,
        final Supplier<Boolean> startGameHandler
    ) {
        super("Start new Game", returnHandler);
        this.startGameHandler = startGameHandler;
        this.observablePlayers = players;
    }

    @Override
    protected Node initCenter() {
        final VBox mainBox = new VBox();
        mainBox.setStyle("-fx-font-size: 2em");
        // For icons see https://pictogrammers.com/library/mdi/
        final VBox playerListVBox = new VBox();
        this.observablePlayers.subscribe(() -> {
            playerListVBox.getChildren().clear();
            for (final PlayerImpl.Builder playerBuilder : this.observablePlayers) {
                final HBox playerListingHBox = new HBox();
                playerListingHBox.setAlignment(Pos.CENTER);
                final TextField playerNameTextField = new TextField(playerBuilder.nameOrDefault());
                playerNameTextField.setOnKeyPressed(e -> {
                    final String newName = playerNameTextField.getText();
                    if (newName.isBlank()) {
                        playerBuilder.name(null);
                        playerNameTextField.setText(playerBuilder.nameOrDefault());
                        playerNameTextField.selectAll();
                    } else {
                        playerBuilder.name(newName);
                    }
                });
                playerListingHBox.getChildren().addAll(
                    playerNameTextField
                );
                playerListVBox.getChildren().add(playerListingHBox);
            }
        });

        final Button startGameButton = new Button("Start Game");
        final Label startGameErrorLabel = new Label();
        startGameButton.setOnAction(e -> {
            if (!this.startGameHandler.get()) {
                startGameErrorLabel.setText("Cannot start game");
            } else {
                BackgroundMusicPlayer.getInstance().fadeOut(2);
                BackgroundMusicPlayer.getInstance().changeMedia(getClass().getResource(Config.GAME_LOOP_MP3_PATH));
                BackgroundMusicPlayer.getInstance().fadeIn(4);
                SoundFXplayer.getInstance().playSound(getClass().getResource(Config.GAMESTART_WAV_PATH));
            }
        });

        mainBox.getChildren().addAll(
            playerListVBox,
            startGameButton,
            startGameErrorLabel
        );
        mainBox.alignmentProperty().set(Pos.TOP_CENTER);
        return mainBox;
    }

    /**
     * Creates a button to add a new player to the game.
     * The button adds a new player to the list of players when clicked.
     *
     * @return a button to add a new player to the game
     */
    @StudentImplementationRequired("H3.4")
    private Node createAddPlayerButton() {
        Button button = new Button("Add Player");
        button.setOnAction(event -> observablePlayers.add(nextPlayerBuilder()));
        return button;
    }

    /**
     * Creates a color picker to select the color of the player.
     * Two players cannot have the same color.
     *
     * @param playerBuilder the builder for the player to create the color picker
     *                      for
     * @return a color picker to select the color of the player
     */
    @StudentImplementationRequired("H3.4")
    private Node createPlayerColorPicker(final Builder playerBuilder) {
        ColorPicker picker = new ColorPicker(playerBuilder.getColor());
        Alert alert = new Alert(Alert.AlertType.ERROR, "Another Player already picked this colour");

        picker.setOnAction(event -> {
            if(!observablePlayers.stream().map(Builder::getColor).filter(x -> x == picker.getValue()).toList().isEmpty()) { // another player already has the colour?
                alert.showAndWait();
            } else { // no-one already has the colour
                playerBuilder.color(picker.getValue());
            }
        });
        return picker;
    }

    /**
     * Creates a node to select whether the player is a bot or not.
     *
     * @param playerBuilder the builder for the player to create the selector for
     * @return a node to select whether the player is a bot or not
     */
    @StudentImplementationRequired("H3.4")
    private Node createBotOrPlayerSelector(final Builder playerBuilder) { // ✅
        // H3.4
        CheckBox checkBox = new CheckBox("Ai?");
        checkBox.setSelected(false);
        checkBox.setOnAction(event -> {
            if (!playerBuilder.isAi() && checkBox.isSelected()) {
                playerBuilder.aiProperty().set(true);
            } else {
                playerBuilder.aiProperty().set(false);
            }
        });
        return checkBox;
    }

    /**
     * Creates a button to remove the player with the given id.
     *
     * @param id the id of the player to remove
     * @return a button to remove the player with the given id
     */
    @StudentImplementationRequired("H3.4")
    private Button createRemovePlayerButton(final int id) {
        Button button = new Button("Remove Player");
        button.setOnAction(event -> removePlayer(id));
        return button;
    }

    /**
     * Removes the player with the given id and updates the ids of the remaining
     * players.
     *
     * @param id the id of the player to remove
     */
    @StudentImplementationRequired("H3.4")
    private void removePlayer(final int id) {
        this.observablePlayers.remove(this.observablePlayers.stream().map(Builder::getId).toList().indexOf(id));
    }

    /**
     * Returns a new {@link PlayerImpl.Builder} for the player with the next id.
     *
     * @return a new {@link PlayerImpl.Builder} for the player with the next id
     */
    public PlayerImpl.Builder nextPlayerBuilder() {
        return new PlayerImpl.Builder(this.observablePlayers.size() + 1);
    }

}
