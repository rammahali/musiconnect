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

public class genreCharts implements Initializable {
    @FXML
    Text displayName;
    @FXML
    Circle profilePicture;
    @FXML
    ListView<String> songsListView;
    @FXML
    ChoiceBox<String> navigator;

    @FXML
    TableView<Song> popList;
    @FXML
    private TableColumn<Song, Integer> popOrder;
    @FXML
    private TableColumn<Song, Integer> popName;

    @FXML
    private TableColumn<Song, Integer> popStreams;


    @FXML
    TableView<Song> jazzList;
    @FXML
    private TableColumn<Song, Integer> jazzOrder;
    @FXML
    private TableColumn<Song, Integer> jazzName;

    @FXML
    private TableColumn<Song, Integer> jazzStreams;

    @FXML
    TableView<Song> classicList;
    @FXML
    private TableColumn<Song, Integer> classicOrder;
    @FXML
    private TableColumn<Song, Integer> classicName;

    @FXML
    private TableColumn<Song, Integer> classicStreams;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getUserData(displayName, profilePicture);
        Helper.populateUserNavigator(navigator, "Charts");
        populateCharts();
    }

    private void populateCharts() {
        populatePopChart();
        populateJazzChart();
        populateClassicChart();
    }

    private void populateClassicChart() {
        final ObservableList<Song> data = FXCollections.observableArrayList();
        classicOrder.setCellValueFactory(new PropertyValueFactory<>("Order"));
        classicName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        classicStreams.setCellValueFactory(new PropertyValueFactory<>("Streams"));
        String query = """
                select song.id as id, song.name as name, SUM(streams.streams) as streams
                from song
                         join streams on song.id = streams.song_id join album on album.id = song.album_id where album.category_id = ?
                group by song.name, song.id ORDER BY streams DESC""";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, 3);
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
        classicList.setItems(data);
    }

    private void populateJazzChart() {
        final ObservableList<Song> data = FXCollections.observableArrayList();
        jazzOrder.setCellValueFactory(new PropertyValueFactory<>("Order"));
        jazzName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        jazzStreams.setCellValueFactory(new PropertyValueFactory<>("Streams"));
        String query = """
                select song.id as id, song.name as name, SUM(streams.streams) as streams
                from song
                         join streams on song.id = streams.song_id join album on album.id = song.album_id where album.category_id = ?
                group by song.name, song.id ORDER BY streams DESC""";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, 2);
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
        jazzList.setItems(data);
    }

    private void populatePopChart() {
        final ObservableList<Song> data = FXCollections.observableArrayList();
        popOrder.setCellValueFactory(new PropertyValueFactory<>("Order"));
        popName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        popStreams.setCellValueFactory(new PropertyValueFactory<>("Streams"));
        String query = """
                select song.id as id, song.name as name, SUM(streams.streams) as streams
                from song
                         join streams on song.id = streams.song_id join album on album.id = song.album_id where album.category_id = ?
                group by song.name, song.id ORDER BY streams DESC""";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, 1);
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
        popList.setItems(data);
    }

    @FXML
    private void onPopRowClickAction() throws IOException {
        Song song = popList.getSelectionModel().getSelectedItem();
        if (song != null) {
            App.setSongID(song.getID());
            App.navigateTo("selectedSong");
        }
    }

    @FXML
    private void onJazzRowClickAction() throws IOException {
        Song song = jazzList.getSelectionModel().getSelectedItem();
        if (song != null) {
            App.setSongID(song.getID());
            App.navigateTo("selectedSong");
        }
    }

    @FXML
    private void onClassicRowClickAction() throws IOException {
        Song song = classicList.getSelectionModel().getSelectedItem();
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
