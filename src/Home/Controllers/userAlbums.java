package Home.Controllers;

import Home.App;
import Home.Helper;
import Home.Modules.Album;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Home.App.loaderFactory;
import static Home.Helper.executeQuery;

public class userAlbums implements Initializable {
    @FXML
    Text displayName;
    @FXML
    Circle profilePicture;
    @FXML
    ListView<String> albumsListView;
    @FXML
    ChoiceBox<String> navigator;
    @FXML
    TextField selectedAlbum;
    ObservableList <String > albums = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getUserData(displayName,profilePicture);
        Helper.populateUserNavigator(navigator,"userAlbums");
        getAlbums();
    }

    private void getAlbums(){

        String query = "SELECT * FROM album ORDER BY id";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                albums.add(name);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        albumsListView.setItems(albums);
    }

    @FXML private void onAlbumClickAction(){
        String album = albumsListView.getSelectionModel().getSelectedItem();
        if(album!=null){
            selectedAlbum.setText(album);
        }
    }

    @FXML private void goToAlbum(){
        String album = albumsListView.getSelectionModel().getSelectedItem();
        if(albums.contains(album)){
             try {
        FXMLLoader loader = loaderFactory("selectedAlbum");
        Parent root = loader.load();
        selectedAlbum controller = loader.getController();
        int albumID =getAlbumByName(album);
          controller.instantiate(albumID);
        App.scene.setRoot(root);
    } catch (IOException | SQLException e) {
        e.printStackTrace();
    }
        }
        else
            App.showInfoMessage("We couldn't find this album","please check the album name and try again");
    }

    private int getAlbumByName(String name) throws SQLException {
        String query = "SELECT * FROM album WHERE name = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getInt("id");
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
