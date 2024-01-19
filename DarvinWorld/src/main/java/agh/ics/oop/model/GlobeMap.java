package agh.ics.oop.model;

import java.util.ArrayList;

public class GlobeMap extends AbstractMap {
    public GlobeMap(MapProperties mapProperties,
                    AnimalProperties animalProperties,
                    ArrayList<MapChangeListener> observers) {
        super(mapProperties, animalProperties, observers);
    }
}
