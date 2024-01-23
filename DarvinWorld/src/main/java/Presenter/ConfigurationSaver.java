package Presenter;



import Records.SimulationProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigurationSaver {

    public static void saveConfiguration(String name, SimulationProperties simulationProperties){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(new File("PO_2023_CZW1120_GAJEK_SZCZYPKA_PROJEKT/DarvinWorld/src/main/resources/configurations/"+name+".txt"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(simulationProperties);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e){
            System.out.println("Error initializing stream");
            AlertBox.display("Error", "Sorry, your configuration could not be saved properly.");
        }
    }

}
