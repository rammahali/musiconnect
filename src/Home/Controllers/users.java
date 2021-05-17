package Home.Controllers;

import Home.App;
import Home.Helper;
import Home.Modules.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Home.Helper.executeQuery;
import static Home.Helper.instantiateAdmin;

public class users implements Initializable {
    @FXML
    Text displayName;
    @FXML
    Circle profilePicture;
    @FXML
    ChoiceBox<String> navigator;
    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private ChoiceBox<String> Country;

    @FXML
    private TextField picturePath;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> ID;

    @FXML
    private TableColumn<User, String> colName;

    @FXML
    private TableColumn<User, String> colEmail;

    @FXML
    private TableColumn<User, String> colCountry;

    @FXML
    private TableColumn<User, String> colPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instantiateAdmin(displayName, profilePicture);
        populateNavigator();
        importUsers();
    }


    @FXML
    private void importUsers() {
        System.out.println("IMPORT USERS FUNC CALLED");
        final ObservableList<User> data = FXCollections.observableArrayList();
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        System.out.println("EMAIL COLUMN SET");

        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colPath.setCellValueFactory(new PropertyValueFactory<>("picture"));
        String query = "SELECT * FROM app_user ";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            ResultSet resultSet = executeQuery(statement, query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String picture = resultSet.getString("picture");
                int countryID = resultSet.getInt("country_id");
                String country = getCountry(countryID);
                data.add(new User(id, name, email, country, picture));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        usersTable.setItems(data);
    }

    @FXML
    private void addUser() {
    }

    @FXML
    private void deleteUser() {
    }

    @FXML
    private void editUser() {
        User user = usersTable.getSelectionModel().getSelectedItem();
        App.showInfoMessage(user.getName(), "");
    }

    private void onRowClickAction() {

    }

    private String getCountry(int id) throws SQLException {
        String query = "SELECT * FROM country WHERE id = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = executeQuery(statement, query);
        resultSet.next();
        return resultSet.getString("name");
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
        pages.addAll("Dashboard", "Users", "Artists", "Albums", "Songs");
        navigator.setItems(pages);
        navigator.getSelectionModel().select(1);
    }

    @FXML
    private void navigate() {
        Helper.navigate(navigator);
    }
}
