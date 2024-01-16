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
    private ArrayList<Simulation> simulations;

    public SimulationStart(){
        simulations = new ArrayList<>();
    }
    public void newSimulationStart(SimulationProperties simulationProperties){
        ArrayList<MapChangeListener> observers = new ArrayList<>();
              SimulationPresenter observerSim = new SimulationPresenter();
              observers.add(observerSim);
              // TODO dodac sprawdzanie czy saveStatisticsCheckBox.isSelected()
              FileMapDisplay observerSaveSim = new FileMapDisplay();
              observers.add(observerSaveSim);
        //  stworzyć prezentera tutaj i przekazać symulacji - done

        simulations.add(new Simulation(simulationProperties, observers));
        new SimulationEngine(simulations).runAsyncInThreadPool();

    }
}
