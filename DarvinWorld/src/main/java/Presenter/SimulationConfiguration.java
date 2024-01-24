package Presenter;

import Records.AnimalProperties;
import Records.ConfigurationElement;
import Records.MapProperties;
import Records.SimulationProperties;
import WordMap.Vector2d;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimulationConfiguration {
    @FXML private Button closeButton;

    @FXML private Text mapHeightText;
    @FXML private TextField mapHeightTextField;

    @FXML private Text mapWidthText;
    @FXML private TextField mapWidthTextField;

    @FXML private Text tunnelCountText;
    @FXML private TextField tunnelCountTextField;

    @FXML private Text startPlantCountText;
    @FXML private TextField startPlantCountTextField;

    @FXML private Text plantsPerDayText;
    @FXML private TextField plantsPerDayTextField;

    @FXML private Text energyPlantText;
    @FXML private TextField energyPlantTextField;

    @FXML private Text startAnimalCountText;
    @FXML private TextField startAnimalCountTextField;

    @FXML private Text startAnimalEnergyText;
    @FXML private TextField startAnimalEnergyTextField;

    @FXML private Text minProcreateEnergyText;
    @FXML private TextField minProcreateEnergyTextField;

    @FXML private Text procreateEnergyText;
    @FXML private TextField procreateEnergyTextField;

    @FXML private Text minMutationText;
    @FXML private TextField minMutationTextField;

    @FXML private Text maxMutationText;
    @FXML private TextField maxMutationTextField;

    @FXML private Text genomeLengthText;
    @FXML private TextField genomeLengthTextField;

    @FXML private CheckBox chooseMapWithTunnelsBox;
    @FXML private CheckBox chooseWithLightMutationCorrectBox;
    @FXML private CheckBox saveStatisticsCheckBox;
    private SimulationStart simulationStart;

    private ConfigurationElement mapHeight;
    private ConfigurationElement mapWidth;
    private ConfigurationElement tunnelCount;
    private ConfigurationElement startPlantCount;
    private ConfigurationElement plantsPerDay;
    private ConfigurationElement energyPlant;
    private ConfigurationElement startAnimalCount;
    private ConfigurationElement startAnimalEnergy;
    private ConfigurationElement minProcreateEnergy;
    private ConfigurationElement procreateEnergy;
    private ConfigurationElement minMutation;
    private ConfigurationElement maxMutation;
    private ConfigurationElement genomeLength;

    private ArrayList<ConfigurationElement> configurationElementArrayList;




    public void initialize() {

        simulationStart = new SimulationStart();
        configurationElementArrayList = new ArrayList<>();

//        All texts needed:
        mapHeight = new ConfigurationElement(mapHeightText, mapHeightTextField, new Vector2d(5, 1000));

        mapWidth = new ConfigurationElement(mapWidthText,mapWidthTextField,new Vector2d(5, 1000));

        tunnelCount = new ConfigurationElement(tunnelCountText,tunnelCountTextField,new Vector2d(1, 40));
        tunnelCount.textField().setDisable(true);

        startPlantCount = new ConfigurationElement(startPlantCountText,startPlantCountTextField,new Vector2d(0, 30));

        plantsPerDay = new ConfigurationElement(plantsPerDayText,plantsPerDayTextField,new Vector2d(1, 60));

        energyPlant = new ConfigurationElement(energyPlantText,energyPlantTextField,new Vector2d(1, 20));

        startAnimalCount = new ConfigurationElement(startAnimalCountText,startAnimalCountTextField, new Vector2d(1, 100));

        startAnimalEnergy = new ConfigurationElement(startAnimalEnergyText,startAnimalEnergyTextField, new Vector2d(1, 100));

        minProcreateEnergy = new ConfigurationElement(minProcreateEnergyText,minProcreateEnergyTextField, new Vector2d(1, 20));

        procreateEnergy = new ConfigurationElement(procreateEnergyText,procreateEnergyTextField,new  Vector2d(1, 10));

        minMutation = new ConfigurationElement(minMutationText,minMutationTextField,new Vector2d(0, 10));

        maxMutation = new ConfigurationElement(maxMutationText,maxMutationTextField,new Vector2d(0, 80));

        genomeLength = new ConfigurationElement(genomeLengthText,genomeLengthTextField,new Vector2d(2, 100));

        this.configurationElementArrayList.add(mapHeight);
        this.configurationElementArrayList.add(mapWidth);
        this.configurationElementArrayList.add(tunnelCount);
        this.configurationElementArrayList.add(startPlantCount);
        this.configurationElementArrayList.add(plantsPerDay);
        this.configurationElementArrayList.add(energyPlant);
        this.configurationElementArrayList.add(startAnimalCount);
        this.configurationElementArrayList.add(startAnimalEnergy);
        this.configurationElementArrayList.add(minProcreateEnergy);
        this.configurationElementArrayList.add(procreateEnergy);
        this.configurationElementArrayList.add(minMutation);
        this.configurationElementArrayList.add(maxMutation);
        this.configurationElementArrayList.add(genomeLength);


        chooseMapWithTunnelsBox.selectedProperty().addListener((o, oldValue, newValue) -> {
            if (newValue) {
                tunnelCount.textField().setDisable(false);
            } else {
                tunnelCount.textField().setDisable(true);
            }
        });
    }


    private boolean checkConfigurationConstraints(){

        for(ConfigurationElement configurationElement: configurationElementArrayList){
            if (!isCorrect(configurationElement)){
                return false;
            }
        }

        if (Integer.parseInt(minMutation.textField().getText()) > Integer.parseInt(maxMutation.textField().getText())){
            AlertBox.display("Incorrect Input", "Minimal Mutation Count cannot be higher than Maximal Mutation Count.");
            return false;
        }
        int area = Integer.parseInt(mapHeight.textField().getText()) * Integer.parseInt(mapWidth.textField().getText());

        if (Integer.parseInt(tunnelCount.textField().getText()) >= area){
            AlertBox.display("Incorrect Input", "Too many tunnels.");
            return false;
        }
        if (Integer.parseInt(startPlantCount.textField().getText())>=area){
            AlertBox.display("Incorrect Input","Too many plants.");
        }
        if (Integer.parseInt(plantsPerDay.textField().getText())>=area){
            AlertBox.display("Incorrect Input","Too many plants per day.");
        }
        if (Integer.parseInt(startAnimalCount.textField().getText())>=area){
            AlertBox.display("Incorrect Input","Too many start animals.");
        }

        return true;
    }

    private boolean isCorrect(ConfigurationElement configurationElement) {

        try {
            int value = Integer.parseInt(configurationElement.textField().getText());
            if (value >= configurationElement.limits().getX() && value <= configurationElement.limits().getY()) {
                return true;
            } else {
                AlertBox.display("Incorrect input",
                        configurationElement.text().getText() + "value shoud be a natural number between %d and %d.".formatted(configurationElement.limits().getX(), configurationElement.limits().getY()));
                return false;
            }
        } catch (NumberFormatException e) {
            AlertBox.display("Incorrect input",
                    configurationElement.text().getText() + "value shoud be a natural number between %d and %d.".formatted(configurationElement.limits().getX(), configurationElement.limits().getY()));
            return false;
        }

    }

    private SimulationProperties wrapProperties(){

        MapProperties mapProperties = new MapProperties(
                Integer.parseInt(mapWidth.textField().getText()),
                Integer.parseInt(mapHeight.textField().getText()),
                Integer.parseInt(startPlantCount.textField().getText()),
                Integer.parseInt(plantsPerDay.textField().getText()),
                Integer.parseInt(startAnimalCount.textField().getText()),
                chooseMapWithTunnelsBox.isSelected(),
                Integer.parseInt(tunnelCount.textField().getText())
        );

        AnimalProperties animalProperties = new AnimalProperties(
                Integer.parseInt(startAnimalEnergy.textField().getText()),
                Integer.parseInt(energyPlant.textField().getText()),
                Integer.parseInt(minProcreateEnergy.textField().getText()),
                Integer.parseInt(procreateEnergy.textField().getText()),
                Integer.parseInt(minMutation.textField().getText()),
                Integer.parseInt(maxMutation.textField().getText()),
                Integer.parseInt(genomeLength.textField().getText()),
                chooseWithLightMutationCorrectBox.isSelected()
        );

        return new SimulationProperties(mapProperties, animalProperties, chooseMapWithTunnelsBox.isSelected(),
                chooseWithLightMutationCorrectBox.isSelected(), saveStatisticsCheckBox.isSelected());
    }

    public void onSimulationStartClicked(){

        if(checkConfigurationConstraints()){
            simulationStart.newSimulationStart(wrapProperties(), saveStatisticsCheckBox.isSelected());
        }
    }

    public void saveConfiguration(){
        if (checkConfigurationConstraints()){
            NameConfigurationBox.giveConfigurationName("New configuration name: ",
                    wrapProperties());
        }
    }

    public void chooseSavedConfiguration(){
        ConfigurationReader configurationReader = new ConfigurationReader();
        configurationReader.choosePredefinedSimulationProperties(this.simulationStart, saveStatisticsCheckBox.isSelected());
    }

    public void onCloseButtonClicked(){
        simulationStart.terminateAllSimulations();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}