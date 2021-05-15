package Home.Controllers;

import Home.App;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class createAccount implements Initializable {
    @FXML private TextField fullName;
    @FXML private TextField email;
    @FXML private TextField country;
    @FXML private TextField profilePath;
    @FXML private PasswordField password;
    @FXML private Button createAccount;
    @FXML private  Button login;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
  @FXML private void onLoginClick() throws IOException {
        App.navigateTo("login");
    }
}
