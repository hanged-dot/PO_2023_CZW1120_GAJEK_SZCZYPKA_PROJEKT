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

        if (savingStatisticsRequested){
            simulations.add(new SimulationWithStats(simulationProperties));
        } else {
            simulations.add(new RegularSimulation(simulationProperties));
        }

        new SimulationEngine(simulations).runAsyncInThreadPool();
        // dołozyc wyswietlanie

    }
    //dołozyc configure stage
}
