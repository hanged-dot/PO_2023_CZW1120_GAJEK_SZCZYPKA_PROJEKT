package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class SimulationPresenter implements MapChangeListener{
    private WorldMap mapa;
    private Simulation simulation;
    private double CELL_WIDTH=25;
    private double CELL_HEIGHT=25;
    @FXML private Button startStopButton;
    @FXML private Button closeButton;
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
    @FXML private Button positionsPreferredByPlantsButton;
    @FXML private Button animalsWithDominantGenotypeButton;
    @FXML private Label plantPos;
    @FXML private Label animalGen;
    @FXML private BorderPane borderPane;
    @FXML private Label statsLabel;



    public SimulationPresenter(Simulation simulation){
        this.simulation = simulation;
    }
//    @FXML
 //   private Button button = new Button("Start");
    public void setWorldMap (WorldMap map){
        statsLabel = new Label();
        this.mapa=map;

        Stage window = new Stage();
        window.setTitle("Map "+map.getID());
        window.setMinWidth(500);
        window.setMinHeight(500);


        // button do wyświetlania najchętniej zarastanych pól

        positionsPreferredByPlantsButton = new Button("Get positions preferred by plants");
        positionsPreferredByPlantsButton.setDisable(true);

        positionsPreferredByPlantsButton.setOnAction(e -> {
            ArrayList<PositionAbundance>  positionAbundances = mapa.getPositionsPreferredByPlants();
            System.out.println("Preferowane pozycje roślin: ");
            for (PositionAbundance p : positionAbundances){
                System.out.println(p.position()+" : "+p.numberOfPlants());
            }
        });

        // Button do wyświetlania zwierząt o najpopularniejszym genotypie

        animalsWithDominantGenotypeButton = new Button("Get animals with dominant genotype");
        animalsWithDominantGenotypeButton.setDisable(true);

        animalsWithDominantGenotypeButton.setOnAction(e -> {
            ArrayList<Animal> animals = mapa.getAnimalsWithDominantGenotype();
            System.out.println("Zwierzęta z dominującym genotypem: ");
            for (Animal a : animals){
                System.out.println(a.getPosition());
            }
        });

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            simulation.terminate();
            window.close();
        });
        Button pauseButton = new Button("Pause");

        // ponizszy kod na razie nie dzała, bo całość naszej symulacji jeszcze nie wywołuje się w jednym wątku, ale z tego co rozumiem docelowo ma działać\
//         na pojedynczym wątku i wtedy powinno być ok

        pauseButton.setOnAction(e -> {
            simulation.pause();
        });

        Button resumeButton = new Button("Resume");

        resumeButton.setOnAction(e -> {
            simulation.resume();
        });

        HBox bottomMenu = new HBox(10);
        bottomMenu.setAlignment(Pos.CENTER);
        bottomMenu.getChildren().addAll(closeButton, pauseButton, resumeButton);

        VBox leftPane = new VBox();

        leftPane.getChildren().addAll(statsLabel,positionsPreferredByPlantsButton, animalsWithDominantGenotypeButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(bottomMenu);
        borderPane.setLeft(leftPane);
//        TODO: right menu to miejsce na mapę
//        borderPane.setRight(rightMenu);

        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.show();
    }

    public void drawMap(WorldMap worldMap, String message){

        clearGrid();
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        mapGrid.setGridLinesVisible(true);
        Boundary bounds = worldMap.getCurrentBounds();

        for (int y=0;y<=abs(bounds.upperY()-bounds.lowerY());y++){
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

        for (int x=0;x<=bounds.rightX();x++){
            for (int y=1;y<=bounds.upperY()+1;y++){
                 {
                    Vector2d current = new Vector2d(bounds.rightX()-x, y-1);
                    WorldElement plant = worldMap.getPlant(current);
                    WorldElement strongest = worldMap.getStrongest(current);
                    if (strongest!=null) {
                        WorldElementBox animalBox = new WorldElementBox(strongest);
                        mapGrid.add(animalBox.getvBox(),y ,x);
                    }
                    else if (plant!=null){
                        WorldElementBox plantBox = new WorldElementBox(plant);
                        mapGrid.add(plantBox.getvBox(),y,x);
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

//        na razie testowałam same statystyki, jka będzie mapa gotowa to dokomentujemy ten fragment

//        Platform.runLater(() -> {
//            drawMap(worldMap, message);
//        });
    }
    @Override
    public void statisticsChanged(){
        Platform.runLater(this::showStatistics);
    }

    private void showStatistics(){
        statsLabel.setText(getStatisticsMessage());
    }

    private String getStatisticsMessage(){

        SimulationStatistics statistics = mapa.getSimulationStatistics();

        String dominantGenotypeOfAliveAnimals = "";
        String dominantGenotypeOfAllAnimals = "";
        for (int i = 0; i < statistics.dominantGenotype().length; ++i){
            dominantGenotypeOfAllAnimals+="%d".formatted(statistics.dominantGenotype()[i]);
            dominantGenotypeOfAliveAnimals+="%d".formatted(statistics.dominantAliveGenotype()[i]);
        }

        String message =  "AliveAnimalCount: %d\nDeadAnimalCount: %d\nNumber of plants: %d\nNumber of free positions: %d\nMean energy of alive animals: %f\nMean life span of dead animals: %f\nMean count of children: %f\nDominant genotype for alive animalss: %s\nDominant genotype for dead animals: %s".formatted(statistics.aliveAnimalCount(), statistics.deadAnimalCount(), statistics.plantCount(), statistics.freePositionCount(),
                statistics.meanAliveAnimalEnergy(), statistics.meanAnimalLifeSpan(), statistics.meanAliveAnimalOffspringCount(),  dominantGenotypeOfAliveAnimals, dominantGenotypeOfAllAnimals);
        return message;
    }

    @Override
    public void statisticsChanged(WorldMap worldMap, SimulationStatistics statistics) {
        Platform.runLater(()->{showStatisticsPlease(worldMap,statistics);});
    }

    @Override
    public void animalChanged(Animal animal, WorldMap worldMap) {
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
        s2.setText("Ilosc zywych zwierzat: " + stats.aliveAnimalCount());
        s3.setText("Ilosc martwych zwierzat: "+stats.deadAnimalCount());
        s4.setText("Ilosc roslin: "+ stats.plantCount());
        s5.setText("Ilosc wolnych pozycji: "+stats.freePositionCount());
        s6.setText("Srednia energia zywych zwierzakow: "+stats.meanAliveAnimalEnergy());
        s7.setText("Dominujący genotyp: " + stats.dominantGenotype().toString());
        s8.setText("Dominujacy zyjący genotyp: " + stats.dominantAliveGenotype().toString());
        s9.setText("Srednia dlugosc zycia zwierzat: "+stats.meanAnimalLifeSpan());
        s10.setText("Srednia ilosc potomstwa: "+stats.meanAliveAnimalOffspringCount());
        //s11.setText("Ilość wolnych pozycji: "+((bounds.upperY()+1)*(bounds.rightX()+1)-(stats.beforeMoveAnimals.size()+plants.size()-plantsToEat.size())));

    }
//    no usage nie jest prawda intelliJ klamie
    public void pause(){
        this.simulation.setPause();
    }

    public void clickGrid(javafx.scene.input.MouseEvent event) {
        // TODO znalezc blad
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != mapGrid) {
            Node parent = clickedNode.getParent();
            while (parent != mapGrid) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            Vector2d position = new Vector2d(rowIndex,colIndex);
            WorldElement animal = this.mapa.getStrongest(position);
            if (animal!=null) { this.mapa.addAnimalObserver((Animal) animal);}


        }
    }
//    prezenter do wyświetlania pozycji najbardziej wybieranych przez roślinki, no usage nie jest prawda intelliJ klamie

    public void preferredPositions(){
        clearGrid();
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        mapGrid.setGridLinesVisible(true);
        Boundary bounds = this.mapa.getCurrentBounds();

        ArrayList<PositionAbundance>  positionAbundances = mapa.getPositionsPreferredByPlants();
        String s = ("Preferowane pozycje roślin: ");
        for (PositionAbundance p : positionAbundances){
            s+=(p.position()+" : "+p.numberOfPlants());
        }
        plantPos.setText(s);
        for (int y=0;y<=abs(bounds.upperY()-bounds.lowerY());y++){
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

        for (int x=0;x<=bounds.rightX();x++){
            for (int y=1;y<=bounds.upperY()+1;y++){
                {
                    Vector2d current = new Vector2d(bounds.rightX()-x, y-1);
                    // TODO zmienic na znajdowanie pozycji w liscie lubianych pozycji
                    WorldElement plant = null;
                    WorldElementBox plantBox = new WorldElementBox(plant);
                    mapGrid.add(plantBox.getvBox(),y ,x);


                }
            }
        }
    }
//    prezenter do wyświetlania tych animali z najbardziej dominującym genomem no usage nie jest prawda intelliJ klamie
    public void dominantGenotypeAnimals(){
        clearGrid();
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        mapGrid.setGridLinesVisible(true);
        Boundary bounds = this.mapa.getCurrentBounds();

        ArrayList<Animal> animals = mapa.getAnimalsWithDominantGenotype();
        String s ="Zwierzęta z dominującym genotypem:";
        for (Animal a : animals){
            s+=(a.getPosition());
        }
        animalGen.setText(s);

        for (int y=0;y<=abs(bounds.upperY()-bounds.lowerY());y++){
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

        for (int x=0;x<=bounds.rightX();x++){
            for (int y=1;y<=bounds.upperY()+1;y++){
                {
                    Vector2d current = new Vector2d(bounds.rightX()-x, y-1);
                    //zmienic na znajdowanie w animalu
                    WorldElement dominantGenomeAnimal = null;
                    WorldElementBox animalBox = new WorldElementBox(dominantGenomeAnimal);
                        mapGrid.add(animalBox.getvBox(),y ,x);

    public void pauseUpdate() {
        positionsPreferredByPlantsButton.setDisable(false);
        animalsWithDominantGenotypeButton.setDisable(false);
    }

    public void resumeUpdate() {
        positionsPreferredByPlantsButton.setDisable(true);
        animalsWithDominantGenotypeButton.setDisable(true);
    }

// animalChanged



}
