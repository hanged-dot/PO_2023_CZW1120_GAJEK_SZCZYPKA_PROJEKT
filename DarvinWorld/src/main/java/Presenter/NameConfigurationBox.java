package Presenter;


import Records.SimulationProperties;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NameConfigurationBox {
    public static void giveConfigurationName(String message, SimulationProperties simulationProperties){

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(150);

        Label label = new Label(message);
        TextField nameField = new TextField();
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            if (!(nameField.getText() == null || nameField.getText().trim().isEmpty())){
                ConfigurationSaver.saveConfiguration(nameField.getText(), simulationProperties);
                window.close();
            } else {
                AlertBox.display("Wrong title","You have to select a name for your configuration.");
            }
        });
        Button abortButton = new Button("Cancel");
        abortButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, nameField, saveButton, abortButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }
}
