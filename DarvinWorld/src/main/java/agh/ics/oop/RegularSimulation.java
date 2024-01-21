package agh.ics.oop;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.SimulationProperties;

import java.util.ArrayList;

public class RegularSimulation extends Simulation{

    public RegularSimulation(SimulationProperties simulationProperties, MapChangeListener observers) {
        super(simulationProperties, observers);
    }
}
