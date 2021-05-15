package Home.Controllers;

import Home.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static Home.Helper.execute;
import static Home.Helper.getHashedPassword;

public class createAccount implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField country;
    @FXML
    private TextField profilePath;
    @FXML
    private PasswordField password;
    @FXML
    private Button createAccount;
    @FXML
    private Button login;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void onLoginMenuClick() throws IOException {
        App.navigateTo("login");
    }


    @FXML
    private void onCreateAccountClick() {
        // TODO: change country to country code, not 1
        String query = String.format("INSERT INTO app_user(name, email, password_hash, country_id, picture) VALUES ('%s', '%s', '%s', %d, '%s')",
                name.getText(), email.getText(), getHashedPassword(password.getText()), 1, profilePath.getText());

        execute(query);

    }

}
