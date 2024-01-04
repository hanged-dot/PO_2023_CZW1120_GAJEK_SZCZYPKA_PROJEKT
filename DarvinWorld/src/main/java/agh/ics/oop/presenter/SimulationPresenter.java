package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

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


//    @FXML
 //   private Button button = new Button("Start");
    public void setWorldMap (WorldMap map){ mapa=map;}

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
// dodac wyświetlanie najsilnieszego zwierzaka z kolejki chyba ze umalo
        for (int x=1;x<=abs(bounds.rightX()-bounds.leftX())+1;x++){
            for (int y=1;y<=abs(bounds.upperY()-bounds.lowerY())+1;y++){
                 {
                    Button button1 = new Button();
                    button1.setMinSize(CELL_WIDTH, CELL_HEIGHT);
                    button1.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(button1, HPos.CENTER);

                    mapGrid.add(button1, x,y);
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
// animalChanged

}
