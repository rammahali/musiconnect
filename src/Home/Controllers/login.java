package Home.Controllers;

import Home.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Home.Helper.executeQuery;
import static Home.Helper.getHashedPassword;

public class login implements Initializable {
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button go;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void onCreateAccountMenuClick() throws IOException {
        App.navigateTo("createAccount");
    }

    @FXML
    private void onLoginClick() throws SQLException {
        String query = String.format("SELECT password_hash FROM app_user WHERE email = '%s'", email.getText());
        ResultSet resultSet = executeQuery(query);

        if (!resultSet.next()) {
            App.showError("Incorrect email or password", "");
            return;
        }

        String passwordHash = resultSet.getString("password_hash");
        if (!getHashedPassword(password.getText()).equals(passwordHash)) {
            App.showError("Incorrect email or password", "");
        } else {
            System.out.println("Successful login");
        }
    }
}
