package Home.Controllers;
import Home.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;



public class adminLogin implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML private void navigateToCreateAccount() throws IOException {
        App.navigateTo("createAccount");
    }
    @FXML private void navigateToUserLogin() throws IOException {
        App.navigateTo("login");
    }

    @FXML private void exitApp() throws IOException {
        App.close();
    }

}

