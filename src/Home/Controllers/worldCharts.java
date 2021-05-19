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
import java.util.ResourceBundle;

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
    TableView<Song> worldList;

    @FXML
    private TableColumn<Song, Integer> worldName;

    @FXML
    private TableColumn<Song, Integer> worldStreams;

    @FXML
    TableView<Song> countryList;

    @FXML
    private TableColumn<Song, Integer> countryName;

    @FXML
    private TableColumn<Song, Integer> countryStreams;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.populateUserNavigator(navigator, "Charts");
        populateCharts();
    }

    private void populateCountriesChart() {
        final ObservableList<Song> data = FXCollections.observableArrayList();
        worldName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        worldStreams.setCellValueFactory(new PropertyValueFactory<>("Streams"));
        String query = """
                select song.name as name, streams.streams as streams
                from song
                         join streams on song.id = streams.song_id where streams.country_id = ?
                ORDER BY streams DESC;""";
    }

    private void populateWorldChart() {

    }

    private void populateCharts() {
        populateWorldChart();
        populateCountriesChart();
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
