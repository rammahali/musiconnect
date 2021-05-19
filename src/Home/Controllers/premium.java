package Home.Controllers;

import Home.App;
import Home.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static Home.Helper.execute;
import static Home.Helper.executeQuery;

public class premium implements Initializable {
    @FXML
    Text displayName;
    @FXML
    Circle profilePicture;
    @FXML
    ChoiceBox<String> navigator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getUserData(displayName,profilePicture);
        populateNavigator();
    }


    private void instantiateAdmin() {
        Helper.getUserData(displayName, profilePicture);
    }

    @FXML
    private void logoutApp() throws IOException {
        App.navigateTo("login");
    }

    @FXML
    private void close() {
        App.close();
    }

    private void populateNavigator() {
        ObservableList<String> pages = FXCollections.observableArrayList();
        pages.addAll("Dashboard", "Library", "Explore", "Albums", "Charts");
        navigator.setItems(pages);
        navigator.getSelectionModel().select(0);
    }

    @FXML private  void buy() throws SQLException, IOException {
        int user_id = getUserID(App.getUserEmail()) ;
        java.util.Date input = new java.util.Date();
        LocalDate date = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(input));
        String premQuery = "SELECT * FROM subscription WHERE user_id = ?";
        PreparedStatement premStatement = App.connection.prepareStatement(premQuery);
        premStatement.setInt(1, user_id);
        ResultSet premResult = executeQuery(premStatement);
        if(premResult.next())
        {
            App.showInfoMessage("Already subscribed","");
            App.navigateTo("userDashboard");
            return;
        }
        String query = "INSERT INTO subscription (user_id, last_payment) VALUES (?, ?)";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setInt(1, user_id);
        statement.setDate(2, Date.valueOf(date));
        execute(statement);
        App.showSuccessMessage("You are now subscribed","Enjoy your subscription ! cheers");
        App.navigateTo("userDashboard");
    }

    private int getUserID(String email) throws SQLException {
        String query = "SELECT * FROM app_user WHERE email = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getInt("id");
    }
    @FXML
    private void navigate() {
        Helper.navigateUser(navigator);
    }
}
