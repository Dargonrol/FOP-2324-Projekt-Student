package projekt.view;

import javafx.application.Application;
import javafx.beans.property.MapProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import projekt.Config;
import projekt.controller.GameController;
import projekt.controller.PlayerController;
import projekt.controller.gui.GameBoardController;
import projekt.model.Player;
import projekt.model.ResourceType;

import java.util.List;

public class DebugWindow extends Application {
    private static DebugWindow instance;
    private DebugWindow() {}
    private Stage primaryStage;

    private GameController gameController;
    private MapProperty<Player, PlayerController> playersProperty = new SimpleMapProperty<>();
    private PlayerController selectedPlayerController;
    private GameBoardController gameBoardController;

    public static DebugWindow getInstance() {
        if (instance == null) {
            instance = new DebugWindow();
        }
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        primaryStage.setResizable(true);
        primaryStage.alwaysOnTopProperty();
        primaryStage.setTitle("Debug Window");
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(250);

        Config.debugModeProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.show();
            } else {
                primaryStage.hide();
            }
        });


        Scene scene = new Scene(buildUI(), 250, 500);
        primaryStage.setScene(scene);
    }

    public void updatePlayers() {
        this.playersProperty.setValue(this.gameController.getObservablePlayerControllers());
    }

    private Parent buildUI() {
        GridPane mainGrid = new GridPane();
        mainGrid.add(buildPlayerSelectComboBox(), 0, 0);
        mainGrid.add(buildDebugSettingsBox(), 0, 1);
        return mainGrid;
    }

    private Node buildDebugSettingsBox() {
        VBox debugSettingsBox = new VBox();
        debugSettingsBox.setSpacing(10);
        debugSettingsBox.setAlignment(Pos.CENTER);

        Button addClayButton = new Button("+1 Clay");
        Button addWoodButton = new Button("+1 Wood");
        Button addOreButton = new Button("+1 Ore");
        Button addGrainButton = new Button("+1 Grain");
        Button addWoolButton = new Button("+1 Wool");
        Button removeClayButton = new Button("-1 Clay");
        Button removedWoodButton = new Button("-1 Wood");
        Button removeOreButton = new Button("-1 Ore");
        Button removeGrainButton = new Button("-1 Grain");
        Button removeWoolButton = new Button("-1 Wool");
        GridPane addRemoveResourcesGrid = new GridPane();
        addRemoveResourcesGrid.add(addClayButton, 1, 0);
        addRemoveResourcesGrid.add(removeClayButton, 0, 0);
        addRemoveResourcesGrid.add(addWoodButton, 1, 1);
        addRemoveResourcesGrid.add(removedWoodButton, 0, 1);
        addRemoveResourcesGrid.add(addOreButton, 1, 2);
        addRemoveResourcesGrid.add(removeOreButton, 0, 2);
        addRemoveResourcesGrid.add(addGrainButton, 1, 3);
        addRemoveResourcesGrid.add(removeGrainButton, 0, 3);
        addRemoveResourcesGrid.add(addWoolButton, 1, 4);
        addRemoveResourcesGrid.add(removeWoolButton, 0, 4);
        debugSettingsBox.getChildren().add(addRemoveResourcesGrid);

        addClayButton.setOnAction(event -> {
            if (this.selectedPlayerController != null) { this.selectedPlayerController.getPlayer().addResource(ResourceType.CLAY, 1); System.out.println("gave " + this.selectedPlayerController.getPlayer().getName() + " 1 Clay"); }
            this.gameBoardController.getPlayerActionsController().updateUIBasedOnObjective(this.gameController.getActivePlayerController().getPlayerObjectiveProperty().getValue());
        });
        addWoodButton.setOnAction(event -> {
            if (this.selectedPlayerController != null) { this.selectedPlayerController.getPlayer().addResource(ResourceType.WOOD, 1); System.out.println("gave " + this.selectedPlayerController.getPlayer().getName() + " 1 Wood"); }
            this.gameBoardController.getPlayerActionsController().updateUIBasedOnObjective(this.gameController.getActivePlayerController().getPlayerObjectiveProperty().getValue());
        });
        addOreButton.setOnAction(event -> {
            if (this.selectedPlayerController != null) { this.selectedPlayerController.getPlayer().addResource(ResourceType.ORE, 1); System.out.println("gave " + this.selectedPlayerController.getPlayer().getName() + " 1 Ore"); }
            this.gameBoardController.getPlayerActionsController().updateUIBasedOnObjective(this.gameController.getActivePlayerController().getPlayerObjectiveProperty().getValue());
        });
        addGrainButton.setOnAction(event -> {
            if (this.selectedPlayerController != null) { this.selectedPlayerController.getPlayer().addResource(ResourceType.GRAIN, 1); System.out.println("gave " + this.selectedPlayerController.getPlayer().getName() + " 1 Grain"); }
            this.gameBoardController.getPlayerActionsController().updateUIBasedOnObjective(this.gameController.getActivePlayerController().getPlayerObjectiveProperty().getValue());
        });
        addWoolButton.setOnAction(event -> {
            if (this.selectedPlayerController != null) { this.selectedPlayerController.getPlayer().addResource(ResourceType.WOOL, 1); System.out.println("gave " + this.selectedPlayerController.getPlayer().getName() + " 1 Wool"); }
            this.gameBoardController.getPlayerActionsController().updateUIBasedOnObjective(this.gameController.getActivePlayerController().getPlayerObjectiveProperty().getValue());
        });

        removeClayButton.setOnAction(event -> {
            if (this.selectedPlayerController != null) { this.selectedPlayerController.getPlayer().removeResource(ResourceType.CLAY, 1); System.out.println("took 1 Clay from " + this.selectedPlayerController.getPlayer().getName()); }
            this.gameBoardController.getPlayerActionsController().updateUIBasedOnObjective(this.gameController.getActivePlayerController().getPlayerObjectiveProperty().getValue());
        });
        removedWoodButton.setOnAction(event -> {
            if (this.selectedPlayerController != null) { this.selectedPlayerController.getPlayer().removeResource(ResourceType.WOOD, 1); System.out.println("took 1 Wood from " + this.selectedPlayerController.getPlayer().getName()); }
            this.gameBoardController.getPlayerActionsController().updateUIBasedOnObjective(this.gameController.getActivePlayerController().getPlayerObjectiveProperty().getValue());
        });
        removeOreButton.setOnAction(event -> {
            if (this.selectedPlayerController != null) { this.selectedPlayerController.getPlayer().removeResource(ResourceType.ORE, 1); System.out.println("took 1 Ore from " + this.selectedPlayerController.getPlayer().getName()); }
            this.gameBoardController.getPlayerActionsController().updateUIBasedOnObjective(this.gameController.getActivePlayerController().getPlayerObjectiveProperty().getValue());
        });
        removeGrainButton.setOnAction(event -> {
            if (this.selectedPlayerController != null) { this.selectedPlayerController.getPlayer().removeResource(ResourceType.GRAIN, 1); System.out.println("took 1 Grain from " + this.selectedPlayerController.getPlayer().getName()); }
            this.gameBoardController.getPlayerActionsController().updateUIBasedOnObjective(this.gameController.getActivePlayerController().getPlayerObjectiveProperty().getValue());
        });
        removeWoolButton.setOnAction(event -> {
            if (this.selectedPlayerController != null) { this.selectedPlayerController.getPlayer().removeResource(ResourceType.WOOL, 1); System.out.println("took 1 Wool from " + this.selectedPlayerController.getPlayer().getName()); }
            this.gameBoardController.getPlayerActionsController().updateUIBasedOnObjective(this.gameController.getActivePlayerController().getPlayerObjectiveProperty().getValue());
        });

        CheckBox activateDiceRollSevenCheckBox = new CheckBox("activate dice roll seven");
        activateDiceRollSevenCheckBox.setSelected(Config.activateDiceRollSeven);
        activateDiceRollSevenCheckBox.setOnAction(e -> {
            Config.activateDiceRollSeven = activateDiceRollSevenCheckBox.isSelected();
            System.out.println("DEBUG: activateDiceRollSeven: " + Config.activateDiceRollSeven);
        });
        debugSettingsBox.getChildren().add(activateDiceRollSevenCheckBox);

        return debugSettingsBox;
    }
    private Node buildPlayerSelectComboBox() {
        Text playerSelectText = new Text("Select Player");
        playerSelectText.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, 20));
        Text selectedPlayerText = new Text("Selected Player: ");

        ComboBox<PlayerController> playerSelectComboBox = new ComboBox<>();

        this.playersProperty.addListener((observable, oldValue, newValue) -> {
            playerSelectComboBox.getItems().clear();
            newValue.forEach((player, controller) -> playerSelectComboBox.getItems().add(this.playersProperty.getValue().get(player)));
        });

        playerSelectComboBox.setOnAction(event -> {
            this.selectedPlayerController = this.gameController.getPlayerControllers().get(playerSelectComboBox.getValue().getPlayer());
            System.out.println("DEBUG: selected player: "+ this.gameController.getPlayerControllers().get(playerSelectComboBox.getValue().getPlayer()).getPlayer().getName());
            selectedPlayerText.setText("Selected Player: " + this.selectedPlayerController.getPlayer().getName());
        });

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(playerSelectText, playerSelectComboBox, selectedPlayerText);
        return vBox;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
    }

    public void close() {
        this.primaryStage.close();
    }
}
