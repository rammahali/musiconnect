package Home.Controllers;

import Home.App;
import Home.Modules.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class users implements Initializable {
    @FXML Text displayName;
    @FXML Circle profilePicture;
    @FXML ChoiceBox navigator;
    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private ChoiceBox<String> Country;

    @FXML
    private TextField picturePath;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> ID;

    @FXML
    private TableColumn<User, String> colName;

    @FXML
    private TableColumn<User, String> colEmail;

    @FXML
    private TableColumn<User, String> colCountry;

    @FXML
    private TableColumn<User, String> colPath;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         instantiateAdmin();
         populateNavigator();
        importUsers();
    }


    @FXML private void importUsers(){
        final ObservableList<User> data = FXCollections.observableArrayList();
         ID.setCellValueFactory(new PropertyValueFactory<User, Integer>("ID"));
         colName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
         colEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
         colCountry.setCellValueFactory(new PropertyValueFactory<User, String>("country"));
         colPath.setCellValueFactory(new PropertyValueFactory<User, String>("picture"));
         String query = "SELECT * FROM app_user ";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            ResultSet resultSet = executeQuery(statement, query);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name =resultSet.getString("name");
                String email =resultSet.getString("email");
                String picture =resultSet.getString("picture");
                int countryID = resultSet.getInt("country_id");
                String country = getCountry(countryID);
                data.add(new User(id,name,email,country,picture));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
         usersTable.setItems(data);
    }
   @FXML private void addUser(){
   }
    @FXML private void deleteUser(){
    }
    @FXML private void editUser(){
    }
    private String getCountry(int id) throws SQLException {
        String query = "SELECT * FROM country WHERE id = ?";
         PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = executeQuery(statement, query);
            resultSet.next();
            String country = resultSet.getString("name");
            return country;
        }


   private void instantiateAdmin(){
       String query = "SELECT * FROM app_user WHERE email = ?";
       try (PreparedStatement statement = App.connection.prepareStatement(query)) {
           statement.setString(1, App.getUserEmail());
           ResultSet resultSet = executeQuery(statement, query);
           resultSet.next();
           String username = resultSet.getString("name");
           String imagePath = resultSet.getString("picture");
           displayName.setText(username);
           File imageFile = new File(imagePath);
           String imageLocation = imageFile.toURI().toString();
           Image pic = new Image(imageLocation,false);
           profilePicture.setFill(new ImagePattern(pic));

       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
   }
    @FXML private void logoutApp() throws IOException {
        App.navigateTo("login");
    }
    @FXML private void close()  {
        App.close();
    }
    private void populateNavigator(){
        ObservableList<String> pages = FXCollections.observableArrayList();
        pages.addAll("Dashboard","Users","Singers","Albums","Songs");
        navigator.setItems(pages);
        navigator.getSelectionModel().select(1);
    }

    @FXML private void navigate(){
        int index = navigator.getSelectionModel().getSelectedIndex();
        switch (index){
            case 0:
                try {
                    App.navigateTo("adminPanel");
                } catch (IOException e) {
                   App.showInfoMessage("Page not found","we couldn't find this page...");
                }
                break;
            case 1:
                try {
                    App.navigateTo("users");
                } catch (IOException e) {
                    App.showInfoMessage("Page not found","we couldn't find this page...");
                }
                break;
            case 2:
                try {
                    App.navigateTo("singers");
                } catch (IOException e) {
                    App.showInfoMessage("Page not found","we couldn't find this page...");
                }
                break;
            case 3:
                try {
                    App.navigateTo("albums");
                } catch (IOException e) {
                    App.showInfoMessage("Page not found","we couldn't find this page...");
                }
                break;
            case 4:
                try {
                    App.navigateTo("songs");
                } catch (IOException e) {
                    App.showInfoMessage("Page not found","we couldn't find this page...");
                }
                break;
        }

    }


}
