package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{

    private final List<MoveDirection> dirs;
    private WorldMap map;
    private List<Animal> animals = new ArrayList<>();
    //implementacja list jako array, przyda sie do getAnimal szczegolnie dla wiekszej liczby zwierzat

    public Simulation(List<Vector2d> vectors, List<MoveDirection> directions, WorldMap map) {
        this.dirs=directions;
        this.map=map;
        for(int i=0; i<vectors.size();i++){
            if (!(map.objectAt(vectors.get(i)) instanceof Animal)){
                this.animals.add(new Animal(vectors.get(i),0,new int[]{0,1,0,4,5,6}));
                try{ this.map.place(new Animal(vectors.get(i),0,new int[]{0,1,0,4,5,6}));}
                catch (PositionAlreadyOccupiedException p){
                        p.printStackTrace();
                }
            }
        }

    }

    public Animal getAnimal(int x){ return this.animals.get(x); }

    public void run() {
        //zmienic z dirs na liste animali
        // dodać while do czasu stop lub smierci wszystkich
        for(int counter=0; counter< this.dirs.size(); counter++) {
            int nr_a= counter%this.animals.size();
            if (nr_a==0) {try {Thread.sleep(500);}catch(InterruptedException e){e.printStackTrace();}}
            this.map.move(animals.get(nr_a), this.dirs.get(counter));
            animals.get(nr_a).age();
            //System.out.println("Zwierzę " + nr_a +" : "+ this.animals.get(nr_a).toString());
            //System.out.println(map);
        }
    }

}

