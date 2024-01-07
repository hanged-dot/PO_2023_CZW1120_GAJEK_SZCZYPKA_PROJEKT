package agh.ics.oop.presenter;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.awt.*;

public class NewSimulation {

    public static void display(String title, String message){
        Stage window = new Stage();
        window.show();

        window.setTitle(title);
        window.setMinWidth(200);

        Label label = new Label(message);
        Button closeButton = new Button("Close the window");
        closeButton.setOnAction(e -> window.close());

        VBox layout= new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);

    }
}
