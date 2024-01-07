package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SimulationStart {

    @FXML private TextField height;
    @FXML private TextField width;
    @FXML private TextField startPlant;
    @FXML private TextField energy1Plant;
    @FXML private TextField plantsPerDay;
    @FXML private TextField tunnelCount;
    @FXML private TextField startAnimal;
    @FXML private TextField startAnimalEnergy;
    @FXML private TextField minFedEnergy;
    @FXML private TextField procreateEnergy;
    @FXML private TextField breedEnergy;
    @FXML private TextField minMutationNumber;
    @FXML private TextField maxMutationNumber;
    @FXML private TextField genomeLength;

    public void onSimulationStartClicked(){
        // add map i animal properties
        MapProperties mapProperties = new MapProperties(
                Integer.parseInt(width.getText()),
                Integer.parseInt(height.getText()),
                Integer.parseInt(startPlant.getText()),
                Integer.parseInt(plantsPerDay.getText()),
                Integer.parseInt(startAnimal.getText()),
                true,
                Integer.parseInt(tunnelCount.getText()));

        AnimalProperties animalProperties = new AnimalProperties(
                Integer.parseInt(startAnimalEnergy.getText()),
                Integer.parseInt(energy1Plant.getText()),
                Integer.parseInt(breedEnergy.getText()),
                Integer.parseInt(procreateEnergy.getText()),
                Integer.parseInt(minMutationNumber.getText()),
                Integer.parseInt(maxMutationNumber.getText()),
                Integer.parseInt(genomeLength.getText()),
                false);

        BorderPane viewRoot;
        Stage sims = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        try {
            viewRoot = loader.load();
        } catch (Exception e){
            e.printStackTrace();
            return ;
        }
        configureStage(sims,viewRoot);
        sims.show();
        SimulationPresenter presenter = loader.getController();
// TODO arraylistę symulacji trzeba przenieść gdzieś wyżej, bo na klik start mamy dodać nową symulację do
//        istniejącej listy, a nie tworzyć nową listę z jedną symulacją
        ArrayList<Simulation> simulations= new ArrayList<>();
        simulations.add(new Simulation(mapProperties, animalProperties, presenter));
        new SimulationEngine(simulations).runAsyncInThreadPool();

    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Darvin World MS, MG");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
