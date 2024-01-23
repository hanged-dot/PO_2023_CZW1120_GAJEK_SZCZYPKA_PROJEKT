package Records;


import WordMap.Vector2d;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public record ConfigurationElement(
        @FXML Text text,
        @FXML TextField textField,
        Vector2d limits
        ) {
}
