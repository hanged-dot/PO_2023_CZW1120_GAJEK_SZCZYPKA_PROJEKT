package agh.ics.oop;

import agh.ics.oop.model.SimulationProperties;
import agh.ics.oop.model.SimulationStatisticsSaver;

import static java.lang.Thread.sleep;

public class SimulationWithStats extends Simulation{

    private final SimulationStatisticsSaver simulationStatisticsSaver;
    public SimulationWithStats(SimulationProperties simulationProperties) {
        super(simulationProperties);
        this.simulationStatisticsSaver = new SimulationStatisticsSaver();
    }
    @Override
    protected void dailyCycle() {
        super.dailyCycle();
        simulationStatisticsSaver.save(map, map.getSimulationStatistics());
    }
}
