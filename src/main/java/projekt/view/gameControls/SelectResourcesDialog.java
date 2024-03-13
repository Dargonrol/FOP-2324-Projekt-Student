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
import javafx.scene.text.Text;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;
import projekt.Config;
import projekt.model.Player;
import projekt.model.ResourceType;

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

    // OWN MODIFICATIONS TO MAKE MY DESIGN WORK!!!!!!! All class variables where not there before.
    BooleanBinding AllResourcesSelected;
    IntegerProperty totalSelectedResources;
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
        dialogPane.getButtonTypes().add(ButtonType.OK);
        dialogPane.setContent(init(amountToSelect, player, resourcesToSelectFrom, dropCards));
    }

    @StudentImplementationRequired("H3.4")
    private Region init(
        final int amountToSelect, final Player player,
        Map<ResourceType, Integer> resourcesToSelectFrom, final boolean dropCards
    ) {
        // TODO: H3.4
        // if resourcesToSelectFrom is empty, you can select from every resource.
        if (resourcesToSelectFrom.isEmpty()) {
            resourcesToSelectFrom.put(ResourceType.CLAY, 1);
            resourcesToSelectFrom.put(ResourceType.ORE, 1);
            resourcesToSelectFrom.put(ResourceType.GRAIN, 1);
            resourcesToSelectFrom.put(ResourceType.WOOD, 1);
            resourcesToSelectFrom.put(ResourceType.WOOL, 1);
        }

        GridPane gridPane = new GridPane();
        GridPane resourceSelectGrid = new GridPane();

        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

        Text title = new Text("select a total of " + amountToSelect + " resources below");
        title.setEffect(ds);
        title.setCache(true);
        title.setFill(Color.DARKRED);
        title.setFont(Font.font(Font.getDefault().getName(), FontPosture.ITALIC, 20));

        ArrayList<Spinner<Integer>> spinners = new ArrayList<>();
        totalSelectedResources = new SimpleIntegerProperty(0);
        MapProperty<ResourceType, Integer> resultResources = new SimpleMapProperty<>(FXCollections.observableHashMap());

        ChangeListener<Integer> spinnerChangeListener = ((observable, oldValue, newValue) -> {
            int sum = 0;
            int index = 0;
            ArrayList<ResourceType> resourceTypes = new ArrayList<>();
            resourceTypes.add(ResourceType.CLAY);
            resourceTypes.add(ResourceType.ORE);
            resourceTypes.add(ResourceType.GRAIN);
            resourceTypes.add(ResourceType.WOOD);
            resourceTypes.add(ResourceType.WOOL);
            for (Spinner<Integer> spinner: spinners) {
                resultResources.put(resourceTypes.get(index), spinner.getValue());
                sum += spinner.getValue();
                index++;
            }
            totalSelectedResources.setValue(sum);
        });
        ArrayList<StackPane> stackPanes = new ArrayList<>();
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        ArrayList<Text> resourceTexts = new ArrayList<>();
        resourceTexts.add(new Text("clay"));
        resourceTexts.add(new Text("ore"));
        resourceTexts.add(new Text("grain"));
        resourceTexts.add(new Text("wood"));
        resourceTexts.add(new Text("wool"));

        for (int i = 0; i < Config.NUMBER_TYPES_RESOURCES; i++) {
            spinners.add(new Spinner<>(0, amountToSelect, 0));
            spinners.get(i).editableProperty().setValue(true);
            //spinners.get(i).setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, amountToSelect, 0, 1));
            //spinners.get(i).getValueFactory().wrapAroundProperty().setValue(true);
            spinners.get(i).valueProperty().addListener(spinnerChangeListener);

            stackPanes.add(new StackPane());

            rectangles.add(new Rectangle(50, 50, Color.rgb(50, 168, 84)));
            rectangles.get(i).setStroke(Color.BLACK);
            rectangles.get(i).setStrokeWidth(1);
            resourceTexts.get(i).setFont(Font.font(Font.getDefault().getName(), FontPosture.ITALIC, 20));
            resourceTexts.get(i).setFill(Color.rgb(168, 86, 50));

            stackPanes.get(i).getChildren().addAll(rectangles.get(i), resourceTexts.get(i));
            resourceSelectGrid.add(stackPanes.get(i), i, 0);
            resourceSelectGrid.add(spinners.get(i), i, 1);

        }

        gridPane.add(title, 0, 0);
        gridPane.add(resourceSelectGrid, 0, 1);

        // TODO: Wie viele Ressourcen müssen noch ausgewählt werden?
        // TODO: ausgrauen der resources, welche nicht ausgewählt werden können

        AllResourcesSelected = Bindings.lessThan(totalSelectedResources, amountToSelect);

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(AllResourcesSelected);

        okButton.setBackground(Background.fill(Color.DARKGRAY));
        okButton.setTextFill(Color.WHITE);
        okButton.disableProperty().bind(AllResourcesSelected);
        okButton.setOnAction(event -> {
            setResult(resultResources.getValue());
        });

        return gridPane;
    }
}
