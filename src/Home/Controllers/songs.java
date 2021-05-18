package Home.Controllers;

import Home.App;
import Home.Helper;
import Home.Modules.Album;
import Home.Modules.Song;
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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static Home.Helper.execute;
import static Home.Helper.executeQuery;

public class songs implements Initializable {
    @FXML
    private Circle profilePicture;

    @FXML
    private Text displayName;

    @FXML
    private ChoiceBox<String> navigator;

    @FXML
    private TextField name;
    @FXML
    private TextField length;

    @FXML
    private ChoiceBox<String> album;

    @FXML
    private ChoiceBox<String> fullArtistList;

    @FXML
    private ChoiceBox<String> songArtistlist;

    @FXML
    private DatePicker releaseDate;

    @FXML
    private TableView<Song> songsTable;

    @FXML
    private TableColumn<Song, Integer> ID;
    @FXML
    private TableColumn<Song, Integer> colLength;

    @FXML
    private TableColumn<Song, String> colName;

    @FXML
    private TableColumn<Song, String> colAlbum;

    @FXML
    private TableColumn<Song, String> colReleaseDate;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getUserData(displayName, profilePicture);
          populateNavigator();
          importSongs();
          importAlbums();
          importArtist();
    }


    @FXML
    private void importSongs() {
        final ObservableList<Song> data = FXCollections.observableArrayList();
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        colLength.setCellValueFactory(new PropertyValueFactory<>("length"));
        colReleaseDate.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        String query = "SELECT * FROM song";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int album_id = resultSet.getInt("album_id");
                int length = resultSet.getInt("length");
                Date release_date = resultSet.getDate("release_date");
                String album = getAlbum(album_id);
                data.add(new Song(id,name,album,length,release_date));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        songsTable.setItems(data);
    }

    @FXML
    private void createSong() throws SQLException {
//        if (!name.getText().equals("") ) {
//            int artistID = artist.getSelectionModel().getSelectedIndex()+1;
//            int categoryID = category.getSelectionModel().getSelectedIndex()+1;
//            if(releaseDate.getValue()==null){
//                setCurrentDate(releaseDate);
//            }
//            String artistQuery = "SELECT * FROM album WHERE name = ?";
//            PreparedStatement artistStatement = App.connection.prepareStatement(artistQuery);
//            artistStatement.setString(1, name.getText());
//            ResultSet artistResult = executeQuery(artistStatement);
//            if(artistResult.next())
//            {
//                    App.showError("Album exists","An album with this name does exist ,please change the album's name");
//                    return;
//            }
//            String query = "INSERT INTO album(name, artist_id, release_date, category_id, picture) VALUES (?, ?, ?, ?, ?)";
//            PreparedStatement statement = App.connection.prepareStatement(query);
//            statement.setString(1, name.getText());
//            statement.setInt(2, artistID);
//            statement.setDate(3, Date.valueOf(releaseDate.getValue()));
//            statement.setInt(4, categoryID);
//            statement.setString(5, picture.getText());
//            execute(statement);
//            importAlbums();
//            App.showSuccessMessage("Album " + name.getText() + " has been created", "");
//            clear();
//        }
    }

    @FXML
    private void deleteSong() throws SQLException {

//        if (!name.getText().equals("")) {
//            String nameQuery = "SELECT * FROM album WHERE name = ?";
//            PreparedStatement nameStatement = App.connection.prepareStatement(nameQuery);
//            nameStatement.setString(1, name.getText());
//            ResultSet nameResult = executeQuery(nameStatement);
//            if(!nameResult.next())
//            {
//                App.showError("Album doesn't exists","no such album with this name");
//                return;
//            }
//            Album album =  albumsTable.getSelectionModel().getSelectedItem();
//            int id = album.getID();
//            String query = "DELETE FROM  album WHERE id = ?";
//            PreparedStatement statement = App.connection.prepareStatement(query);
//            statement.setInt(1, id);
//            execute(statement);
//            App.showSuccessMessage("Album " + name.getText() + " has been deleted", "");
//            importAlbums();
//            clear();
//        }
    }


    @FXML
    private void updateSong() throws SQLException {
//        if (!name.getText().equals("")) {
//            int artistID = artist.getSelectionModel().getSelectedIndex()+1;
//            int categoryID = category.getSelectionModel().getSelectedIndex()+1;
//            Album album = albumsTable.getSelectionModel().getSelectedItem();
//            int id;
//            if(album==null)
//                id=-1;
//            else
//                id=album.getID();
//            if(releaseDate.getValue()==null){
//                setCurrentDate(releaseDate);
//            }
//            String query = "UPDATE  album SET name =?,artist_id =?,release_date =?,category_id =?,picture = ? WHERE id = ?";
//            PreparedStatement statement = App.connection.prepareStatement(query);
//            statement.setString(1, name.getText());
//            statement.setInt(2, artistID);
//            statement.setDate(3, Date.valueOf(releaseDate.getValue()));
//            statement.setInt(4, categoryID);
//            statement.setString(5, picture.getText());
//            statement.setInt(6, id);
//
//            if (execute(statement) != 0) {
//                importAlbums();
//                App.showSuccessMessage("Album " + name.getText() + " has been updated", "");
//            } else {
//                App.showError("Album does not exist", "");
//            }
//            clear();
//
//        }
    }

    @FXML
    private void onRowClickAction() throws SQLException {
        Song song = songsTable.getSelectionModel().getSelectedItem();
        if (song != null) {
            name.setText(song.getName());
            album.getSelectionModel().select(song.getAlbum());
            releaseDate.setValue(song.getReleaseDate().toLocalDate());
            length.setText(Integer.toString(song.getLength()));
            importSongArtists(song.getID());
        }
    }

    private void clear() {
//        name.setText("");
//        picture.setText("");
//       releaseDate.setValue(null);
//       artist.getSelectionModel().select(0);
//       category.getSelectionModel().select(0);
    }



    private void importArtist()   {
        final ObservableList<String> artists = FXCollections.observableArrayList();
        String query = "SELECT * FROM artist ORDER BY id";
        PreparedStatement statement = null;
        try {
            statement = App.connection.prepareStatement(query);
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                artists.add(name);
            }
            fullArtistList.setItems(artists);
            fullArtistList.getSelectionModel().select(0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void importAlbums()   {
        final ObservableList<String> albums = FXCollections.observableArrayList();
        String query = "SELECT * FROM album ORDER BY id";
        PreparedStatement statement = null;
        try {
            statement = App.connection.prepareStatement(query);
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                albums.add(name);
            }
            album.setItems(albums);
            album.getSelectionModel().select(0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void importSongArtists(int ID)   {
        final ObservableList<String> songArtists = FXCollections.observableArrayList();
        String query = "SELECT * FROM song_artist WHERE artist_id =?";
        PreparedStatement statement = null;
        try {
            statement = App.connection.prepareStatement(query);
            statement.setInt(1,ID);
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                int artist_id = resultSet.getInt("artist_id");
                String artist_name = getArtist(artist_id);
                songArtists.add(artist_name);
            }
            songArtistlist.setItems(songArtists);
            songArtistlist.getSelectionModel().select(0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    private String getArtist(int id) throws SQLException {
        String query = "SELECT * FROM artist WHERE id = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getString("name");
    }

    private String getAlbum(int id) throws SQLException {
        String query = "SELECT * FROM album WHERE id = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getString("name");
    }


    private void setCurrentDate(DatePicker datePicker){
        java.util.Date input = new java.util.Date();
//        LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(input));
       datePicker.setValue(date);
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
        navigator.getSelectionModel().select(3);
    }

    @FXML
    private void navigate() {
        Helper.navigate(navigator);
    }


}
