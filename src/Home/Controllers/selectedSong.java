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
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Home.Helper.execute;
import static Home.Helper.executeQuery;

public class selectedSong implements Initializable {
    @FXML
    Text displayName;
    @FXML
    Circle profilePicture;

    @FXML
    ChoiceBox<String> navigator;

    @FXML
    TableView<Song> popList;

    @FXML
    Circle songPicture;

    @FXML
    Text songName;

    @FXML
    Text albumName;

    @FXML
    Text releaseDate;

    @FXML
    ListView<String> artistsList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getUserData(displayName, profilePicture);
        Helper.populateUserNavigator(navigator, "Charts");
        importSong();
        importArtists();
    }

    private void importArtists() {
        final ObservableList<String> artists = FXCollections.observableArrayList();
        String query = "SELECT name from artist where id in (select artist_id from song_artist where song_id = ?)";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, App.songID);
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                artists.add(resultSet.getString("name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        artistsList.setItems(artists);
    }

    private void importSong() {
        String query = "SELECT s.name AS s_name, a.name AS a_name, a.picture AS picture, s.release_date AS release_date FROM song s join album a on a.id = s.album_id WHERE s.id = ?";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, App.songID);
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                songName.setText(resultSet.getString("s_name"));
                albumName.setText(resultSet.getString("a_name"));

                String picturePath = resultSet.getString("picture");
                File imageFile = new File(picturePath);
                String imageLocation = imageFile.toURI().toString();
                Image pic = new Image(imageLocation, false);
                songPicture.setFill(new ImagePattern(pic));

                releaseDate.setText(String.valueOf(resultSet.getDate("release_date")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    private void onPlay() {
        int result = 0;
        String query = "UPDATE streams set streams = streams + 1 where song_id = ? and country_id = ?";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, App.songID);
            statement.setInt(2, App.userCountryId);
            result = execute(statement);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (result == 0) {
            String creationQuery = "INSERT INTO streams VALUES (?, ?, 1)";
            try (PreparedStatement creationStatement = App.connection.prepareStatement(creationQuery)) {
                creationStatement.setInt(1, App.songID);
                creationStatement.setInt(2, App.userCountryId);
                execute(creationStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
