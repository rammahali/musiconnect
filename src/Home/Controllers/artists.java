package Home.Controllers;

import Home.App;
import Home.Helper;
import Home.Modules.Artist;
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
import java.util.HashMap;
import java.util.ResourceBundle;

import static Home.Helper.*;

public class artists implements Initializable {
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
    private TableView<Artist> ArtistsTable;

    @FXML
    private TableColumn<Artist, Integer> ID;

    @FXML
    private TableColumn<Artist, String> colName;

    @FXML
    private TableColumn<Artist, String> colCountry;

    @FXML
    private TableColumn<Artist, String> colPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getUserData(displayName, profilePicture);
        populateNavigator();
        importArtists();
    }


    @FXML
    private void importArtists() {
        System.out.println("IMPORT ARTISTS FUNC CALLED");
        final ObservableList<Artist> data = FXCollections.observableArrayList();
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colPath.setCellValueFactory(new PropertyValueFactory<>("picture"));
        String query = "SELECT * FROM artist";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String picture = resultSet.getString("picture");
                int countryID = resultSet.getInt("country_id");
                String country = getCountry(countryID);
                data.add(new Artist(id, name, country, picture));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ArtistsTable.setItems(data);
    }

    @FXML
    private void createArtist() throws SQLException {
        if (!email.getText().equals("") && email.getText() != null) {
            HashMap<String, Integer> countries = createCountries();
            String query = "INSERT INTO artist(name, country_id, picture) VALUES (?, ?, ?)";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setString(1, name.getText());
            statement.setInt(2, countries.get(country.getValue()));
            statement.setString(3, picturePath.getText());
            execute(statement);
            importArtists();
            App.showSuccessMessage("Artist " + name.getText() + " has been created", "");
            clear();
        }
    }

    private void clear() {
        name.setText("");
        email.setText("");
        password.setText("");
        picturePath.setText("");
        country.getSelectionModel().select(0);
    }


    @FXML
    private void updateArtist() {
        Artist artist = ArtistsTable.getSelectionModel().getSelectedItem();
        App.showInfoMessage(artist.getName(), "");
    }

    @FXML
    private void deleteArtist() {
    }

    private void onRowClickAction() {

    }

    private String getCountry(int id) throws SQLException {
        String query = "SELECT * FROM country WHERE id = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = executeQuery(statement);
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
        navigator.getSelectionModel().select(2);
    }

    @FXML
    private void navigate() {
        Helper.navigate(navigator);
    }
}
