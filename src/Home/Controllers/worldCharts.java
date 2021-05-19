package Home.Controllers;

import Home.App;
import Home.Helper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class worldCharts implements Initializable {
    @FXML
    Text displayName;
    @FXML
    Circle profilePicture;
    @FXML
    ListView<String> songsListView;
    @FXML
    ChoiceBox<String> navigator;

    @FXML
    TableView<String> worldList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.populateUserNavigator(navigator, "Charts");
        populateCharts();
    }

    private void populateCharts() {

    }

    @FXML
    private void navigate() {
        Helper.navigateUser(navigator);
    }

    @FXML
    private void logoutApp() throws IOException {
        App.navigateTo("login");
    }

    @FXML
    private void close() {
        App.close();
    }

}
