package Home.Controllers;

import Home.App;
import Home.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class userDashboard implements Initializable {
    @FXML
    Text displayName;
    @FXML
    Circle profilePicture;
    @FXML
    ChoiceBox<String> navigator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instantiateAdmin();
        populateNavigator();
    }


    private void instantiateAdmin() {
        Helper.getUserData(displayName, profilePicture);
    }

    @FXML
    private void logoutApp() throws IOException {
        App.navigateTo("login");
    }

    @FXML
    private void close() {
        App.close();
    }

    private void populateNavigator() {
        ObservableList<String> pages = FXCollections.observableArrayList();
        pages.addAll("Dashboard", "Library", "Explore", "Albums", "Charts");
        navigator.setItems(pages);
        navigator.getSelectionModel().select(0);
    }

    @FXML
    private void navigate() {
        Helper.navigateUser(navigator);
    }

    @FXML
    private void gotoLibrary() {
        try {
            App.navigateTo("library");
        } catch (IOException e) {
            App.showInfoMessage("Section not found","we couldn't find this section...");
        }
    }

    @FXML
    private void gotoExplore() {
        try {
            App.navigateTo("explore");
        } catch (IOException e) {
            App.showInfoMessage("Section not found","we couldn't find this section...");
        }
    }

    @FXML
    private void gotoAlbums() {
        try {
            App.navigateTo("userAlbums");
        } catch (IOException e) {
            App.showInfoMessage("Section not found", "we couldn't find this section...");
        }
    }

    @FXML
    private void gotoCharts() {
        try {
            App.navigateTo("charts");
        } catch (IOException e) {
            App.showInfoMessage("Section not found", "we couldn't find this section...");
        }
    }

}
