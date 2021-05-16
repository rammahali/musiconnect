package Home.Controllers;

import Home.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Home.Helper.executeQuery;

public class userPlaylists implements Initializable {
    String email = "";
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

    }

    @FXML
    public void getUserData(String email) {
        setEmail(email);
        String query = "SELECT * FROM app_user WHERE email = ?";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = executeQuery(statement, query);
            resultSet.next();
            String username = resultSet.getString("name");
            String imagePath = resultSet.getString("picture");
            displayName.setText(username);
//             Image pic = new Image(imagePath,false);
//             profilePicture.setFill(new ImagePattern(pic));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @FXML
    private void logoutApp() throws IOException {
        App.navigateTo("login");
        App.showSuccessMessage("You are now logged out", "");
    }


    public void setEmail(String email) {
        System.out.println("EMAIL IS : " + email);
        this.email = email;
        System.out.println("email to use is : " + this.email);
    }
}
