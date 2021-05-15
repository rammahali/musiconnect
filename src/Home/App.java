package Home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;

import static Home.Helper.setupDB;

public class App extends Application {
    double x,y;
    public static Connection connection;
    private static Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception{
        String url = "jdbc:postgresql://localhost:5432/musiconnect?user=postgres&password=postgres";
        connection = setupDB(url);
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
    public static void showError(String header,String message){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXMLS/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
