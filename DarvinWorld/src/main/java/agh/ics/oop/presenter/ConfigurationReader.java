package agh.ics.oop.presenter;

import agh.ics.oop.model.SimulationProperties;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;

public class ConfigurationReader {
    public void choosePredefinedSimulationProperties(){

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Ready conifgurations");
        window.setMinWidth(150);

        Label label = new Label("Choose configuration: ");

        File folder = new File("/configurations/");
        File[] files = folder.listFiles();

        ChoiceBox choiceBox = new ChoiceBox<>();

        for (File file: files){
            if (file.isFile() && file.getName().endsWith(".txt")){
                String name = file.getName();
                int len = name.length();
                choiceBox.getItems().add(name.substring(0, len - 5));
            }
        }

        Button acceptButton = new Button("Start simulation");
        acceptButton.setOnAction(e -> {
            this.onSimulationStartClicked((String)choiceBox.getValue());
            window.close();
        });

        Button abortButton = new Button("Cancel");
        abortButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, choiceBox, acceptButton, abortButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

    private void onSimulationStartClicked(String configurationName) throws Exception{
        try {
            FileInputStream fi = new FileInputStream(new File("/configurations/"+configurationName+".txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            SimulationProperties simulationProperties = (SimulationProperties) oi.readObject();
//            TODO: zmienić tak żeby stąd też się dało dodawać symulacje i uruchamiac

        } catch (FileNotFoundException e){
            System.out.println("File not found");
        } catch (IOException e){
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
