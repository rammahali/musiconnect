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
import javafx.stage.FileChooser;

import java.io.File;
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
    private TextField ID;

    @FXML
    private TextField name;

    @FXML
    private ChoiceBox<String> country;

    @FXML
    private TextField picturePath;

    @FXML
    private TableView<Artist> artistsTable;

    @FXML
    private TableColumn<Artist, Integer> colID;

    @FXML
    private TableColumn<Artist, String> colName;

    @FXML
    private TableColumn<Artist, String> colCountry;

    @FXML
    private TableColumn<Artist, String> colPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getUserData(displayName, profilePicture);
        populateNavigator(navigator);
        importArtists();
        importCountries(country);
    }


    @FXML
    private void importArtists() {
        final ObservableList<Artist> data = FXCollections.observableArrayList();
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colPath.setCellValueFactory(new PropertyValueFactory<>("picture"));
        String query = "SELECT * FROM artist ORDER BY id";
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
        artistsTable.setItems(data);
    }

    @FXML
    private void createArtist() throws SQLException {
        if (!ID.getText().equals("") && ID.getText() != null) {
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
        ID.setText("");
        name.setText("");
        picturePath.setText("");
        country.getSelectionModel().select(0);
    }


    @FXML
    private void updateArtist() throws SQLException {
        if (!ID.getText().equals("") && ID.getText() != null) {
            HashMap<String, Integer> countries = createCountries();

            String query = "UPDATE artist SET name = ?, country_id = ?, picture = ? WHERE id = ?";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setString(1, name.getText());
            statement.setInt(2, countries.get(country.getValue()));
            statement.setString(3, picturePath.getText());
            statement.setInt(4, Integer.parseInt(ID.getText()));

            if (execute(statement) != 0) {
                importArtists();
                App.showSuccessMessage("Artist " + name.getText() + " has been updated", "");
            } else {
                App.showError("Artist does not exist", "");
            }
            clear();
        }
    }

    @FXML
    private void deleteArtist() {
    }

    @FXML
    private void onRowClickAction() throws SQLException {
        Artist artist = artistsTable.getSelectionModel().getSelectedItem();
        if (artist != null) {
            name.setText(artist.getName());
            ID.setText(String.valueOf(artist.getID()));
            picturePath.setText(artist.getPicture());
            String query = "SELECT * FROM artist WHERE id = ?";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setInt(1, artist.getID());
            ResultSet resultSet = executeQuery(statement);
            resultSet.next();
            int countryID = resultSet.getInt("country_id");
            String country = getCountry(countryID);
            this.country.getSelectionModel().select(country);
        }
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

    @FXML
    private void navigate() {
        Helper.navigate(navigator);
    }

    @FXML
    private void selectProfileImage() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            picturePath.setText(selectedFile.getPath());
        }
    }
}
