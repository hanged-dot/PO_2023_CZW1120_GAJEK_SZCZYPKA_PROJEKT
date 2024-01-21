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

    public SimulationStart(){
    }
    public void newSimulationStart(SimulationProperties simulationProperties, boolean savingStatisticsRequested){

        Simulation s;
        if (savingStatisticsRequested){
            s = new SimulationWithStats(simulationProperties);
        } else {
            s = new RegularSimulation(simulationProperties);
        }

        SimulationEngine.getInstance().runSimAsyncInThreadPool(s);
    }
}
