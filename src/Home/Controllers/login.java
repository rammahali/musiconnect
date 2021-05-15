package Home.Controllers;

import Home.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class login implements Initializable {
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private Button go;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML private void onCreateAccountClick() throws IOException {
        App.navigateTo("createAccount");
    }
}
