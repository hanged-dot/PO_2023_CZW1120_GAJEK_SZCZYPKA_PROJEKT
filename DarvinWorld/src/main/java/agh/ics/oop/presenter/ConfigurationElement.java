package agh.ics.oop.presenter;

import agh.ics.oop.model.Vector2d;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Vector;

public record ConfigurationElement(
        @FXML Text text,
        @FXML TextField textField,
        Vector2d limits
        ) {
}
