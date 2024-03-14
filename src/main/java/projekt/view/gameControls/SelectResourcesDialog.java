package projekt.view.gameControls;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.Config;
import projekt.model.Player;
import projekt.model.ResourceType;
import projekt.view.ResourceCardPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A dialog to prompt the user to select a number of resources.
 * The dialog shows the resources the player can choose from and lets the user
 * select a number of each resource.
 * If dropCards is true, the user is prompted to drop cards instead of selecting
 * them.
 * The result of the dialog is a map of the selected resources and their
 * amounts.
 */
public class SelectResourcesDialog extends Dialog<Map<ResourceType, Integer>> {
    final DialogPane dialogPane;

    /**
     * Creates a new SelectResourcesDialog for the given player and resources.
     *
     * @param amountToSelect        The amount of resources to select.
     * @param player                The player that is prompted to select resources.
     * @param resourcesToSelectFrom The resources the player can select from. If
     *                              null the player can select any resource.
     * @param dropCards             Whether the player should drop cards instead of
     *                              selecting them.
     */
    public SelectResourcesDialog(
        final int amountToSelect, final Player player,
        final Map<ResourceType, Integer> resourcesToSelectFrom, final boolean dropCards
    ) {
        this.dialogPane = getDialogPane(); // modded -alex
        //dialogPane.getButtonTypes().add(ButtonType.OK); // needed to remove cause I wanted to implement my own Style
        dialogPane.setContent(init(amountToSelect, player, resourcesToSelectFrom, dropCards));
    }

    @StudentImplementationRequired("H3.4")
    private Region init( // ✅
        final int amountToSelect, final Player player,
        Map<ResourceType, Integer> resourcesToSelectFrom, final boolean dropCards
    ) {
        // H3.4
        ArrayList<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(ResourceType.CLAY);
        resourceTypes.add(ResourceType.ORE);
        resourceTypes.add(ResourceType.GRAIN);
        resourceTypes.add(ResourceType.WOOD);
        resourceTypes.add(ResourceType.WOOL);

        Map<ResourceType, Integer> modifiableResourcesToSelectFrom = new HashMap<>(resourcesToSelectFrom);
        modifiableResourcesToSelectFrom.putAll(resourcesToSelectFrom);
        for (ResourceType resourceType: resourceTypes) {
            if (!modifiableResourcesToSelectFrom.containsKey(resourceType)) {
                modifiableResourcesToSelectFrom.put(resourceType, 0);
            }
        }

        // base structure of window
        GridPane gridPane = new GridPane();
        GridPane resourceSelectGrid = new GridPane();

        // drop shadow for various elements
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

        // adding title Prompt
        HBox hBoxTitle = new HBox();
        hBoxTitle.setAlignment(Pos.CENTER);

        Text playerName = new Text(player.getName());
        playerName.setEffect(ds);
        playerName.setCache(true);
        playerName.setFill(Color.BLACK);
        playerName.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, 20));

        Text titlePrompt = new Text(" Select " + amountToSelect + " resources below");
        titlePrompt.setEffect(ds);
        titlePrompt.setCache(true);
        titlePrompt.setFill(Color.BLACK);
        titlePrompt.setFont(Font.font(Font.getDefault().getName(), FontPosture.REGULAR, 20));

        hBoxTitle.getChildren().addAll(playerName, titlePrompt);
        gridPane.add(hBoxTitle, 0, 0);
        // added title Prompt

        if (dropCards) {
            Text dropCardText = new Text("cards will be dropped");
            dropCardText.setCache(true);
            dropCardText.setFill(Color.DARKRED);
            dropCardText.setFont(Font.font(Font.getDefault().getName(), FontPosture.ITALIC, 20));
            HBox hBoxDropCards = new HBox();
            hBoxDropCards.setAlignment(Pos.CENTER);
            hBoxDropCards.getChildren().add(dropCardText);
            gridPane.add(hBoxDropCards, 0, 1);
        }

        Text remainingCardsText = new Text("cards remaining: " + amountToSelect);
        remainingCardsText.setCache(true);
        remainingCardsText.setFill(Color.DARKRED);
        remainingCardsText.setFont(Font.font(Font.getDefault().getName(), FontWeight.SEMI_BOLD, FontPosture.ITALIC, 15));


        Map<ResourceType, Spinner<Integer>> spinnersMap = new HashMap<>();
        IntegerProperty totalSelectedResources = new SimpleIntegerProperty(0);
        MapProperty<ResourceType, Integer> resultResources = new SimpleMapProperty<>(FXCollections.observableHashMap());

        ChangeListener<Integer> spinnerChangeListener = ((observable, oldValue, newValue) -> {
            int sum = 0;
            for (Map.Entry<ResourceType, Spinner<Integer>> entry: spinnersMap.entrySet()) {
                resultResources.put(entry.getKey(), entry.getValue().getValue());
                sum += entry.getValue().getValue();
            }
            for (Map.Entry<ResourceType, Spinner<Integer>> entry: spinnersMap.entrySet()) {
                int newSpinnerValue = (amountToSelect - sum) + entry.getValue().getValue();
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    0,
                    newSpinnerValue <= modifiableResourcesToSelectFrom.get(entry.getKey()) ? newSpinnerValue : modifiableResourcesToSelectFrom.get(entry.getKey()),
                    entry.getValue().getValue()
                );
                entry.getValue().setValueFactory(valueFactory);
            }
            totalSelectedResources.setValue(sum);
            remainingCardsText.setText("cards remaining: " + (amountToSelect - sum));
            remainingCardsText.setFill(sum == amountToSelect ? Color.GREEN : Color.DARKRED);
        });


        for (Map.Entry<ResourceType, Integer> entry: modifiableResourcesToSelectFrom.entrySet()) {
            Spinner<Integer> spinner = new Spinner<>(0, entry.getValue(), 0);
            spinner.setEditable(false);
            spinner.valueProperty().addListener(spinnerChangeListener);
            spinner.setStyle("-fx-font-weight: BOLD;");
            spinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
            if (entry.getValue() == 0) { spinner.setDisable(true); }
            spinnersMap.put(entry.getKey(), spinner);

            ResourceCardPane resourceCardPane = new ResourceCardPane(entry.getKey(), entry.getValue());
            resourceCardPane.setEffect(ds);
            resourceSelectGrid.add(resourceCardPane, entry.getKey().ordinal(), 0);
            resourceSelectGrid.add(spinner, entry.getKey().ordinal(), 1);
        }

        resourceSelectGrid.setHgap(10); // Set horizontal gap
        resourceSelectGrid.setVgap(10); // Set vertical gap
        gridPane.add(resourceSelectGrid, 0, 2);


        // Button logic and dialog modifications

        BooleanBinding AllResourcesSelected = Bindings.notEqual(totalSelectedResources, amountToSelect);

        Button okButton = new Button();
        okButton.disableProperty().bind(AllResourcesSelected);

        okButton.setBackground(Background.fill(Color.rgb(74, 74, 74)));
        okButton.setTextFill(Color.WHITE);
        okButton.disableProperty().bind(AllResourcesSelected);
        okButton.setText("confirm");
        okButton.setOnAction(event -> {
            setResult(resultResources.getValue());
            this.close();
        });

        HBox hBoxFooter = new HBox();
        hBoxFooter.setAlignment(Pos.CENTER);
        hBoxFooter.getChildren().addAll(remainingCardsText, okButton);
        hBoxFooter.setSpacing(10);
        gridPane.add(hBoxFooter, 0, 3);
        gridPane.setVgap(10);

        this.initStyle(StageStyle.UNDECORATED);
        dialogPane.setStyle("-fx-border-color: black; -fx-border-width: 4px; -fx-background-color: #C19A6B; -fx-padding: 2px;");
        return gridPane;
    }
}
