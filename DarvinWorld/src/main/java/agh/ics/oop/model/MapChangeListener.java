package agh.ics.oop.model;

public interface MapChangeListener {
    public void mapChanged(WorldMap worldMap, String message);
    public void statisticsChanged(WorldMap worldMap, SimulationStatistics statistics);
    public void animalChanged( Animal animal, WorldMap worldMap, String message);
}
