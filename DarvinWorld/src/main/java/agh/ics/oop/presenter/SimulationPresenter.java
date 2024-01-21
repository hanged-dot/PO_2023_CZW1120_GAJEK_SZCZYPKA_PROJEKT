package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class SimulationPresenter implements MapChangeListener{
    private WorldMap mapa;
    private Simulation simulation;
    private double CELL_WIDTH=20;
    private double CELL_HEIGHT=20;
    @FXML private Button startStopbutton;
    @FXML private GridPane mapGrid;
    @FXML private Label t1;
    @FXML private Label t2;
    @FXML private Label t3;
    @FXML private Label t4;
    @FXML private Label t5;
    @FXML private Label t6;
    @FXML private Label t7;

    @FXML private Label s1;
    @FXML private Label s2;
    @FXML private Label s3;
    @FXML private Label s4;
    @FXML private Label s5;
    @FXML private Label s6;
    @FXML private Label s7;
    @FXML private Label s8;
    @FXML private Label s9;
    @FXML private Label s10;
    @FXML private Label s11;
//  boolean isPaused;


    public void setSimulation(Simulation simulation){this.simulation=simulation;}
//    public void setWorldMap (WorldMap map){
//        this.mapa=map;
//
//        Stage window = new Stage();
//        window.setTitle("Map "+map.getID());
//        window.setMinWidth(150);
//        isPaused = false;
//        Button closeButton = new Button("Close");
//        closeButton.setOnAction(e -> window.close());
//
//        Button stopButton = new Button("Stop");
//
//        // ponizszy kod na razie nie dzała, bo całość naszej symulacji jeszcze nie wywołuje się w jednym wątku, ale z tego co rozumiem docelowo ma działać\
////         na pojedynczym wątku i wtedy powinno być ok
//
//        stopButton.setOnAction(e -> {setPause(true);
//            while (isPaused){
//                try {wait();} catch (InterruptedException ex) {throw new RuntimeException(ex);}
//            }
//        });
//
//        Button resumeButton = new Button("Resume");
//
//        resumeButton.setOnAction(e -> {
//            setPause(false);
//            notify();
//        });
//
//        VBox layout = new VBox(10);
//        layout.setAlignment(Pos.CENTER);
//        layout.getChildren().addAll(closeButton, stopButton, resumeButton);
//
//        Scene scene = new Scene(layout);
//        window.setScene(scene);
//        window.show();



//    }
//
//    private void setPause(boolean b) {
//        this.isPaused = b;
//    }

    public void drawMap(WorldMap worldMap, String message){
        //infoLabel2.setText(message);
        // TODO znalezc blad
        clearGrid();
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        mapGrid.setGridLinesVisible(true);
        Boundary bounds = worldMap.getCurrentBounds();

        Label labelxy = new Label("y//x");
        labelxy.setMinSize(CELL_WIDTH,CELL_HEIGHT);
        labelxy.setAlignment(Pos.CENTER);
        mapGrid.add(labelxy,0,0);

        for (int x=1;x<=abs(bounds.rightX()-bounds.leftX())+1;x++){
            Label label2 = new Label(Integer.toString(bounds.leftX()+x-1));
            label2.setMinSize(CELL_WIDTH,CELL_HEIGHT);
            label2.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label2, HPos.CENTER);

            mapGrid.add(label2,x,0);
        }
        for (int y=1;y<=abs(bounds.upperY()-bounds.lowerY())+1;y++){
            Label label2 = new Label(Integer.toString(bounds.upperY()-y+1));
            label2.setMinSize(CELL_WIDTH,CELL_HEIGHT);
            label2.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label2, HPos.CENTER);

            mapGrid.add(label2,0,y);
        }
        for (int x=1;x<=abs(bounds.rightX()-bounds.leftX())+1;x++){
            for (int y=1;y<=abs(bounds.upperY()-bounds.lowerY())+1;y++){
                 {
                    Vector2d current = new Vector2d(bounds.leftX()+x-1,bounds.upperY()-y+1);
                    WorldElement plant = this.mapa.getPlant(current);
                    WorldElement strongest = this.mapa.getStrongest(current);
                    if (strongest!=null) {
                        WorldElementBox animalBox = new WorldElementBox(strongest);
                        GridPane.setHalignment(animalBox.getvBox(), HPos.CENTER);
                        mapGrid.add(animalBox.getvBox(), current.getX(),current.getY());
                    }
                    if (plant!=null){
                        WorldElementBox plantBox = new WorldElementBox(plant);
                        mapGrid.add(plantBox.getvBox(),current.getX(),current.getY());
                    }
                }
            }
        }


    }
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {drawMap(worldMap,message);});
    }

    @Override
    public void statisticsChanged(WorldMap worldMap, SimulationStatistics statistics) {
        Platform.runLater(()->{showStatisticsPlease(worldMap,statistics);});
    }

    @Override
    public void animalChanged(Animal animal, WorldMap worldMap, String message) {
        Platform.runLater(()->{animalObserve(animal,worldMap);});
    }

    private void animalObserve(Animal animal, WorldMap worldMap) {
        t1.setText("Genom : "+ animal.getGenome().toString());
        t2.setText("Część aktywowana genomu: "+ animal.getGenomePart());
        t3.setText("Energia: "+ animal.getEnergy());
        t4.setText("Ile zjadł roślin: " + animal.getPlants());
        t5.setText("Ile posiada dzieci:" + animal.getKidsLen());
        t6.setText("Ile posiada potomków: +" + animal.getNumberOfChildren());
        if (animal.getDeath()==0){t7.setText("Ile dni żyje: "+ animal.getAge());}
        t7.setText("Umarło: "+ animal.getDeath());

    }
    public void showStatisticsPlease(WorldMap worldMap, SimulationStatistics stats){
        Boundary bounds = worldMap.getCurrentBounds();
        s1.setText("Statystyki z dnia nr "+worldMap.getDay());
        s2.setText("Ilość żywych zwierząt: " + stats.aliveAnimalCount());
        s3.setText("Ilość martwych zwierząt: "+stats.deadAnimalCount());
        s4.setText("Ilość roślin: "+ stats.plantCount());
        s5.setText("Ilość wolnych pozycji: "+stats.freePositionCount());
        s6.setText("Średnia energia żywych zwierzaków: "+stats.meanAliveAnimalEnergy());
        s7.setText("Dominujący genotyp: " + stats.dominantGenotype().toString());
        s8.setText("Dominujący żyjący genotyp: " + stats.dominantAliveGenotype().toString());
        s9.setText("Średnia długość życia zwierząt: "+stats.meanAnimalLifeSpan());
        s10.setText("Średnia ilość potomstwa: "+stats.meanAliveAnimalOffspringCount());
        //s11.setText("Ilość wolnych pozycji: "+((bounds.upperY()+1)*(bounds.rightX()+1)-(stats.beforeMoveAnimals.size()+plants.size()-plantsToEat.size())));

    }
    public void pause(){
        this.simulation.setPause();
    }

    public void clickGrid(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != mapGrid) {
            Node parent = clickedNode.getParent();
            while (parent != mapGrid) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
        }
    }


}
