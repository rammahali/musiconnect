package Home.Controllers;

import Home.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import static Home.Helper.execute;
import static Home.Helper.getHashedPassword;

public class createAccount implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private ChoiceBox countryList;
    @FXML
    private TextField profileImageName;
    @FXML
    private PasswordField password;
    @FXML
    private Button createAccount;
    @FXML
    private Button login;
    String profileImagePath="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     importCountries();
    }

    @FXML
    private void onLoginMenuClick() throws IOException {
        App.navigateTo("login");
    }


    @FXML
    private void onCreateAccountClick() {
        // TODO: change country to country code, not 1
        String query = String.format("INSERT INTO app_user(name, email, password_hash, country_id, picture) VALUES ('%s', '%s', '%s', %d, '%s')",
                name.getText(), email.getText(), getHashedPassword(password.getText()), 1, profileImagePath);
           execute(query);
           App.showSuccessMessage("Account has been created","You are now logged in");
    }
    @FXML private void selectProfileImage(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile!=null){
            profileImageName.setText(selectedFile.getName());
            profileImagePath=selectedFile.getPath();
        }
    }

    @FXML private void importCountries(){
        ObservableList<String> countries = FXCollections.observableArrayList();
        countries.add("Select country");
        String [] countryNames = Locale.getISOCountries();
        for (String country : countryNames) {
            Locale obj = new Locale("en", country);
            countries.add(obj.getDisplayCountry());
        }
        countryList.setItems(countries);
        countryList.getSelectionModel().select(0);
    }

}
