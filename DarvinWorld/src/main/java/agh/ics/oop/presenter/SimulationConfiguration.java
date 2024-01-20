package agh.ics.oop.presenter;

import agh.ics.oop.model.AnimalProperties;
import agh.ics.oop.model.MapProperties;
import agh.ics.oop.model.SimulationProperties;
import agh.ics.oop.model.Vector2d;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimulationConfiguration extends Application {

    private ConfigurationElement mapHeight;
    private ConfigurationElement mapWidth;
    private ConfigurationElement tunnelCount;
    private ConfigurationElement startPlantCount;
    private ConfigurationElement plantsPerDay;
    private ConfigurationElement energyPlant;
    private ConfigurationElement startAnimalCount;
    private ConfigurationElement startAnimalEnergy;
    private ConfigurationElement minProcreateEnergy;
    private ConfigurationElement procreateEnergy;
    private ConfigurationElement minMutation;
    private ConfigurationElement maxMutation;
    private ConfigurationElement genomeLength;

    private CheckBox chooseMapWithTunnelsBox;
    private CheckBox chooseWithLightMutationCorrectBox;
    private CheckBox saveStatisticsCheckBox;
    private ArrayList<ConfigurationElement> configurationElementArrayList;

    private SimulationStart simulationStart;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.show();

        primaryStage.setTitle("Darwin World");

        simulationStart = new SimulationStart();
        configurationElementArrayList = new ArrayList<>();

//        All texts needed:
        mapHeight = new ConfigurationElement(
                new Text("Choose map height: "),
                new TextField("20"),
                new Vector2d(10, 1000)
                );

        mapWidth = new ConfigurationElement(
                new Text("Choose map width: "),
                new TextField("20"),
                new Vector2d(10, 1000)
        );

        tunnelCount = new ConfigurationElement(
                new Text("Choose percent of tunnels: "),
                new TextField("10"),
                new Vector2d(1, 40)
        );

        startPlantCount = new ConfigurationElement(
                new Text("Choose initial percent of plants: "),
                new TextField("10"),
                new Vector2d(1, 30)
        );

        plantsPerDay = new ConfigurationElement(
                new Text("Choose daily number(?percent?) of new plants: "),
                new TextField("10"),
                new Vector2d(5, 60)
        );

        energyPlant = new ConfigurationElement(
                new Text("Choose energy from eating one plant: "),
                new TextField("2"),
                new Vector2d(1, 5)
        );

        startAnimalCount = new ConfigurationElement(
                new Text("Choose initial number of animals: "),
                new TextField("20"),
                new Vector2d(10, 50)
        );

        startAnimalEnergy = new ConfigurationElement(
                new Text("Choose initial energy of animals: "),
                new TextField("10"),
                new Vector2d(3, 100)
        );

        minProcreateEnergy = new ConfigurationElement(
                new Text("Choose minimal energy to procreate: "),
                new TextField("10"),
                new Vector2d(10, 20)
        );

        procreateEnergy = new ConfigurationElement(
                new Text("Choose energy used by procreating: "),
                new TextField("5"),
                new Vector2d(2, 10)
        );

        minMutation = new ConfigurationElement(
                new Text("Choose minimal number of mutations: "),
                new TextField("0"),
                new Vector2d(0, 10)
        );

        maxMutation = new ConfigurationElement(
                new Text("Choose maximal number of mutations: "),
                new TextField("11"),
                new Vector2d(0, 80)
        );

        genomeLength = new ConfigurationElement(
                new Text("Choose genome length: "),
                new TextField("8"),
                new Vector2d(2, 100)
        );

        this.configurationElementArrayList.add(mapHeight);
        this.configurationElementArrayList.add(mapWidth);
        this.configurationElementArrayList.add(tunnelCount);
        this.configurationElementArrayList.add(startPlantCount);
        this.configurationElementArrayList.add(plantsPerDay);
        this.configurationElementArrayList.add(energyPlant);
        this.configurationElementArrayList.add(startAnimalCount);
        this.configurationElementArrayList.add(startAnimalEnergy);
        this.configurationElementArrayList.add(minProcreateEnergy);
        this.configurationElementArrayList.add(procreateEnergy);
        this.configurationElementArrayList.add(minMutation);
        this.configurationElementArrayList.add(maxMutation);
        this.configurationElementArrayList.add(genomeLength);

////

//      Potrzebne CheckBoxy:

//        zapisywanie statystyk uruchomionej symulacji:
        saveStatisticsCheckBox = new CheckBox("Save daily statistics");
//        wybór mapy z tunelami:
        chooseMapWithTunnelsBox = new CheckBox("Tunnels");
        tunnelCount.textField().setDisable(!chooseMapWithTunnelsBox.isSelected());
//        wybór lekkiej korekty
        chooseWithLightMutationCorrectBox = new CheckBox("Light Mutation Correct");

//        Potrzebne przyciski:
        Button saveConfigurationButton = new Button("Save configuration");
        saveConfigurationButton.setOnAction(e -> saveConfiguration());
        Button chooseConfigurationButton = new Button("Choose configuration");
        chooseConfigurationButton.setOnAction(e -> chooseSavedConfiguration());

        Button startSimulationButton = new Button("Start simulation");
        startSimulationButton.setOnAction(e -> onSimulationStartClicked());


        HBox bottomMenu = new HBox();
        bottomMenu.getChildren().addAll(saveConfigurationButton, chooseConfigurationButton, startSimulationButton);
        bottomMenu.setAlignment(Pos.CENTER);

        VBox leftMenu = new VBox();

        for (ConfigurationElement configurationElement : configurationElementArrayList) {
            leftMenu.getChildren().addAll(
                    configurationElement.text(),
                    configurationElement.textField());
        }

        leftMenu.setAlignment(Pos.TOP_LEFT);


        VBox rightMenu = new VBox();
        rightMenu.getChildren().addAll(chooseMapWithTunnelsBox, chooseWithLightMutationCorrectBox, saveStatisticsCheckBox);
        rightMenu.setAlignment(Pos.TOP_LEFT);

        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(bottomMenu);
        borderPane.setLeft(leftMenu);
        borderPane.setRight(rightMenu);


        chooseMapWithTunnelsBox.selectedProperty().addListener((o, oldValue, newValue) -> {
            if (newValue) {
                tunnelCount.textField().setDisable(false);
            } else {
                tunnelCount.textField().setDisable(true);
            }
        });

        Scene scene = new Scene(borderPane, 500, 600);
        primaryStage.setScene(scene);

    }


// Sprawdzanie, czy wszystkie elementy są poprawnie wypełnione
    private boolean checkConfigurationConstraints(){
        for(ConfigurationElement configurationElement: configurationElementArrayList){
            if (!isCorrect(configurationElement)){
                return false;
            }
        }

        if (Integer.parseInt(minMutation.textField().getText()) > Integer.parseInt(maxMutation.textField().getText())){
            AlertBox.display("Incorrect Input", "Minimal Mutation Count cannot be higher than Maximal Mutation Count.");
            return false;
        }
        int area = Integer.parseInt(mapHeight.textField().getText()) * Integer.parseInt(mapWidth.textField().getText());

        if (Integer.parseInt(tunnelCount.textField().getText()) >= area){
            AlertBox.display("Incorrect Input", "Too many tunnels.");
            return false;
        }

        // TODO sprawdzenie pozostałych warunków

        return true;
    }

    private boolean isCorrect(ConfigurationElement configurationElement) {
// dorobi warunek minmutation<maxmutaton<genLen
        try {
            int value = Integer.parseInt(configurationElement.textField().getText());
            if (value >= configurationElement.limits().getX() && value <= configurationElement.limits().getY()) {
                return true;
            } else {
                AlertBox.display("Incorrect input",
                        configurationElement.text().getText() + "value shoud be a natural number between %d and %d.".formatted(configurationElement.limits().getX(), configurationElement.limits().getY()));
                return false;
            }
        } catch (NumberFormatException e) {
            AlertBox.display("Incorrect input",
                    configurationElement.text().getText() + "value shoud be a natural number between %d and %d.".formatted(configurationElement.limits().getX(), configurationElement.limits().getY()));
            return false;
        }
    }

    private SimulationProperties wrapProperties(){

        MapProperties mapProperties = new MapProperties(
                Integer.parseInt(mapWidth.textField().getText()),
                Integer.parseInt(mapHeight.textField().getText()),
                Integer.parseInt(startPlantCount.textField().getText()),
                Integer.parseInt(plantsPerDay.textField().getText()),
                Integer.parseInt(startAnimalCount.textField().getText()),
                chooseMapWithTunnelsBox.isSelected(),
                Integer.parseInt(tunnelCount.textField().getText())
        );

        AnimalProperties animalProperties = new AnimalProperties(
                Integer.parseInt(startAnimalEnergy.textField().getText()),
                Integer.parseInt(energyPlant.textField().getText()),
                Integer.parseInt(minProcreateEnergy.textField().getText()),
                Integer.parseInt(procreateEnergy.textField().getText()),
                Integer.parseInt(minMutation.textField().getText()),
                Integer.parseInt(maxMutation.textField().getText()),
                Integer.parseInt(genomeLength.textField().getText()),
                chooseWithLightMutationCorrectBox.isSelected()
        );

        return new SimulationProperties(mapProperties, animalProperties, chooseMapWithTunnelsBox.isSelected(),
                chooseWithLightMutationCorrectBox.isSelected(), saveStatisticsCheckBox.isSelected());
    }

    private void onSimulationStartClicked(){

        if(checkConfigurationConstraints()){
            simulationStart.newSimulationStart(wrapProperties(), saveStatisticsCheckBox.isSelected());
        }
    }

    private void saveConfiguration(){
        if (checkConfigurationConstraints()){
            NameConfigurationBox.giveConfigurationName("New configuration name: ",
                    wrapProperties());
        }
    }

    private void chooseSavedConfiguration(){
        ConfigurationReader configurationReader = new ConfigurationReader();
        configurationReader.choosePredefinedSimulationProperties(this.simulationStart, saveStatisticsCheckBox.isSelected());
    }





}

