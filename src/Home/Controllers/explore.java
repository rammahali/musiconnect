package Home.Controllers;

import Home.App;
import Home.Helper;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Home.App.loaderFactory;
import static Home.Helper.executeQuery;

public class explore implements Initializable {
    @FXML
    Text displayName;
    @FXML
    Circle profilePicture;
    @FXML
    ListView<String> usersListView;
    @FXML
    ChoiceBox<String> navigator;
    @FXML
    TextField selectedUser;
    ObservableList <String > users = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getUserData(displayName,profilePicture);
        Helper.populateUserNavigator(navigator,"userAlbums");
        getUsers();
    }

    private void getUsers(){

        String query = "SELECT * FROM subscription ";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                String name = getUserByID(user_id);
                users.add(name);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        usersListView.setItems(users);
    }

    @FXML private void onUserClickAction(){
        String user = usersListView.getSelectionModel().getSelectedItem();
        if(user!=null){
            selectedUser.setText(user);
        }
    }

    @FXML private void getUser(){
//        String user  = usersListView.getSelectionModel().getSelectedItem();
//        if(users.contains(user)){
//             try {
//        FXMLLoader loader = loaderFactory("selectedAlbum");
//        Parent root = loader.load();
//        selectedAlbum controller = loader.getController();
//        int albumID =getAlbumByName(album);
//          controller.instantiate(albumID);
//        App.scene.setRoot(root);
//    } catch (IOException | SQLException e) {
//        e.printStackTrace();
//    }
//        }
//        else
//            App.showInfoMessage("We couldn't find this album","please check the album name and try again");
    }

    private String getUserByID(int id) throws SQLException {
        String query = "SELECT * FROM app_user WHERE id = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getString("name");
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
