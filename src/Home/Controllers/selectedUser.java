package Home.Controllers;

import Home.App;
import Home.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

public class selectedUser implements Initializable {
    @FXML
    Text displayName;
    @FXML
    Circle profilePicture;
    @FXML
    Text userName;
    @FXML
    Circle userPicture;
    @FXML
    ListView<String> popListView;
    @FXML
    ListView<String> jazzListView;
    @FXML
    ListView<String> classicListView;
    @FXML
    ChoiceBox<String> navigator;
    @FXML
    TextField selectedUser;
    ObservableList<String> songs = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getUserData(displayName,profilePicture);
        Helper.populateUserNavigator(navigator,"userAlbums");

    }


    @FXML private void onSongActionClick(){
//        String song = songsListView.getSelectionModel().getSelectedItem();
//        if(song!=null){
//            selectedSong.setText(song);
//        }
    }

    @FXML private void viewSong() throws IOException, SQLException {
//         if(songs.contains(selectedSong.getText())){
//             App.setSongID(getSongID(selectedSong.getText()));
//             App.navigateTo("selectedSong");
//         }
    }
    @FXML private void addSong() throws SQLException {
//        String albumName = this.albumName.getText();
//         int user_id = getUserID(App.getUserEmail());
//         int song_id= getSongID(selectedSong.getText());
//         int song_category = getCategory(albumName);
//         int playListID = getPlayList(user_id,song_category);
//        String idQuery = "SELECT * FROM playlist_song WHERE playlist_id = ?";
//
//        PreparedStatement idStatement = App.connection.prepareStatement(idQuery);
//        idStatement.setInt(1, playListID);
//        ResultSet idResult = executeQuery(idStatement);
//        if (idResult.next()) {
//            int result_id = idResult.getInt("song_id");
//            if(result_id==song_id)
//            {
//                App.showError("This song is already added","You already have this song in your playlist");
//                return;
//            }
//        }
//        String query = "INSERT INTO playlist_song (playlist_id, song_id) VALUES (?, ?)";
//        PreparedStatement statement = App.connection.prepareStatement(query);
//        statement.setInt(1, playListID);
//        statement.setInt(2, song_id);
//        execute(statement);
//        App.showSuccessMessage("Song added","The song has now been added to your playlist");
    }



    @FXML public void instantiate(int userID){
        String query = "SELECT  * app_user WHERE id =? ORDER BY name ";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            ResultSet resultSet = executeQuery(statement);
                String name = resultSet.getString("name");
                userName.setText(name);
               try {
                   File imageFile = new File(resultSet.getString("picture"));
                   String imageLocation = imageFile.toURI().toString();
                   Image pic = new Image(imageLocation, false);
                   userPicture.setFill(new ImagePattern(pic));
               }
               catch (Exception ignored){}

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private int getUserID(String email) throws SQLException {
        String query = "SELECT * FROM app_user WHERE email = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getInt("id");
    }

    private int getSongID(String name) throws SQLException {
        String query = "SELECT * FROM song WHERE name = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getInt("id");
    }

    private int getCategory(String name) throws SQLException {
        String query = "SELECT * FROM album WHERE name = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getInt("category_id");
    }

    private int getPlayList(int userID , int category_id) throws SQLException {
        String query = "SELECT * FROM playlist WHERE user_id = ? AND category_id=?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setInt(1, userID);
        statement.setInt(2, category_id);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getInt("id");
    }

    private  void getPopList(int userID){

    }

    private  void getJazzList(int userID){

    }
    private  void getClassicList(int userID){

    }


    @FXML private  void navigate (){
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
