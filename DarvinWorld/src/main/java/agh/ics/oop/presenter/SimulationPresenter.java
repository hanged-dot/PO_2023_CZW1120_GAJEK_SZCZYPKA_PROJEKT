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


//    @FXML
 //   private Button button = new Button("Start");

    public void setWorldMap (WorldMap map){

        Stage window = new Stage();
        window.setTitle("Map "+map.getID());
        window.setMinWidth(150);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(closeButton);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();

        this.mapa=map;

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

        Platform.runLater(() -> {
            drawMap(worldMap, message);
        });
    }
// animalChanged



}
