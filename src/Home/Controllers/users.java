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
import javafx.stage.FileChooser;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import static Home.Helper.*;

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
    private ChoiceBox<String> country;

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
        importCountries();
    }


    @FXML
    private void importUsers() {
        final ObservableList<User> data = FXCollections.observableArrayList();
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colPath.setCellValueFactory(new PropertyValueFactory<>("picture"));
        String query = "SELECT * FROM app_user ORDER BY id ASC";
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
    private void createUser() throws SQLException {
        if (email.getText() != "" && email.getText() != null) {
            String emailQuery = "SELECT password_hash FROM app_user WHERE email = ?";

            PreparedStatement emailStatement = App.connection.prepareStatement(emailQuery);
            emailStatement.setString(1, email.getText());
            ResultSet emailResult = executeQuery(emailStatement, emailQuery);
            if (emailResult.next()) {
                App.showError("this email already exists", "please change the email");
                return;
            }
            HashMap<String, Integer> countries = createCountries();
            String query = "INSERT INTO app_user(name, email, password_hash, country_id, picture) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setString(1, name.getText());
            statement.setString(2, email.getText());
            statement.setString(3, getHashedPassword(password.getText()));
            statement.setInt(4, countries.get(country.getValue()));
            statement.setString(5, picturePath.getText());
            execute(statement, query);
            importUsers();
            App.showSuccessMessage("user " + name.getText() + " has been created", "");
            clear();
        }
    }

    @FXML
    private void deleteUser() throws SQLException {
        if (email.getText() != null && email.getText() != "") {
            String query = "DELETE FROM  app_user WHERE email = ?";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setString(1, email.getText());
            execute(statement, query);
            App.showSuccessMessage("user " + name.getText() + " has been deleted", "");
            importUsers();
            clear();
        }
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

    @FXML
    private void updateUser() throws SQLException {
        if (email.getText() != "" && email.getText() != null) {
            User user = usersTable.getSelectionModel().getSelectedItem();
            HashMap<String, Integer> countries = createCountries();
            String query = "UPDATE  app_user SET name =?,email =?,password_hash =?,country_id =?,picture = ? WHERE email = ?";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setString(1, name.getText());
            statement.setString(2, email.getText());
            statement.setString(3, getHashedPassword(password.getText()));
            statement.setInt(4, countries.get(country.getValue()));
            statement.setString(5, picturePath.getText());
            statement.setString(6, email.getText());
            execute(statement, query);
            importUsers();
            App.showSuccessMessage("user " + name.getText() + " has been updated", "");
            clear();
        }
    }

    @FXML private void onRowClickAction() throws SQLException {
        User user =usersTable.getSelectionModel().getSelectedItem();
        if(user!=null){
            name.setText(user.getName());
            email.setText(user.getEmail());
            picturePath.setText(user.getPicture());
            String query = "SELECT * FROM app_user WHERE email = ?";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setString(1, email.getText());
            ResultSet resultSet = executeQuery(statement, query);
            resultSet.next();
            int countryID = resultSet.getInt("country_id");
            String country = getCountry(countryID);
            this.country.getSelectionModel().select(country);
        }
    }
    private void clear(){
        name.setText("");
        email.setText("");
        password.setText("");
        picturePath.setText("");
        country.getSelectionModel().select(0);
    }
    @FXML
    private void selectProfileImage() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            picturePath.setText(selectedFile.getPath());
        }
    }
    @FXML
    private void importCountries() {
        ObservableList<String> countries = FXCollections.observableArrayList();
        countries.add("Select country");
        String[] countryNames = Locale.getISOCountries();
        for (String country : countryNames) {
            Locale obj = new Locale("en", country);
            countries.add(obj.getDisplayCountry());
        }
        countries = countries.sorted();
        country.setItems(countries);
        country.getSelectionModel().select(0);
    }


    private String getCountry(int id) throws SQLException {
        String query = "SELECT * FROM country WHERE id = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = executeQuery(statement, query);
        resultSet.next();
        return resultSet.getString("name");
    }


    private void instantiateAdmin() {
        String query = "SELECT * FROM app_user WHERE email = ?";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setString(1, App.getUserEmail());
            ResultSet resultSet = executeQuery(statement, query);
            resultSet.next();
            String username = resultSet.getString("name");
            String imagePath = resultSet.getString("picture");
            displayName.setText(username);
            File imageFile = new File(imagePath);
            String imageLocation = imageFile.toURI().toString();
            Image pic = new Image(imageLocation, false);
            profilePicture.setFill(new ImagePattern(pic));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
