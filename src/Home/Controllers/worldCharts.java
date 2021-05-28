package Home.Controllers;

import Home.App;
import Home.Helper;
import Home.Modules.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class worldCharts implements Initializable {
    @FXML
    Text displayName;
    @FXML
    Circle profilePicture;
    @FXML
    ListView<String> songsListView;
    @FXML
    ChoiceBox<String> navigator;

    @FXML
    Text countryLabel;

    @FXML
    TableView<Song> worldList;
    @FXML
    private TableColumn<Song, Integer> worldOrder;
    @FXML
    private TableColumn<Song, Integer> worldName;

    @FXML
    private TableColumn<Song, Integer> worldStreams;

    @FXML
    TableView<Song> countryList;
    @FXML
    private TableColumn<Song, Integer> countryOrder;
    @FXML
    private TableColumn<Song, Integer> countryName;

    @FXML
    private TableColumn<Song, Integer> countryStreams;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getUserData(displayName, profilePicture);
        Helper.populateUserNavigator(navigator, "Charts");
        setCountry();
        populateCharts();
    }

    private void setCountry() {
        String query = "SELECT name FROM country WHERE id = ? ";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, App.getUserCountryId());
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                String userCountryName = resultSet.getString("name");
                countryLabel.setText("Top 10 " + userCountryName);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void populateCountriesChart() {
        final ObservableList<Song> data = FXCollections.observableArrayList();
        countryOrder.setCellValueFactory(new PropertyValueFactory<>("ID"));
        countryOrder.setCellValueFactory(new PropertyValueFactory<>("Order"));
        countryName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        countryStreams.setCellValueFactory(new PropertyValueFactory<>("Streams"));
        String query = """
                SELECT song.id AS id, song.name AS name, streams.streams AS streams
                FROM song
                         JOIN streams ON song.id = streams.song_id WHERE streams.country_id = ?
                ORDER BY streams DESC""";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, App.getUserCountryId());
            ResultSet resultSet = executeQuery(statement);
            int i = 1;
            while (resultSet.next()) {
                int songID = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int streams = resultSet.getInt("streams");
                data.add(new Song(i, songID, name, streams));
                i++;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        countryList.setItems(data);
    }

    private void populateWorldChart() {
        final ObservableList<Song> data = FXCollections.observableArrayList();
        worldOrder.setCellValueFactory(new PropertyValueFactory<>("ID"));
        worldOrder.setCellValueFactory(new PropertyValueFactory<>("Order"));
        worldName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        worldStreams.setCellValueFactory(new PropertyValueFactory<>("Streams"));
        String query = """
                SELECT song.id AS id, song.name, SUM(streams.streams) AS streams
                FROM song
                         JOIN streams ON song.id = streams.song_id
                GROUP BY song.name, song.id ORDER BY streams DESC""";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            ResultSet resultSet = executeQuery(statement);
            int i = 1;
            while (resultSet.next()) {
                int songID = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int streams = resultSet.getInt("streams");
                data.add(new Song(i, songID, name, streams));
                i++;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        worldList.setItems(data);
    }

    private void populateCharts() {
        populateWorldChart();
        populateCountriesChart();
    }

    @FXML
    private void onWorldRowClickAction() throws IOException {
        Song song = worldList.getSelectionModel().getSelectedItem();
        if (song != null) {
            App.setSongID(song.getID());
            App.navigateTo("selectedSong");
        }
    }

    @FXML
    private void onCountryRowClickAction() throws IOException {
        Song song = countryList.getSelectionModel().getSelectedItem();
        if (song != null) {
            App.setSongID(song.getID());
            App.navigateTo("selectedSong");
        }
    }

    @FXML
    private void navigate() {
        Helper.navigateUser(navigator);
    }

    @FXML
    private void logoutApp() throws IOException {
        App.navigateTo("login");
    }

    @FXML
    private void close() {
        App.close();
    }

}
