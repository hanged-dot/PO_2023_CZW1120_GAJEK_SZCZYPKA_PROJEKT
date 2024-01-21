package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class SimulationPresenter implements MapChangeListener{
    private WorldMap mapa;
    private double CELL_WIDTH=30;
    private double CELL_HEIGHT=30;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label infoLabel2;
    @FXML private Label infoLabel;
    @FXML private Label statsLabel;

    private final Simulation simulation;

    Button positionsPreferredByPlantsButton;
    Button animalsWithDominantGenotypeButton;

    public SimulationPresenter(Simulation simulation){
        this.simulation = simulation;
    }

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

        infoLabel2.setText(message);
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
                        Button button1 = new Button();
                        ImageView image = new ImageView(strongest.getPicture().toString());
                        button1.setGraphic(image);
                        button1.setMinSize(CELL_WIDTH, CELL_HEIGHT);
                        button1.setAlignment(Pos.CENTER);
                        GridPane.setHalignment(button1, HPos.CENTER);

                        mapGrid.add(button1, current.getX(),current.getY());
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
