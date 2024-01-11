package agh.ics.oop.model;

public class GlobeMap extends AbstractMap {
    public GlobeMap(MapProperties mapProperties,
                    AnimalProperties animalProperties,
                    MapChangeListener observer) {
        super(mapProperties, animalProperties, observer);
    }
}
