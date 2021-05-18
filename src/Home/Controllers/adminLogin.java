package Home.Controllers;
import Home.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Home.Helper.executeQuery;
import static Home.Helper.getHashedPassword;


public class adminLogin implements Initializable {
     @FXML TextField email;
    @FXML TextField password;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    @FXML
    private void onLoginClick() {
        String query = "SELECT * FROM app_user WHERE email = ?";

        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setString(1, email.getText());
            ResultSet resultSet = executeQuery(statement);
            if (!resultSet.next()) {
                App.showError("Incorrect email or password", "please try again");
                return;
            }
             boolean isAdmin = resultSet.getBoolean("is_Admin");
            if (!isAdmin) {
                App.showError("Only admins are allowed here", " please contact an admin for promotion");
                return;
            }
            String passwordHash = resultSet.getString("password_hash");
            if (!getHashedPassword(password.getText()).equals(passwordHash)) {
                App.showError("Incorrect email or password", "please try again");
            } else {
                App.setUserEmail(email.getText());
                try {
                    App.navigateTo("adminPanel");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                App.showSuccessMessage("Successful login", "Welcome back");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML private void navigateToCreateAccount() throws IOException {
        App.navigateTo("createAccount");
    }
    @FXML private void navigateToUserLogin() throws IOException {
        App.navigateTo("login");
    }

    @FXML private void exitApp()  {
        App.close();
    }

}

