package agh.ics.oop.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileMapDisplay implements MapChangeListener {
    @Override
    public void mapChanged(WorldMap worldMap, String message) {
      File file = new File("map_"+worldMap.getID()+".log");
      if(!file.exists() && !file.isDirectory()) try{file.createNewFile();} catch (IOException e) {throw new RuntimeException(e);} ;

      try{
          FileWriter fileWriter = new FileWriter(file,true);
          BufferedWriter fileWriterBuffer = new BufferedWriter(fileWriter);
          fileWriterBuffer.append(message);
          fileWriterBuffer.newLine();
          fileWriterBuffer.flush();
          fileWriterBuffer.close();
      }catch (IOException e){ throw new RuntimeException(e);};
        System.out.println("zapis");
    }
}
