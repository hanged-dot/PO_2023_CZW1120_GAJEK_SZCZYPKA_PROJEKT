package agh.ics.oop.presenter;

import agh.ics.oop.model.SimulationProperties;

import java.io.*;

public class ConfigurationSaver {

    public static void saveConfiguration(String name, SimulationProperties simulationProperties){

        try{
            FileOutputStream f = new FileOutputStream("src/main/java/agh/ics/oop/presenter/configurations/"+name+".txt");
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(simulationProperties);
            o.close();
            f.close();

        } catch (IOException e){
            System.out.println("Error initializing stream");
            AlertBox.display("Error", "Sorry, your configuration could not be saved properly.");
        }
    }

}
