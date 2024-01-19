package agh.ics.oop;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.SimulationProperties;
import agh.ics.oop.model.SimulationStatisticsSaver;

import java.util.ArrayList;

public class SimulationWithStats extends Simulation{

    private SimulationStatisticsSaver simulationStatisticsSaver;
    public SimulationWithStats(SimulationProperties simulationProperties, ArrayList<MapChangeListener> observers) {
        super(simulationProperties, observers);
        this.simulationStatisticsSaver = new SimulationStatisticsSaver();
    }
    @Override
    protected void dailyCycle(){
        super.dailyCycle();
        simulationStatisticsSaver.save(map, map.getSimulationStatistics());
//        i tutaj będzie wywołanie dla simulationStatisticsSaver żeby zapisywał staty
    }
}
