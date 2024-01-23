package Simulation;


import Presenter.SimulationStatisticsSaver;
import Records.SimulationProperties;

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
