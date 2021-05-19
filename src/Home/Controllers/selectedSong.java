package Home.Controllers;

import Home.App;
import Home.Helper;
import Home.Modules.Song;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
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
    Text length;


    private int songID;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getUserData(displayName, profilePicture);
        Helper.populateUserNavigator(navigator, "Charts");
        importSong();
    }

    private void importSong() {

        String query = "SELECT s.name, a.name, a.picture AS picture, s.release_date FROM song s join album a on a.id = s.album_id WHERE s.id = ?";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, songID);
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                songName.setText(resultSet.getString("s.name"));
                albumName.setText(resultSet.getString("a.name"));

                String picturePath = resultSet.getString("picture");
                File imageFile = new File(picturePath);
                String imageLocation = imageFile.toURI().toString();
                Image pic = new Image(imageLocation, false);
                songPicture.setFill(new ImagePattern(pic));

                releaseDate.setText(String.valueOf(resultSet.getDate("s.release_date")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }
}
