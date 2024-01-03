package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

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

    @FXML
    private TextField textField;
    @FXML
    private Button button = new Button("Start");
    public void setWorldMap (WorldMap map){ mapa=map;}

    public void drawMap(WorldMap worldMap, String message){
        infoLabel2.setText(message);
        //infoLabel.setText(worldMap.toString());
        //<Label fx:id="infoLabel" text="All animals will be living here!" textAlignment="CENTER"/>
        clearGrid();
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        mapGrid.setGridLinesVisible(true);
        //mapGrid.setVgap(50);
        //mapGrid.setHgap(50);
       // GridPane.setHalignment(label, HPos.CENTER);
        Boundary bounds = worldMap.getCurrentBounds();



        Label labelxy = new Label("y//x");
        labelxy.setMinSize(CELL_WIDTH,CELL_HEIGHT);
        labelxy.setAlignment(Pos.CENTER);
        //GridPane.setHalignment(labelxy, HPos.CENTER);
        mapGrid.add(labelxy,0,0);

        for (int x=1;x<=abs(bounds.upperRight().getX()-bounds.lowerLeft().getX())+1;x++){
            Label label2 = new Label(Integer.toString(bounds.lowerLeft().getX()+x-1));
            label2.setMinSize(CELL_WIDTH,CELL_HEIGHT);
            label2.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label2, HPos.CENTER);

            mapGrid.add(label2,x,0);
        }
        for (int y=1;y<=abs(bounds.upperRight().getY()-bounds.lowerLeft().getY())+1;y++){
            Label label2 = new Label(Integer.toString(bounds.upperRight().getY()-y+1));
            label2.setMinSize(CELL_WIDTH,CELL_HEIGHT);
            label2.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label2, HPos.CENTER);

            mapGrid.add(label2,0,y);
        }

        for (int x=1;x<=abs(bounds.upperRight().getX()-bounds.lowerLeft().getX())+1;x++){
            for (int y=1;y<=abs(bounds.upperRight().getY()-bounds.lowerLeft().getY())+1;y++){
                if (worldMap.isOccupied(new Vector2d(bounds.lowerLeft().getX()+x-1,bounds.upperRight().getY()-y+1))) {
                    Label label3 = new Label(worldMap.objectAt(new Vector2d(bounds.lowerLeft().getX()+x-1,bounds.upperRight().getY()-y+1)).toString());
                    label3.setMinSize(CELL_WIDTH, CELL_HEIGHT);
                    label3.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(label3, HPos.CENTER);

                    mapGrid.add(label3, x,y);
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

    public void onSimulationStartClicked(){
        List<Vector2d> positions1 = List.of(new Vector2d(2,2), new Vector2d(3,4));
        ArrayList<Simulation> simulations= new ArrayList<>();
        List<MoveDirection> directions;
        try {directions = OptionsParser.parse(textField.getText().split(" "));}
        catch(IllegalArgumentException e){return;}
        TunnelMap recmap = new TunnelMap();
        SimulationPresenter presenter = this;
        recmap.addObserver(presenter);

        simulations.add(new Simulation(positions1, directions,recmap ));
        new SimulationEngine(simulations).runAsyncInThreadPool();

    }
}
