package Home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
  double x,y;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXMLS/createAccount.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y= mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
           primaryStage.setX(mouseEvent.getScreenX()-x);
           primaryStage.setY(mouseEvent.getScreenY()-y);
        });
        primaryStage.setTitle("Musiconnect");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }



    public static void main(String[] args) {

        launch(args);
    }
}
