package agh.ics.oop.presenter;

import agh.ics.oop.model.AnimalProperties;
import agh.ics.oop.model.MapProperties;
import agh.ics.oop.model.Vector2d;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
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

        private ArrayList<ConfigurationElement> configurationElementArrayList;
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.show();

        primaryStage.setTitle("Darwin World");

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
                new Text("Choose number(?percent?) of tunnels: "),
                new TextField("10"),
                new Vector2d(1, 99) // TODO jak wyznaczyć ile tuneli skoro nie znamy rozmiaru mapy? Chyba że wskażemy w procentach
        );

        startPlantCount = new ConfigurationElement(
                new Text("Choose initial number(?percent?) of plants: "),
                new TextField("20"),
                new Vector2d(10, 60) // TODO jw
        );

        plantsPerDay = new ConfigurationElement(
                new Text("Choose daily number(?percent?) of new plants: "),
                new TextField("20"),
                new Vector2d(5, 20)
        );

        energyPlant = new ConfigurationElement(
                new Text("Choose energy from eating one plant: "),
                new TextField("2"),
                new Vector2d(1, 5)
        );

        startAnimalCount = new ConfigurationElement(
                new Text("Choose initial number of animals: "),
                new TextField("50"),
                new Vector2d(10, 200)
        );

        startAnimalEnergy = new ConfigurationElement(
                new Text("Choose initial energy of animals: "),
                new TextField("15"),
                new Vector2d(10, 100)
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
                new Text("Choose minimal number(?percent?) of mutations: "),
                new TextField("0"),
                new Vector2d(0, 10)
        );

        maxMutation = new ConfigurationElement(
                new Text("Choose maximal number (?percent?) of mutations: "),
                new TextField("3"),
                new Vector2d(10, 80)
        );

        genomeLength = new ConfigurationElement(
                new Text("Choose maximal number of mutations: "),
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

////        Text mapHeightText = new Text("Choose map height: ");
//        Text mapWidthText = new Text("Choose map width: ");
//        // TODO: ma się pojawić jak zaznaczymy że chcemy tunele;
//        //  musi sprawdzać czy nie dajemy za dużo tuneli w przeliczeniu na rozmiar mapy
//        Text tunnelCountText = new Text("Choose number of tunnels: ");
//        Text startPlantText = new Text("Choose initial number of plants: ");
//        Text plantsPerDayText = new Text("Choose daily number of new plants: ");
//        Text energyPlantText = new Text("Choose energy from eating one plant: ");
//        Text startAnimalText = new Text("Choose initial number of animals: ");
//        Text startAnimalEnergyText = new Text("Choose initial energy of animals: ");
//        Text minProcreateEnergyText = new Text("Choose minimal energy to procreate: ");
//        Text procreateEnergyText = new Text("Choose energy used by procreating: ");
//        Text minMutationText = new Text("Choose minimal number of mutations: ");
//        Text maxMutationText = new Text("Choose maximal number of mutations: ");
//        Text genomeLengthText = new Text("Choose maximal number of mutations: ");


//        // All text fields needed:
//        mapHeight = new TextField("20");
//        mapWidth = new TextField("20");
//        tunnelCount = new TextField("5");
//        startPlantCount = new TextField("70");
//        plantsPerDayCount = new TextField("20");
//        energyPlant = new TextField("2");
//        startAnimalCount = new TextField("50");
//        startAnimalEnergy = new TextField("15");
//        minProcreateEnergy = new TextField("10");
//        procreateEnergy = new TextField("5");
//        minMutation = new TextField("0");
//        maxMutation = new TextField("3");
//        genomeLength = new TextField("8");


//      Potrzebne CheckBoxy:

        CheckBox saveStatisticsCheckBox = new CheckBox("Save daily statistics");
        chooseMapWithTunnelsBox = new CheckBox("Tunnels");
        tunnelCount.textField().setDisable(!chooseMapWithTunnelsBox.isSelected());

        chooseWithLightMutationCorrectBox = new CheckBox("Light Mutation Correct");

//        Potrzebne przyciski:
        Button saveConfigurationButton = new Button("Save configuration");
//        TODO: zastanowić się jak chcemy przechowywać konfiguracje do wyboru
        Button chooseConfigurationButton = new Button("Choose configuration");

        Button startSimulationButton = new Button("Start simulation");

//        startSimulationButton.setOnAction(e -> {
//            if(checkConfigurationConstraints()){
//                onSimulationStartClicked();
//            };
//        });

//        Layout dla przycisków:

        HBox bottomMenu = new HBox();
        bottomMenu.getChildren().addAll(saveConfigurationButton, chooseConfigurationButton, startSimulationButton);
        bottomMenu.setAlignment(Pos.CENTER);

        VBox leftMenu = new VBox();

        for (ConfigurationElement configurationElement : configurationElementArrayList) {
            leftMenu.getChildren().addAll(
                    configurationElement.text(),
                    configurationElement.textField());
        }
//        leftMenu.getChildren().addAll(
//                mapHeightText, mapHeight,
//                mapWidthText, mapWidth,
//                startPlantText, startPlantCount,
//                plantsPerDayText, plantsPerDayCount,
//                energyPlantText, energyPlant,
//                startAnimalText, startAnimalCount,
//                startAnimalEnergyText, startAnimalEnergy,
//                minProcreateEnergyText, minProcreateEnergy,
//                procreateEnergyText, procreateEnergy,
//                genomeLengthText, genomeLength,
//                minMutationText, minMutation,
//                maxMutationText, maxMutation
//        );
        leftMenu.setAlignment(Pos.TOP_LEFT);

        VBox rightMenu = new VBox();
        rightMenu.getChildren().addAll(chooseMapWithTunnelsBox, chooseWithLightMutationCorrectBox);
        rightMenu.setAlignment(Pos.TOP_LEFT);

        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(bottomMenu);
        borderPane.setLeft(leftMenu);
        borderPane.setRight(rightMenu);


//        W taki sposób będziemy wołać nowe symulacje: tylko oczywiście nie display, a jakieś run simulation itp.

//        button.setOnAction(e -> NewSimulation.display("Simulation 1", "meh"));
//        button.setOnAction(e -> handleOptions(saveStatisticsCheckBox));
//        button.setOnAction(e -> checkChoice(chooseMapChoiceBox));
//
//        VBox layout = new VBox(10);
//        layout.setPadding(new Insets(20, 20, 20, 20));
//        layout.getChildren().addAll(mapHeightText, mapHeight,
//                mapWidthText, mapWidth,
//                chooseMapWithTunnelsBox);

        chooseMapWithTunnelsBox.selectedProperty().addListener((o, oldValue, newValue) -> {
            if (newValue) {
                tunnelCount.textField().setDisable(false);
            } else {
                tunnelCount.textField().setDisable(true);
            }
        });

        Scene scene = new Scene(borderPane, 500, 1000);
        primaryStage.setScene(scene);

    }


// Sprawdzanie, czy wszystkie elementy są poprawnie wypełnione
    private boolean checkConfigurationConstraints(){
        for(ConfigurationElement configurationElement: configurationElementArrayList){
            if (!isCorrect(configurationElement)){
                return false;
            }
        }

        if (Integer.parseInt(minMutation.textField().getText()) > Integer.parseInt(maxMutation.text().getText())){
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

//     do modyfikacji - tutaj będziemy odczytywać poprawne properties, a potem będziemy mogli je przekazywać
//    albo do nowej symulacji, albo do zapisu konfiguracji na przyszłość
//    stworzę nowy rekord, w którym będą wszystkie propertiesy spięte
    private void onSimulationStartClicked(){

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

    }

}

