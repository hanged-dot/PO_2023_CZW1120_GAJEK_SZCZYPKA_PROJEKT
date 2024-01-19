package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public abstract class Simulation implements Runnable{

    private WorldMap map;
    private List<Animal> animals;

// w konstruktorze przekazujemy wszystkie parametry zczytane z okna startowego
    public Simulation(SimulationProperties simulationProperties,
                      ArrayList<MapChangeListener> observers) {

        if (simulationProperties.hasTunnels()){
            this.map = new TunnelMap(simulationProperties.mapProperties(),
                    simulationProperties.animalProperties(), observers);
        } else {
            this.map = new GlobeMap(simulationProperties.mapProperties(),
                    simulationProperties.animalProperties(), observers);
        }

        animals = new ArrayList<>();
    }

    public Animal getAnimal(int x){ return this.animals.get(x); }

    public void run() {
        //zmienic z dirs na liste animali
        // dodać while do czasu stop lub smierci wszystkich
//        Codziennie będzie wywoływana metoda map.refreshMap(), która zwraca true, jeśli symulacja
//        może być kontynuowana, albo false, jeśli się okaże, że nie ma już żadnych zwierząt

        do {
//            TODO Update statystyk symulacji - trzeba ogarnąć ich wyświetlanie
            dailyCycle();

            try {
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (map.refreshMap()); // Wzrastanie nowych roślin na wybranych polach mapy. + sprawdzenie czy są jakieś zwierzęta (inaczej symulacja się kończy) + usunięcie martwych zwierząt

        System.out.println("Symulacja over");

//        for(int counter=0; counter< this.dirs.size(); counter++) {
//            int nr_a= counter%this.animals.size();
//            if (nr_a==0) {try {Thread.sleep(500);}catch(InterruptedException e){e.printStackTrace();}}
//            this.map.move(animals.get(nr_a), this.dirs.get(counter));
////            animals.get(nr_a).age();
//            //System.out.println("Zwierzę " + nr_a +" : "+ this.animals.get(nr_a).toString());
//            //System.out.println(map);
//        }
    }

    protected void dailyCycle(){
        map.moveEveryAnimal();
        map.removeEatenPlants();
        map.procreate();
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

