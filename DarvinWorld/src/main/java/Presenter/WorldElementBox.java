package Presenter;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import WorldElement.WorldElement;

public class WorldElementBox {
    private final VBox vBox;
    private final int height = 20;
    private final int width = 20;

    public WorldElementBox(WorldElement worldElement) {
        ImageView view = new ImageView(worldElement.getPicture());
        view.setFitHeight(height);
        view.setFitWidth(width);
        //Label label = new Label(worldElement.getPosition().toString());
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(view);
        //vBox.getChildren().add(label);
    }
    public VBox getvBox(){ return vBox;};
}

