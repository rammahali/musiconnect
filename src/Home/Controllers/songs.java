package Home.Controllers;

import Home.App;
import Home.Helper;
import Home.Modules.Song;
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
    ObservableList<String> songArtists = FXCollections.observableArrayList();
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
        String query = "SELECT * FROM song ORDER BY id";
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
        if (!name.getText().equals("") &&songArtists.size()!=0 ){

            if(releaseDate.getValue()==null){
                setCurrentDate(releaseDate);
            }
            String songQuery = "SELECT * FROM song WHERE name = ?";
            PreparedStatement songStatement = App.connection.prepareStatement( songQuery);
            songStatement.setString(1, name.getText());
            ResultSet songResult = executeQuery(songStatement);
            if(songResult.next())
            {
                    App.showError("Song exists"," a song with this name does exist ,please change the song's name");
                    return;
            }
            int album_id = album.getSelectionModel().getSelectedIndex()+1;
            int length;
            if(this.length.getText().equals(""))
                length=0;
            else
                length=Integer.parseInt(this.length.getText());
            String query = "INSERT INTO song(name, album_id, length, release_date) VALUES (?, ?, ?, ?) ";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setString(1, name.getText());
            statement.setInt(2, album_id);
            statement.setInt(3, length);
            statement.setDate(4, Date.valueOf(releaseDate.getValue()));
            execute(statement);
            setSongArtists(getSongByName(name.getText()));
            importSongs();
            App.showSuccessMessage("Song " + name.getText() + " has been created", "");
            clear();
        }
    }

    @FXML
    private void deleteSong() throws SQLException {

        if (!name.getText().equals("")) {
            String nameQuery = "SELECT * FROM song WHERE name = ?";
            PreparedStatement nameStatement = App.connection.prepareStatement(nameQuery);
            nameStatement.setString(1, name.getText());
            ResultSet nameResult = executeQuery(nameStatement);
            if(!nameResult.next())
            {
                App.showError("Song doesn't exists","no such song with this name");
                return;
            }
            Song song =  songsTable.getSelectionModel().getSelectedItem();
            int id = song.getID();
            String query = "DELETE FROM  song WHERE id = ?";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setInt(1, id);
            execute(statement);
            App.showSuccessMessage("Song " + name.getText() + " has been deleted", "");
            importSongs();
            clear();
        }
    }



    @FXML
    private void updateSong() throws SQLException {
        if (!name.getText().equals("")) {
            int album_id = album.getSelectionModel().getSelectedIndex()+1;
            int length;
            if(this.length.getText().equals(""))
                length=0;
            else
                length=Integer.parseInt(this.length.getText());
            Song song = songsTable.getSelectionModel().getSelectedItem();
            int id;
            if(song==null)
                id=-1;
            else
                id=song.getID();
            if(releaseDate.getValue()==null){
                setCurrentDate(releaseDate);
            }
            String query = "UPDATE song SET name =?,album_id =?,length =?,release_date=? WHERE id = ?";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setString(1, name.getText());
            statement.setInt(2, album_id);
            statement.setInt(3, length);
            statement.setDate(4, Date.valueOf(releaseDate.getValue()));
            statement.setInt(5, id);

            if (execute(statement) != 0) {
                importSongs();
                updateSongArtist(song.getID());
                App.showSuccessMessage("Song " + name.getText() + " has been updated", "");
            } else {
                App.showError("Song does not exist", "");
            }
            clear();

        }
    }

    @FXML
    private void onRowClickAction()  {
        Song song = songsTable.getSelectionModel().getSelectedItem();
        if (song != null) {
            name.setText(song.getName());
            album.getSelectionModel().select(song.getAlbum());
            releaseDate.setValue(song.getReleaseDate().toLocalDate());
            length.setText(Integer.toString(song.getLength()));
            importSongArtists(song.getID());
        }
    }

    @FXML  private  void  addArtist(){
        String artist = fullArtistList.getSelectionModel().getSelectedItem();
        if(artist!=null){
            if(songArtists.contains(artist)){
                App.showInfoMessage("This Artist is already added","");
                return;
            }
            songArtists.add(artist);
            songArtistlist.setItems(songArtists);
           songArtistlist.getSelectionModel().select(songArtists.size()-1);
        }
    }
    @FXML private  void  deleteArtist(){
        String artist = songArtistlist.getSelectionModel().getSelectedItem();
        if(artist!=null){
            int selectedIndex = songArtistlist.getSelectionModel().getSelectedIndex();
            songArtists.remove(artist);
            songArtistlist.setItems(songArtists);
            selectedIndex=selectedIndex-1;
           if(selectedIndex!=0){
               songArtistlist.getSelectionModel().select(selectedIndex-1);
           }
        }
    }


    private void clear() {
        name.setText("");
       releaseDate.setValue(null);
       album.getSelectionModel().select(0);
       length.setText("");
       songArtists.clear();
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
        String query = "SELECT name FROM artist WHERE artist.id in (SELECT artist_id from song_artist WHERE song_id = ?)";
        PreparedStatement statement = null;
        try {
            statement = App.connection.prepareStatement(query);
            statement.setInt(1,ID);
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                String artist_name = resultSet.getString("name");
                songArtists.add(artist_name);
                this.songArtists.add(artist_name);
            }
            songArtistlist.setItems(songArtists);
            songArtistlist.getSelectionModel().select(0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setSongArtists(int songID) throws SQLException {
       for(int i =0;i<songArtists.size();i++){
           int artist_id = getArtistByName(songArtists.get(i));
           String query = "INSERT INTO song_artist(song_id, artist_id) VALUES (?, ?)";
           PreparedStatement statement = App.connection.prepareStatement(query);
           statement.setInt(1, songID);
           statement.setInt(2, artist_id);
           execute(statement);
       }
    }

    private void updateSongArtist(int id) throws SQLException {
          deleteSongArtists(id);
          setSongArtists(id);
    }

    @FXML
    private void deleteSongArtists(int id) throws SQLException {
            String query = "DELETE FROM  song_artist WHERE song_id = ?";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setInt(1, id);
            execute(statement);
        }

    private String getAlbum(int id) throws SQLException {
        String query = "SELECT * FROM album WHERE id = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getString("name");
    }

    private int getArtistByName(String name) throws SQLException {
        String query = "SELECT * FROM artist WHERE name = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getInt("id");
    }

    private int getSongByName(String name) throws SQLException {
        String query = "SELECT * FROM song WHERE name = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getInt("id");
    }
    private void setCurrentDate(DatePicker datePicker){
        java.util.Date input = new java.util.Date();
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
        navigator.getSelectionModel().select(4);
    }

    @FXML
    private void navigate() {
        Helper.navigate(navigator);
    }


}
