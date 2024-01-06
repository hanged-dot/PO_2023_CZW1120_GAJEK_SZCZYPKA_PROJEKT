package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{

    private WorldMap map;
    private List<Animal> animals = new ArrayList<>();

    public Simulation() {


    }

    public Animal getAnimal(int x){ return this.animals.get(x); }

    public void run() {
        //zmienic z dirs na liste animali
        // dodać while do czasu stop lub smierci wszystkich
//        Codziennie będzie wywoływana metoda map.refreshMap(), która zwraca true, jeśli symulacja
//        może być kontynuowana, albo false, jeśli się okaże, że nie ma już żadnych zwierząt

        while(map.refreshMap()){

        }
        for(int counter=0; counter< this.dirs.size(); counter++) {
            int nr_a= counter%this.animals.size();
            if (nr_a==0) {try {Thread.sleep(500);}catch(InterruptedException e){e.printStackTrace();}}
            this.map.move(animals.get(nr_a), this.dirs.get(counter));
//            animals.get(nr_a).age();
            //System.out.println("Zwierzę " + nr_a +" : "+ this.animals.get(nr_a).toString());
            //System.out.println(map);
        }
    }

}

