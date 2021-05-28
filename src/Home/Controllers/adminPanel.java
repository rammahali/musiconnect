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

public class adminPanel implements Initializable {
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
        pages.addAll("Dashboard", "Users", "Artists", "Albums", "Songs");
        navigator.setItems(pages);
        navigator.getSelectionModel().select(0);
    }

    @FXML
    private void navigate() {
        Helper.navigateAdmin(navigator);
    }

    @FXML
    private void gotoUsers() {
        try {
            App.navigateTo("users");
        } catch (IOException e) {
            App.showInfoMessage("Section not found","we couldn't find this section...");
        }
    }

    @FXML
    private void gotoArtists() {
        try {
            App.navigateTo("artists");
        } catch (IOException e) {
            App.showInfoMessage("Section not found","we couldn't find this section...");
        }
    }

    @FXML
    private void gotoAlbums() {
        try {
            App.navigateTo("albums");
        } catch (IOException e) {
            App.showInfoMessage("Section not found", "we couldn't find this section...");
        }
    }

    @FXML
    private void gotoSongs() {
        try {
            App.navigateTo("songs");
        } catch (IOException e) {
            App.showInfoMessage("Section not found", "we couldn't find this section...");
        }
    }

}
