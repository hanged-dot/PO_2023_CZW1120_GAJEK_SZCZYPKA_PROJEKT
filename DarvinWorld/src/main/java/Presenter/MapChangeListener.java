package Presenter;

import Records.SimulationStatistics;
import WordMap.WorldMap;
import WorldElement.Animal;

public interface MapChangeListener {
    public void mapChanged(WorldMap worldMap, String message);

    void statisticsChanged();

    public void statisticsChanged(WorldMap worldMap, SimulationStatistics statistics);
    public void animalChanged(Animal animal, WorldMap worldMap);
    public void setWorldMap (WorldMap map);
}
