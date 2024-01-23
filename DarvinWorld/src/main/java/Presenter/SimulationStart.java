package Presenter;


import Records.SimulationProperties;
import Simulation.RegularSimulation;
import Simulation.SimulationEngine;
import Simulation.SimulationWithStats;
import Simulation.Simulation;

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
