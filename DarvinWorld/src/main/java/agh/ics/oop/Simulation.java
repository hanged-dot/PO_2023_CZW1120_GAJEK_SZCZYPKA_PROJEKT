package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{

    private WorldMap map;
    private List<Animal> animals = new ArrayList<>();

// w konstruktorze przekazujemy wszystkie parametry sczytane z okna startowego
    public Simulation(SimulationProperties simulationProperties,
                      MapChangeListener observer) {

        if (simulationProperties.hasTunnels()){
            this.map = new TunnelMap(simulationProperties.mapProperties(),
                    simulationProperties.animalProperties(), observer);
        } else {
            this.map = new GlobeMap(simulationProperties.mapProperties(),
                    simulationProperties.animalProperties(), observer);
        }

        System.out.println(simulationProperties.animalProperties().energyFromPlant());

    }

    public Animal getAnimal(int x){ return this.animals.get(x); }

    public void run() {
        //zmienic z dirs na liste animali
        // dodać while do czasu stop lub smierci wszystkich
//        Codziennie będzie wywoływana metoda map.refreshMap(), która zwraca true, jeśli symulacja
//        może być kontynuowana, albo false, jeśli się okaże, że nie ma już żadnych zwierząt

        do {
//            TODO Update statystyk symulacji - trzeba ogarnąć ich wyświetlanie
            map.getSimulationStatistics();
//            Usunięcie martwych zwierzaków z mapy
            map.removeDeadAnimals();
//            Skręt i przemieszczenie każdego zwierzaka
            map.moveEveryAnimal();
//            Konsumpcja roślin, na których pola weszły zwierzaki
            map.removeEatenPlants();
//            Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu
            map.procreate();
        } while (map.refreshMap()); // Wzrastanie nowych roślin na wybranych polach mapy.

//        for(int counter=0; counter< this.dirs.size(); counter++) {
//            int nr_a= counter%this.animals.size();
//            if (nr_a==0) {try {Thread.sleep(500);}catch(InterruptedException e){e.printStackTrace();}}
//            this.map.move(animals.get(nr_a), this.dirs.get(counter));
////            animals.get(nr_a).age();
//            //System.out.println("Zwierzę " + nr_a +" : "+ this.animals.get(nr_a).toString());
//            //System.out.println(map);
//        }
    }

//    tu pewnie będzie konieczna zmiana nazwy - metoda, która ma się wywoałać, kiedy po zatrzymaniu
//    symulacji użytkownik naciśnie odpowiedni przycisk
    public void highlightAnimalsWithMostPopularGenome(){
        ArrayList<Animal> animals = map.getAnimalsWithDominantGenotype();
//        TODO highlighting animals
    }

//    j.w. ale tym razem jak użytkownik zażyczy sobie podświetlenie pól najchętniej zarastanych
//    przez rośliny

    public void highlightPositionsPreferredByPlants(){

        ArrayList<PositionAbundance> positionsWithPlantCount = map.getPositionsPreferredByPlants();

        ArrayList<Vector2d> positions = new ArrayList<>();
        for (PositionAbundance p : positionsWithPlantCount){
            positions.add(p.position());
//            TODO highlight position (może nawet nie potrzeba tej arraylisty wektorów)
        }
    }


}

