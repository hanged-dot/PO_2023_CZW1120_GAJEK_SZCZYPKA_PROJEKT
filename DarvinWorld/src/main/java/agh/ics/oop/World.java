package agh.ics.oop;

import agh.ics.oop.model.*;
import javafx.application.Application;

public class World {

    public static void main(String[] args) {
       
        Application.launch(SimulationApp.class, new String[]{"f","f","b","r","l","b"});

        System.out.println("system zakonczył działanie");

    }
}
