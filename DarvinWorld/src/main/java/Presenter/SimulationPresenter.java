package Presenter;

import Records.Boundary;
import Records.PositionAbundance;
import Records.SimulationStatistics;
import WordMap.TunnelMap;
import WordMap.Vector2d;
import WordMap.WorldMap;
import Simulation.Simulation;
import WorldElement.WorldElement;


import WorldElement.Animal;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.swing.text.html.parser.Parser;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

import static java.lang.Math.abs;

public class SimulationPresenter implements MapChangeListener{
    private WorldMap mapa;
    private Simulation simulation;
    private double CELL_WIDTH=25;
    private double CELL_HEIGHT=25;

    @FXML private GridPane mapGrid;
    @FXML private Label t1;
    @FXML private Label t2;
    @FXML private Label t3;
    @FXML private Label t4;
    @FXML private Label t5;
    @FXML private Label t6;
    @FXML private Label t7;
    @FXML private Button closeButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Button positionsPreferredByPlantsButton;
    @FXML private Button animalsWithDominantGenotypeButton;
    @FXML private Label plantPos;
    @FXML private Label animalGen;
    @FXML private BorderPane borderPane;
    @FXML private Label statsLabel;

    public SimulationPresenter() {}

    public SimulationPresenter(Simulation simulation){this.simulation = simulation;}
    public void setSimulation(Simulation simulation) {this.simulation = simulation;}

    public void setWorldMap (WorldMap map){this.mapa=map;}

    public void drawBounds(WorldMap worldMap){
        clearGrid();
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        mapGrid.setGridLinesVisible(true);
        Boundary bounds = worldMap.getCurrentBounds();

        for (int y = 0; y<=abs(bounds.upperY()-bounds.lowerY()); y++){
            Label label2 = new Label(Integer.toString(bounds.upperY()-y));
            label2.setMinSize(CELL_WIDTH,CELL_HEIGHT);
            label2.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label2, HPos.CENTER);

            mapGrid.add(label2,0,y);
        }
        Label labelxy = new Label("y//x");
        labelxy.setMinSize(CELL_WIDTH,CELL_HEIGHT);
        labelxy.setAlignment(Pos.CENTER);
        mapGrid.add(labelxy,0,bounds.upperY()+1);
        for (int x=1;x<=abs(bounds.rightX()-bounds.leftX())+1;x++){
            Label label3 = new Label(Integer.toString(bounds.leftX()+x-1));
            label3.setMinSize(CELL_WIDTH,CELL_HEIGHT);
            label3.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label3, HPos.CENTER);

            mapGrid.add(label3,x,bounds.upperY()+1);
        }
    }
    public void drawMap(WorldMap worldMap, String message){

        showStatistics();
        drawBounds(worldMap);
        Boundary bounds = worldMap.getCurrentBounds();


        for (int x = 1; x<=bounds.rightX()+1; x++){
            for (int y=0; y<=bounds.upperY()+1; y++){
                 {
                    Vector2d current = new Vector2d(bounds.rightX()-x+1, y);
                    WorldElement plant = worldMap.getPlant(current);
                    WorldElement strongest = worldMap.getStrongest(current);
                    if (strongest!=null) {
                        WorldElementBox animalBox = new WorldElementBox(strongest);
                        mapGrid.add(animalBox.getvBox(),x ,y);
                    }
                    else if (plant!=null){
                        WorldElementBox plantBox = new WorldElementBox(plant);
                        mapGrid.add(plantBox.getvBox(),x,y);
                    }
                    if (simulation.isTunnel()){
                        TunnelMap tunnelMap = (TunnelMap) this.mapa;
                        if(tunnelMap.getTunnels().containsKey(current)){
                            Rectangle tunnel = new Rectangle(CELL_WIDTH,CELL_HEIGHT);
                            tunnel.setFill(Color.SANDYBROWN);
                            mapGrid.add(tunnel,x,y);
                        }
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
        Platform.runLater(() -> {
            drawMap(worldMap, message);
//            showStatistics();
        });
    }
    @Override
    public void statisticsChanged(){

    }

    private void showStatistics(){
        var message = getStatisticsMessage();
        statsLabel.setText(message);
    }

    private String getStatisticsMessage(){

        SimulationStatistics statistics = mapa.getSimulationStatistics();

        String dominantGenotypeOfAliveAnimals = "";
        String dominantGenotypeOfAllAnimals = "";
        for (int i = 0; i < statistics.dominantGenotype().length; ++i){
            dominantGenotypeOfAllAnimals+="%d".formatted(statistics.dominantGenotype()[i]);
            if (statistics.dominantAliveGenotype().length ==0){dominantGenotypeOfAliveAnimals="-";}
            else {dominantGenotypeOfAliveAnimals+="%d".formatted(statistics.dominantAliveGenotype()[i]);}
        }
        String message =  "AliveAnimalCount: %d\nDeadAnimalCount: %d\nNumber of plants: %d\nNumber of free positions: %d\nMean energy of alive animals: %f\nMean life span of dead animals: %f\nMean count of children: %f\nDominant genotype for alive animals: %s\nDominant genotype for dead animals: %s".formatted(statistics.aliveAnimalCount(), statistics.deadAnimalCount(), statistics.plantCount(), statistics.freePositionCount(),
                statistics.meanAliveAnimalEnergy(), statistics.meanAnimalLifeSpan(), statistics.meanAliveAnimalOffspringCount(),  dominantGenotypeOfAliveAnimals, dominantGenotypeOfAllAnimals);
        return message;
    }

    @Override
    public void statisticsChanged(WorldMap worldMap, SimulationStatistics statistics) {

    }

    @Override
    public void animalChanged(Animal animal, WorldMap worldMap) {
        Platform.runLater(()->{animalObserve(animal,worldMap);});
    }

    private void animalObserve(Animal animal, WorldMap worldMap) {
        String genes= new String();
        for (int i: animal.getGenome()){
            genes+= Integer.toString(i);
        }
        t1.setText("Genom : "+ genes);
        t2.setText("Activated part of genome: "+ animal.getGenomePart());
        t3.setText("Energy: "+ animal.getEnergy());
        t4.setText("Plants eaten: " + animal.getPlants());
        t5.setText("Number of children:" + animal.getKidsLen());
        t6.setText("Number of descendants: +" + animal.getNumberOfChildren());
        if (animal.getDeath()==0){t7.setText("Days alive: "+ animal.getAge());}
        t7.setText("Death: "+ animal.getDeath());
    }


    public void pause(){simulation.pause();}
    public void resume(){simulation.resume();}

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
            Vector2d position = new Vector2d(this.mapa.getCurrentBounds().rightX()-rowIndex,colIndex-1);
            WorldElement animal = this.mapa.getStrongest(position);
            System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
            if (animal!=null) { this.mapa.addAnimalObserver((Animal) animal);}


        }
    }
//    prezenter do wyświetlania pozycji najbardziej wybieranych przez roślinki, no usage nie jest prawda intelliJ klamie

    public void preferredPositions(){
        drawBounds(this.mapa);
        Boundary bounds = this.mapa.getCurrentBounds();
        Boundary equator = this.mapa.getPlantPositionGenerator().getEquatorBoundary();
        ArrayList<PositionAbundance>  positionAbundances = mapa.getPositionsPreferredByPlants();

        for (int x=1;x<=bounds.rightX()+1;x++){
            for (int y=0;y<=bounds.upperY();y++){
                {
                    Vector2d current = new Vector2d(bounds.rightX()-x+1, y);

                    Optional <PositionAbundance> elOpt = positionAbundances.stream().filter(e-> e.position().equals(current)).findFirst();

                    if (elOpt.isPresent()) {
                        var el = elOpt.get();
                        Label plantnr = new Label();
                        plantnr.setText(Integer.toString(el.numberOfPlants()));
                        mapGrid.add(plantnr, x,y);
                    }
                    else {
                        if (current.follows(new Vector2d(equator.leftX(),equator.lowerY())) && current.precedes(new Vector2d(equator.rightX(),equator.upperY()))){
                            Rectangle equatorRectanle = new Rectangle(CELL_WIDTH,CELL_HEIGHT);
                            equatorRectanle.setFill(Color.LAWNGREEN);
                            mapGrid.add(equatorRectanle,x,y);
                        }
                    }

                }
            }
        }
    }
//    prezenter do wyświetlania tych animali z najbardziej dominującym genomem no usage nie jest prawda intelliJ klamie
    public void dominantGenotypeAnimals() {
        drawBounds(this.mapa);
        Boundary bounds = this.mapa.getCurrentBounds();

        ArrayList<Animal> animals = mapa.getAnimalsWithDominantGenotype();

        for (int x = 1; x <= bounds.rightX()+1; x++) {
            for (int y = 0; y <= bounds.upperY() ; y++) {
                {
                    Vector2d current = new Vector2d(bounds.rightX() - x+1, y );
                    Optional <Animal> elOpt = animals.stream().filter(e-> e.getPosition().equals(current)).findFirst();
                    if (elOpt.isPresent()) {
                        WorldElement dominantGenomeAnimal = (WorldElement) elOpt.get();
                        WorldElementBox animalBox = new WorldElementBox(dominantGenomeAnimal);
                        mapGrid.add(animalBox.getvBox(), x,y);
                    }
                }
            }
        }
    }

    public void pauseUpdate() {
        positionsPreferredByPlantsButton.setDisable(false);
        animalsWithDominantGenotypeButton.setDisable(false);
    }

    public void resumeUpdate() {
        positionsPreferredByPlantsButton.setDisable(true);
        animalsWithDominantGenotypeButton.setDisable(true);
    }

    public void closeSimulation(){
        simulation.terminate();
        ((Stage) borderPane.getScene().getWindow()).close();
    }

}