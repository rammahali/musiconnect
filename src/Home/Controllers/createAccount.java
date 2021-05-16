package Home.Controllers;

import Home.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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

import static Home.App.loaderFactory;
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
    String profileImagePath = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        importCountries();
    }

    @FXML
    private void onLoginMenuClick() throws IOException {
        App.navigateTo("login");
    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @FXML
    private void onCreateAccountClick() {
        // validation
        if (name.getText().length() < 4)
            App.showError("Name cannot be less than 4 chars", "please update your name");
        else if (!isValidEmailAddress(email.getText()))
            App.showError("InValid email address", "please enter a Valid email");
        else if (password.getText().length() < 5)
            App.showError("Password cannot be less than 5 chars", "please update your password");
        else if (countryList.getValue().toString().equals("Select country"))
            App.showError("Country must be selected", "please select your country");

        else {
            // TODO: change country to country code, not 1
            String query = String.format("INSERT INTO app_user(name, email, password_hash, country_id, picture) VALUES ('%s', '%s', '%s', %d, '%s')",
                    name.getText(), email.getText(), getHashedPassword(password.getText()), 1, profileImagePath);
            execute(query);
            try {
                FXMLLoader loader = loaderFactory("userPlaylists");
                Parent root = loader.load();
                userPlaylists controller = loader.getController();

                controller.getUserData(email.getText());
                App.scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
            App.showSuccessMessage("Account has been created", "You are now logged in");
        }

    }

    @FXML
    private void selectProfileImage() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            profileImageName.setText(selectedFile.getName());
            profileImagePath = selectedFile.getPath();
        }
    }

    @FXML
    private void importCountries() {
        ObservableList<String> countries = FXCollections.observableArrayList();
        countries.add("Select country");
        String[] countryNames = Locale.getISOCountries();
        for (String country : countryNames) {
            Locale obj = new Locale("en", country);
            countries.add(obj.getDisplayCountry());
        }
        countryList.setItems(countries);
        countryList.getSelectionModel().select(0);
    }

}
