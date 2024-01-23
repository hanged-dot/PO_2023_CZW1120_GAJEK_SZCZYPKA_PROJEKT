package WordMap;


import Presenter.SimulationPresenter;
import Records.AnimalProperties;
import Records.MapProperties;

public class GlobeMap extends AbstractMap {
    public GlobeMap(MapProperties mapProperties,
                    AnimalProperties animalProperties, SimulationPresenter simPresenter) {
        super(mapProperties, animalProperties, simPresenter);
    }
}
