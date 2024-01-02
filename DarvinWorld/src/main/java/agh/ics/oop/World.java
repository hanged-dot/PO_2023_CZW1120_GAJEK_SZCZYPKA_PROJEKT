package agh.ics.oop;

import agh.ics.oop.model.*;
import javafx.application.Application;

public class World {

    public static void main(String[] args) {
        /*System.out.println("system wystartował");
        MoveDirection[] directions=OptionsParser.optionsPars(args);
        run(directions);
        System.out.println("system zakonczył działanie");

        Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));


        Animal anim1 = new Animal();
        System.out.println(anim1);
        */
        System.out.println("system wystartował");


        //new SimulationEngine(simulations).runSync();
        //System.out.println(map);
/*
        List<MoveDirection> directions;
        try {directions= OptionsParser.parse(args);}
        catch(IllegalArgumentException e){return;}
        List<Vector2d> positions1 = List.of(new Vector2d(2,2), new Vector2d(3,4));
        ConsoleMapDisplay observer = new ConsoleMapDisplay();
        ArrayList<Simulation> simulations= new ArrayList<>();
        //Simulation simulation1 = new Simulation(positions1, directions, map);
        //Simulation simulation2 = new Simulation(positions1, directions, recmap);
        for ( int i =0; i<15; i++) {
            if (i%2==1){
                GrassField map = new GrassField(10);
                map.addObserver(observer);
                simulations.add(new Simulation(positions1, directions, map));
            }
            else{
                RectangularMap recmap = new RectangularMap(4,4);
                recmap.addObserver(observer);
                simulations.add(new Simulation(positions1, directions,recmap ));
            }
        }

        //new SimulationEngine(simulations).runAsync();
        SimulationEngine sim = new SimulationEngine(simulations);
        sims.runAsyncInThreadPool();
        sims.awaitSimulationsEnd();
*/
        Application.launch(SimulationApp.class, new String[]{"f","f","b","r","l","b"});

        System.out.println("system zakonczył działanie");

    }
}
