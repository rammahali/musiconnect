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

public class library implements Initializable {
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
        String query = """
                select * from song where id in
                (select song_id
                from playlist_song
                         join playlist p on p.id = playlist_song.playlist_id
                    where category_id = ? and user_id = ?)""";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, 3);
            statement.setInt(2, getUserID(App.getUserEmail()));
            ResultSet resultSet = executeQuery(statement);
            int i = 1;
            while (resultSet.next()) {
                int songID = resultSet.getInt("id");
                String name = resultSet.getString("name");
                data.add(new Song(i, songID, name));
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
        String query = """
                select * from song where id in
                (select song_id
                from playlist_song
                         join playlist p on p.id = playlist_song.playlist_id
                    where category_id = ? and user_id = ?)""";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, 2);
            statement.setInt(2, getUserID(App.getUserEmail()));
            ResultSet resultSet = executeQuery(statement);
            int i = 1;
            while (resultSet.next()) {
                int songID = resultSet.getInt("id");
                String name = resultSet.getString("name");
                data.add(new Song(i, songID, name));
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
        String query = """
                select * from song where id in
                (select song_id
                from playlist_song
                         join playlist p on p.id = playlist_song.playlist_id
                    where category_id = ? and user_id = ?)""";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, 1);
            statement.setInt(2, getUserID(App.getUserEmail()));
            ResultSet resultSet = executeQuery(statement);
            int i = 1;
            while (resultSet.next()) {
                int songID = resultSet.getInt("id");
                String name = resultSet.getString("name");
                data.add(new Song(i, songID, name));
                i++;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        popList.setItems(data);
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
