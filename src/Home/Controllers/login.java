package Home.Controllers;

import Home.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
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
    private void onLoginClick() {
        String query = "SELECT password_hash FROM app_user WHERE email = ?";

        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setString(1, email.getText());
            ResultSet resultSet = executeQuery(statement, query);

            if (!resultSet.next()) {
                App.showError("Incorrect email or password", "please try again");
                return;
            }

            String passwordHash = resultSet.getString("password_hash");

            if (!getHashedPassword(password.getText()).equals(passwordHash)) {
                App.showError("Incorrect email or password", "please try again");
            } else {
                App.showSuccessMessage("Successful login", "You are now logged in");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            App.showError("Could not login", "Please contact your system administrator");
        }
    }
}
