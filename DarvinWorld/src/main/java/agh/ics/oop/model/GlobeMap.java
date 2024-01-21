package agh.ics.oop.model;

import agh.ics.oop.presenter.SimulationPresenter;

public class GlobeMap extends AbstractMap {
    public GlobeMap(MapProperties mapProperties,
                    AnimalProperties animalProperties, SimulationPresenter simPresenter) {
        super(mapProperties, animalProperties, simPresenter);
    }
}
