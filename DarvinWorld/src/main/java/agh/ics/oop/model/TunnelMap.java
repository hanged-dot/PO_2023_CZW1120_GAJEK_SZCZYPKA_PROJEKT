package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Random;

public class TunnelMap extends AbstractMap {

    private int tunnelCount;
    private HashMap<Vector2d, Vector2d> tunnels;
    public TunnelMap(MapProperties mapProperties, AnimalProperties animalProperties) {

        super(mapProperties, animalProperties);
        this.tunnelCount = mapProperties.tunnelCount();

        while (tunnelCount > 0){

            Random random = new Random();
            Vector2d firstEnd = createRandomPosition(mapBoundary);
            Vector2d secondEnd = createRandomPosition(mapBoundary);

            if (!tunnels.containsKey(firstEnd) && !tunnels.containsKey(secondEnd) &&
                    !firstEnd.equals(secondEnd)){
                tunnels.put(firstEnd, secondEnd);
                tunnels.put(secondEnd, firstEnd);
            }
        }
    }

//    żeby zwierzak nie wpadł w pułapkę, że po wejściu na tunel przemieszcza się tylko
//    pomiędzy dwoma końcami tunelu, musimy pzechowywać jakiegoś boola w którym będzie
//    przechowywana informacja, czy zwierzak się właśnie przetransportowal przez tunel -
//    jeśli tak, to kolejny ruch jest wykonywany "normlanie"
//     w tym celu chyba najlepiej będzie do mapy z tunelami mieć inną klasę zwierząt, która
//     będzie mieć wszystkie metody takie jak Animal, ale będzie mieć dodatkowy atrybut
//    z boolem przechowującym informację, czy zwierzak właśnie się przetransportował
//     z tunelu czy normalnie wszedł na dane pole
    @Override
    protected Vector2d getNextPosition(Animal animal){

        if (tunnels.containsKey(animal.getPosition()) && !animal.isTransferredThroughTunnel()){
            Vector2d targetPosition = tunnels.get(animal.getPosition());
            animal.setTransferredThroughTunnel(true);
            return targetPosition;
        } else {
            animal.setTransferredThroughTunnel(false);
            return super.getNextPosition(animal);
        }
    }

}
