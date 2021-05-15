package Home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class App extends Application {
    double x,y;
    private static Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception{
        scene = new Scene(loadFXML("login"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        scene.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y= mouseEvent.getSceneY();
        });
        scene.setOnMouseDragged(mouseEvent -> {
            primaryStage.setX(mouseEvent.getScreenX()-x);
            primaryStage.setY(mouseEvent.getScreenY()-y);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void navigateTo(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLS/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
