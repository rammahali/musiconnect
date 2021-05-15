package Home.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
public class login implements Initializable {
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private Button go;
    @FXML private Button createAccount;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
