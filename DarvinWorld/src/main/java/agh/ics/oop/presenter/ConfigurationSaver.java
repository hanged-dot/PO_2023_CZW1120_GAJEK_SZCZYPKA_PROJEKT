package agh.ics.oop.presenter;

import agh.ics.oop.model.SimulationProperties;

import java.io.*;

public class ConfigurationSaver {

//    `czy to tak będzie działać???
    public static void saveConfiguration(String name, SimulationProperties simulationProperties){

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(new File("/configurations/"+name+".txt"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(simulationProperties);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();

        } catch (IOException e){
            System.out.println("Error initializing stream");
        }
    }


}
