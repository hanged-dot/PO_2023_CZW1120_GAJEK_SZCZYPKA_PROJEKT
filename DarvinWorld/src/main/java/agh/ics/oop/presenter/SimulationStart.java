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
        MapChangeListener observer = new SimulationPresenter();
        // TODO stworzyć prezentera tutaj i przekazać symulacji
//        simulations.add(new Simulation(simulationProperties, observer));
//        new SimulationEngine(simulations).runAsyncInThreadPool();

//        NA RAZIE ODPALAMY 1 SYMULACJe,WĄTKI DODAMY NA KONIEC
        (new Simulation(simulationProperties, observer)).run();

    }
}
