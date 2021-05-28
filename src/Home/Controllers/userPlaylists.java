package Home.Controllers;

import Home.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static Home.Helper.getUserData;

public class userPlaylists implements Initializable {
    @FXML
    private Circle profilePicture;

    @FXML
    private Text displayName;

    @FXML
    private ListView<String> popList;

    @FXML
    private ListView<String> jazzList;

    @FXML
    private ListView<String> classicList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getUserData(displayName, profilePicture);
    }

    @FXML
    private void logoutApp() throws IOException {
        App.navigateTo("login");
        App.showSuccessMessage("You are now logged out", "");
    }


}
