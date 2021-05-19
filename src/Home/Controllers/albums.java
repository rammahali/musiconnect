package Home.Controllers;

import Home.App;
import Home.Helper;
import Home.Modules.Album;
import Home.Modules.User;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import static Home.Helper.*;

public class albums implements Initializable {
    @FXML
    private Circle profilePicture;

    @FXML
    private Text displayName;

    @FXML
    private ChoiceBox<String> navigator;

    @FXML
    private TextField name;

    @FXML
    private ChoiceBox<String> artist;

    @FXML
    private TextField picture;

    @FXML
    private ChoiceBox<String> category;

    @FXML
    private DatePicker releaseDate;

    @FXML
    private TableView<Album> albumsTable;

    @FXML
    private TableColumn<Album, Integer> ID;

    @FXML
    private TableColumn<Album, String> colName;

    @FXML
    private TableColumn<Album, String> colArtist;

    @FXML
    private TableColumn<Album, Date> colReleaseDate;

    @FXML
    private TableColumn<Album, String> colCategory;

    @FXML
    private TableColumn<Album, String> colPicture;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helper.getUserData(displayName, profilePicture);
          populateNavigator();
          importAlbums();
          importArtist();
          importCategories();
    }


    @FXML
    private void importAlbums() {
        final ObservableList<Album> data = FXCollections.observableArrayList();
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colReleaseDate.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPicture.setCellValueFactory(new PropertyValueFactory<>("picture"));
        String query = "SELECT * FROM album ORDER BY id";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int artist_id = resultSet.getInt("artist_id");
                int category_id = resultSet.getInt("category_id");
                Date release_date = resultSet.getDate("release_date");
                String picture = resultSet.getString("picture");
                String artist = getArtist(artist_id);
                String category =getCategory(category_id);
                data.add(new Album(id,name,artist,category,release_date,picture));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        albumsTable.setItems(data);
    }

    @FXML
    private void createAlbum() throws SQLException {
        if (!name.getText().equals("") ) {
            int artistID = getArtistByName(artist.getValue());
            int categoryID =  getCategoryByName(category.getValue());
            if(releaseDate.getValue()==null){
                setCurrentDate(releaseDate);
            }
            String artistQuery = "SELECT * FROM album WHERE name = ?";
            PreparedStatement artistStatement = App.connection.prepareStatement(artistQuery);
            artistStatement.setString(1, name.getText());
            ResultSet artistResult = executeQuery(artistStatement);
            if(artistResult.next())
            {
                    App.showError("Album exists","An album with this name does exist ,please change the album's name");
                    return;
            }
            String query = "INSERT INTO album(name, artist_id, release_date, category_id, picture) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setString(1, name.getText());
            statement.setInt(2, artistID);
            statement.setDate(3, Date.valueOf(releaseDate.getValue()));
            statement.setInt(4, categoryID);
            statement.setString(5, picture.getText());
            execute(statement);
            importAlbums();
            App.showSuccessMessage("Album " + name.getText() + " has been created", "");
            clear();
        }
    }

    @FXML
    private void deleteAlbum() throws SQLException {

        if (!name.getText().equals("")) {
            String nameQuery = "SELECT * FROM album WHERE name = ?";
            PreparedStatement nameStatement = App.connection.prepareStatement(nameQuery);
            nameStatement.setString(1, name.getText());
            ResultSet nameResult = executeQuery(nameStatement);
            if(!nameResult.next())
            {
                App.showError("Album doesn't exists","no such album with this name");
                return;
            }
            Album album =  albumsTable.getSelectionModel().getSelectedItem();
            int id = album.getID();
            String query = "DELETE FROM  album WHERE id = ?";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setInt(1, id);
            execute(statement);
            App.showSuccessMessage("Album " + name.getText() + " has been deleted", "");
            importAlbums();
            clear();
        }
    }


    @FXML
    private void updateAlbum() throws SQLException {
        if (!name.getText().equals("")) {
            int artistID = getArtistByName(artist.getValue());
            int categoryID =  getCategoryByName(category.getValue());
            Album album = albumsTable.getSelectionModel().getSelectedItem();
            int id;
            if(album==null)
                id=-1;
            else
                id=album.getID();
            if(releaseDate.getValue()==null){
                setCurrentDate(releaseDate);
            }
            String query = "UPDATE  album SET name =?,artist_id =?,release_date =?,category_id =?,picture = ? WHERE id = ?";
            PreparedStatement statement = App.connection.prepareStatement(query);
            statement.setString(1, name.getText());
            statement.setInt(2, artistID);
            statement.setDate(3, Date.valueOf(releaseDate.getValue()));
            statement.setInt(4, categoryID);
            statement.setString(5, picture.getText());
            statement.setInt(6, id);

            if (execute(statement) != 0) {
                importAlbums();
                App.showSuccessMessage("Album " + name.getText() + " has been updated", "");
            } else {
                App.showError("Album does not exist", "");
            }
            clear();

        }
    }

    @FXML
    private void onRowClickAction()  {
        Album album = albumsTable.getSelectionModel().getSelectedItem();
        if (album != null) {
            name.setText(album.getName());
            picture.setText(album.getPicture());
            releaseDate.setValue(album.getReleaseDate().toLocalDate());
            artist.getSelectionModel().select(album.getArtist());
            category.getSelectionModel().select(album.getCategory());
        }
    }

    private void clear() {
        name.setText("");
        picture.setText("");
       releaseDate.setValue(null);
       artist.getSelectionModel().select(0);
       category.getSelectionModel().select(0);
    }

    @FXML
    private void selectProfileImage() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            picture.setText(selectedFile.getPath());
        }
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
            artist.setItems(artists);
            artist.getSelectionModel().select(0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void importCategories()   {
        final ObservableList<String> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM category ORDER BY id";
        PreparedStatement statement = null;
        try {
            statement = App.connection.prepareStatement(query);
            ResultSet resultSet = executeQuery(statement);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                categories.add(name);
            }
            category.setItems(categories);
            category.getSelectionModel().select(0);
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
    private int getArtistByName(String name) throws SQLException {
        String query = "SELECT * FROM artist WHERE name = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getInt("id");
    }
    private int getCategoryByName(String name) throws SQLException {
        String query = "SELECT * FROM category WHERE name = ?";
        PreparedStatement statement = App.connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = executeQuery(statement);
        resultSet.next();
        return resultSet.getInt("id");
    }
    private String getCategory(int id) throws SQLException {
        String query = "SELECT * FROM category WHERE id = ?";
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
