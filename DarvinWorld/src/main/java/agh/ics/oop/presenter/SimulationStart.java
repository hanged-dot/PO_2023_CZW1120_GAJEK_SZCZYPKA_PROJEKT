package agh.ics.oop.presenter;

import agh.ics.oop.RegularSimulation;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationWithStats;
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
    private ArrayList<Simulation> simulations;


    public SimulationStart(){
        simulations = new ArrayList<>();
    }
    public void newSimulationStart(SimulationProperties simulationProperties, boolean savingStatisticsRequested){
        //  stworzyć prezentera tutaj i przekazać symulacji - done
        BorderPane viewRoot;
        Stage sims = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        try {viewRoot = loader.load(); } catch (Exception e){e.printStackTrace();return ;}
        configureStage(sims,viewRoot);
        sims.show();
        SimulationPresenter observerSim = loader.getController();

        MapChangeListener observers =observerSim;
        Simulation simulation;
        if (savingStatisticsRequested){
            simulation =new SimulationWithStats(simulationProperties, observers);

        } else {
            simulation=(new RegularSimulation(simulationProperties, observers));
        }
        simulations.add(simulation);
        observerSim.setSimulation(simulation);
        new SimulationEngine(simulations).runAsyncInThreadPool();

    }
    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());

    }}
