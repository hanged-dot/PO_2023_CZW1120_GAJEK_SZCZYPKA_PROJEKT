package agh.ics.oop.presenter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;

public class SimulationConfiguration extends Application {

    Button button;
    Button closeButton;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.show();


        primaryStage.setTitle("Darwin World");

//        TextField input = new TextField();
//        CheckBox saveStatisticsCheckBox = new CheckBox("Save daily statistics");

//        Button saveConfigurationButton = new Button("Save this configuration");
        button = new Button("Run simulation");

        ChoiceBox<String> chooseMapChoiceBox = new ChoiceBox<>();
        chooseMapChoiceBox.getItems().addAll("Globe", "Tunnels");
        chooseMapChoiceBox.setValue("Tunnels");


//        W taki sposób będziemy wołać nowe symulacje: tylko oczywiście nie display, a jakieś run simulation itp.

//        button.setOnAction(e -> NewSimulation.display("Simulation 1", "meh"));
//        button.setOnAction(e -> handleOptions(saveStatisticsCheckBox));
        button.setOnAction(e -> checkChoice(chooseMapChoiceBox));

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(chooseMapChoiceBox, button);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);

    }

//    private void handleOptions(CheckBox box1){
//        if (box1.isSelected()){
////            do something
//        }
//    }

    private void checkChoice(ChoiceBox<String> box){
        System.out.println(box.getValue());
    }

}

