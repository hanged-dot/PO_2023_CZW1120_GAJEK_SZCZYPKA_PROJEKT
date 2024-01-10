package agh.ics.oop.presenter;

import agh.ics.oop.model.SimulationProperties;

import java.io.*;

public class ConfigurationSaver {

//    `czy to tak będzie działać???
    public static void saveConfiguration(String name, SimulationProperties simulationProperties){

        try{
            FileOutputStream f = new FileOutputStream(new File("/configurations/"+name+".txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(simulationProperties);
            o.close();
            f.close();

        } catch (IOException e){
            System.out.println("Error initializing stream");
        }
//        TODO: zapis konfiguracji do pliku
    }


}
