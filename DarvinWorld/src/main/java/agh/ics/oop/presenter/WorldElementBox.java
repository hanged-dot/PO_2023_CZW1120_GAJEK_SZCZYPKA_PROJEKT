package agh.ics.oop.presenter;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class WorldElementBox extends Image {
    private final VBox vBox;
    private final int height = 20;
    private final int width = 20;

    public WorldElementBox(WorldElement worldElement) {
        ImageView view = new ImageView(worldElement.getPicture().toString());
        view.setFitHeight(height);
        view.setFitWidth(width);
        //Label label = new Label(worldElement.getPosition().toString());
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(view);
        //vBox.getChildren().add(label);
    }
    public VBox getvBox(){ return vBox;};

    @Override
    public int getWidth(ImageObserver observer) {
        return width;
    }

    @Override
    public int getHeight(ImageObserver observer) {
        return height;
    }

    @Override
    public ImageProducer getSource() {
        return null;
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }

    @Override
    public Object getProperty(String name, ImageObserver observer) {
        return null;
    }
}


