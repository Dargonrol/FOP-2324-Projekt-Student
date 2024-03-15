package projekt.view.menus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.Config;
import projekt.model.PlayerImpl;
import projekt.model.PlayerImpl.Builder;
import projekt.sound.BackgroundMusicPlayer;
import projekt.sound.SoundFXplayer;
import projekt.view.DebugWindow;

import java.io.InputStream;
import java.util.Objects;
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
        super(true, returnHandler);
        this.startGameHandler = startGameHandler;
        this.observablePlayers = players;
    }

    @Override
    protected Node initCenter() {
        boolean buttonTextureExists = getClass().getResource(Config.MAIN_MENU_BUTTON_PATH) != null;

        final VBox mainBox = new VBox();
        mainBox.setStyle("-fx-font-size: 2em");
        if (getClass().getResource(Config.BACKGROUND2_PATH) != null) {
            Image image = new Image(Objects.requireNonNull(getClass().getResource(Config.BACKGROUND2_PATH)).toExternalForm());
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);
            root.setBackground(new Background(backgroundImage));
        }
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        // For icons see https://pictogrammers.com/library/mdi/
        final VBox playerListVBox = new VBox();
        this.observablePlayers.subscribe(() -> {
            playerListVBox.getChildren().clear();
            for (final PlayerImpl.Builder playerBuilder : this.observablePlayers) {
                final HBox playerListingHBox = new HBox();
                playerListingHBox.setAlignment(Pos.CENTER);
                final TextField playerNameTextField = new TextField(playerBuilder.nameOrDefault());
                if (getClass().getResource(Config.BACKGROUND_PATH) != null) {
                    Image image = new Image(Objects.requireNonNull(getClass().getResource(Config.BACKGROUND_PATH)).toExternalForm());
                    BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
                    BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                    playerNameTextField.setBackground(new Background(backgroundImage));
                    playerNameTextField.setStyle(" -fx-border-width: 4px; -fx-font-size: 1.7em; -fx-border-color: BLACK; -fx-text-fill: WHITE;");
                }
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
                    playerNameTextField,
                    createPlayerColorPicker(playerBuilder),
                    createRemovePlayerButton(playerBuilder.getId())
                );
                playerListVBox.getChildren().add(playerListingHBox);
            }
        });

        mainBox.getChildren().addAll(
            playerListVBox,
            createAddPlayerButton()
        );
        mainBox.alignmentProperty().set(Pos.TOP_CENTER);

        final Button startGameButton = new Button("Start Game");
        final Label startGameErrorLabel = new Label();
        VBox startGameButtonBox = new VBox();
        startGameButtonBox.setPadding(new Insets(10, 0, 1, 0));
        startGameButtonBox.setAlignment(Pos.CENTER);
        startGameButtonBox.getChildren().addAll(startGameButton, startGameErrorLabel);
        startGameButton.setOnAction(e -> {
            if (!this.startGameHandler.get()) {
                startGameErrorLabel.setText("Cannot start game");
            } else if (BackgroundMusicPlayer.getInstance().getMediaPlayer() != null) {
                SoundFXplayer.getInstance().playSound(getClass().getResource(Config.GAMESTART_WAV_PATH));
            }
        });
        startGameButton.setOnMouseEntered(e -> {
            SoundFXplayer.getInstance().playSound(getClass().getResource(Config.HOVER_BUTTON_WAV_PATH));
            if (buttonTextureExists) {
                startGameButton.setTextFill(Color.GOLD);
            } else {
                startGameButton.setTextFill(Color.AQUAMARINE);
            }
        });
        startGameButton.setOnMouseExited(e -> {
            if (buttonTextureExists) {
                startGameButton.setTextFill(Color.WHITE);
            } else {
                startGameButton.setTextFill(Color.BLACK);
            }
        });

        if (buttonTextureExists) {
            Image buttonBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Config.MAIN_MENU_BUTTON_PATH)));
            double buttonWidth = buttonBackground.getWidth() * Config.bigButtonSize;
            double buttonHeight = buttonBackground.getHeight() * Config.bigButtonSize;
            ImageView startImageView = new ImageView(buttonBackground);
            startGameButton.setGraphic(startImageView);
            startGameButton.contentDisplayProperty().set(ContentDisplay.CENTER);
            startImageView.fitWidthProperty().set(buttonWidth);
            startImageView.setPreserveRatio(true);
            startGameButton.setMaxSize(buttonWidth, buttonHeight);
            startGameButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 1.7em; -fx-border-color: GOLD;");
            startGameButton.setTextFill(Color.WHITE);
        }

        gridPane.add(mainBox, 0, 0);
        gridPane.add(startGameButtonBox, 0, 1);
        gridPane.setAlignment(Pos.CENTER);

        return gridPane;
    }

    @Override
    protected Node initFooter(Runnable returnHandler) {
        boolean buttonTextureExists = getClass().getResource(Config.MAIN_MENU_BUTTON_PATH) != null;

        Button cancelButton = new Button("Cancel");
        if (buttonTextureExists) {
            Image buttonBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Config.MAIN_MENU_BUTTON_PATH)));
            double buttonWidth = buttonBackground.getWidth() * Config.bigButtonSize;
            double buttonHeight = buttonBackground.getHeight() * Config.bigButtonSize;
            ImageView startImageView = new ImageView(buttonBackground);
            cancelButton.setGraphic(startImageView);
            cancelButton.contentDisplayProperty().set(ContentDisplay.CENTER);
            startImageView.fitWidthProperty().set(buttonWidth);
            startImageView.setPreserveRatio(true);
            cancelButton.setMaxSize(buttonWidth, buttonHeight);
            cancelButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 1.7em;");
            cancelButton.setTextFill(Color.WHITE);
        }

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.setPadding(new Insets(0, 0, 10, 10));
        buttonBox.getChildren().add(cancelButton);

        cancelButton.setOnAction(event -> {
            SoundFXplayer.getInstance().playSound(getClass().getResource(Config.BUTTON_CLICK_MP3_PATH));
            returnHandler.run();
        });
        cancelButton.setOnMouseEntered(e -> {
            SoundFXplayer.getInstance().playSound(getClass().getResource(Config.HOVER_BUTTON_WAV_PATH));
            if (buttonTextureExists) {
                cancelButton.setTextFill(Color.GOLD);
            } else {
                cancelButton.setTextFill(Color.AQUAMARINE);
            }
        });
        cancelButton.setOnMouseExited(e -> {
            if (buttonTextureExists) {
                cancelButton.setTextFill(Color.WHITE);
            } else {
                cancelButton.setTextFill(Color.BLACK);
            }
        });
        return buttonBox;
    }
    @Override
    protected Node initHeader() {
        Text createGameTitle = new Text("Create a new Game");
        InnerShadow is = new InnerShadow();
        is.setOffsetX(4.0f);
        is.setOffsetY(4.0f);
        if (getClass().getResource(Config.ANANDA_FONT_PATH) != null && (getClass().getResource("/css/CreateGameBuilder.css")) != null) {
            System.out.println("Font found");
            createGameTitle.getStyleClass().add("createGameTitle");
        } else {
            createGameTitle.setStyle("-fx-font-size: 8em");
            System.out.println("Font not found");
        }
        createGameTitle.setFill(Color.valueOf("#ffc72b"));
        createGameTitle.setEffect(is);
        HBox titleBox = new HBox();
        titleBox.setPadding(new Insets(40, 20, 0, 20));
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(createGameTitle);
        return titleBox;
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
        button.setOnAction(event -> {
            observablePlayers.add(nextPlayerBuilder());
        });
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Another Player already picked this colour");

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
        Button button = new Button("Remove");
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
