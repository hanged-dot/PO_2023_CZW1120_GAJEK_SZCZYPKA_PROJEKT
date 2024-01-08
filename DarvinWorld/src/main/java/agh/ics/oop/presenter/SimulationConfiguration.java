package agh.ics.oop.presenter;

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

public class SimulationConfiguration extends Application {

    private TextField mapHeight;
    private TextField mapWidth;
    private TextField tunnelCount;
    private TextField startPlantCount;
    private TextField plantsPerDayCount;
    private TextField energyPlant;
    private TextField startAnimalCount;
    private TextField startAnimalEnergy;
    private TextField minProcreateEnergy;
    private TextField procreateEnergy;
    private TextField minMutation;
    private TextField maxMutation;
    private TextField genomeLength;


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.show();

        primaryStage.setTitle("Darwin World");

//        All texts needed:
        Text mapHeightText = new Text("Choose map height: ");
        Text mapWidthText = new Text("Choose map width: ");
        // TODO: ma się pojawić jak zaznaczymy że chcemy tunele;
        //  musi sprawdzać czy nie dajemy za dużo tuneli w przeliczeniu na rozmiar mapy
        Text tunnelCountText = new Text("Choose number of tunnels: ");
        Text startPlantText = new Text("Choose initial number of plants: ");
        Text plantsPerDayText = new Text("Choose daily number of new plants: ");
        Text energyPlantText = new Text("Choose energy from eating one plant: ");
        Text startAnimalText = new Text("Choose initial number of animals: ");
        Text startAnimalEnergyText = new Text("Choose initial energy of animals: ");
        Text minProcreateEnergyText = new Text("Choose minimal energy to procreate: ");
        Text procreateEnergyText = new Text("Choose energy used by procreating: ");
        Text minMutationText = new Text("Choose minimal number of mutations: ");
        Text maxMutationText = new Text("Choose maximal number of mutations: ");
        Text genomeLengthText = new Text("Choose maximal number of mutations: ");



        // All text fields needed:
        mapHeight = new TextField("20");
        mapWidth = new TextField("20");
        tunnelCount = new TextField("5");
        startPlantCount = new TextField("70");
        plantsPerDayCount = new TextField("20");
        energyPlant = new TextField("2");
        startAnimalCount = new TextField("50");
        startAnimalEnergy = new TextField("15");
        minProcreateEnergy = new TextField("10");
        procreateEnergy = new TextField("5");
        minMutation = new TextField("0");
        maxMutation = new TextField("3");
        genomeLength = new TextField("8");


//      Potrzebne CheckBoxy:

        CheckBox saveStatisticsCheckBox = new CheckBox("Save daily statistics");
        CheckBox chooseMapWithTunnelsBox = new CheckBox("Tunnels");

//        Potrzebne przyciski:
        Button saveConfigurationButton = new Button("Save configuration");
//        TODO: zastanowić się jak chcemy przechowywać konfiguracje do wyboru
        Button chooseConfigurationButton = new Button("Choose configuration");
        Button startSimulationButton = new Button("Start simulation");

//        Layout dla przycisków:

        HBox bottomMenu = new HBox();
        bottomMenu.getChildren().addAll(saveConfigurationButton, chooseConfigurationButton, startSimulationButton);
        bottomMenu.setAlignment(Pos.CENTER);

        VBox leftMenu = new VBox();
        leftMenu.getChildren().addAll(
                mapHeightText, mapHeight,
                mapWidthText, mapWidth,
                startPlantText, startPlantCount,
                plantsPerDayText, plantsPerDayCount,
                energyPlantText, energyPlant,
                startAnimalText, startAnimalCount,
                startAnimalEnergyText, startAnimalEnergy,
                minProcreateEnergyText, minProcreateEnergy,
                procreateEnergyText, procreateEnergy,
                genomeLengthText, genomeLength,
                minMutationText, minMutation,
                maxMutationText, maxMutation
        );
        leftMenu.setAlignment(Pos.TOP_LEFT);


        VBox rightMenu = new VBox();
        rightMenu.getChildren().add(chooseMapWithTunnelsBox);
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
                rightMenu.getChildren().addAll(tunnelCountText, tunnelCount);
            } else {
                rightMenu.getChildren().removeAll(tunnelCountText, tunnelCount);
            }
        });


        Scene scene = new Scene(borderPane, 500, 1000);
        primaryStage.setScene(scene);

    }

    private void handleOptions(CheckBox box){
        if (box.isSelected()){
            System.out.println("tunnels");
        } else {
            System.out.println("no tunnels");
        }
    }

//    private void handleOptions(CheckBox box1){
//        if (box1.isSelected()){
////            do something
//        }
//    }



    private void setConfiguration(){


    }

    private boolean isCorrect(TextField input, Text instruction, Vector2d limits) {

        try {
            int value = Integer.parseInt(input.getText());
            if (value >= limits.getX() && value <= limits.getY()) {
                return true;
            } else {
                AlertBox.display("Incorrect input",
                        instruction.getText() + "value shoud be a natural number between %d and %d.".formatted(limits.getX(), limits.getY()));
                return false;
            }
        } catch (NumberFormatException e) {
            AlertBox.display("Incorrect input",
                    instruction.getText() + "value shoud be a natural number between %d and %d.".formatted(limits.getX(), limits.getY()));
            return false;
        }
    }

}

