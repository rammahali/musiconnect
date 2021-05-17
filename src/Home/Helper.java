package Home;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;

public class Helper {
    private Helper() {
    }

    public static int execute(PreparedStatement statement, String query) throws SQLException {
        System.out.println("query = " + query);
        System.out.println("Running query...");

        int affectedRows = statement.executeUpdate();

        System.out.println("query successfully executed!");
        return affectedRows;
    }

    public static ResultSet executeQuery(PreparedStatement statement) throws SQLException {

        System.out.println("query = " + statement.toString());
        System.out.println("Running query...");

        return statement.executeQuery();
    }

    public static String getHashedPassword(String password) {
        String hashedPassword = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * hash.length);

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            hashedPassword = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    public static Connection setupDB(String url) {
        Connection connection = null;

        try {
            System.out.println("Connecting...");
            connection = DriverManager.getConnection(url);
            System.out.println("Successfully connected to the database");
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }

        return connection;
    }

    public static void getUserData(Text displayName, Circle profilePicture) {
        String query = "SELECT * FROM app_user WHERE email = ?";
        try (PreparedStatement statement = App.connection.prepareStatement(query)) {
            statement.setString(1, App.getUserEmail());
            ResultSet resultSet = executeQuery(statement);
            resultSet.next();
            String username = resultSet.getString("name");
            String imagePath = resultSet.getString("picture");
            displayName.setText(username);
            File imageFile = new File(imagePath);
            String imageLocation = imageFile.toURI().toString();
            Image pic = new Image(imageLocation, false);
            profilePicture.setFill(new ImagePattern(pic));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public static void navigate(ChoiceBox<String> navigator) {
        int index = navigator.getSelectionModel().getSelectedIndex();
        switch (index) {
            case 0:
                try {
                    App.navigateTo("adminPanel");
                } catch (IOException e) {
                    App.showInfoMessage("Page not found", "we couldn't find this page...");
                }
                break;
            case 1:
                try {
                    App.navigateTo("users");
                } catch (IOException e) {
                    App.showInfoMessage("Page not found", "we couldn't find this page...");
                }
                break;
            case 2:
                try {
                    App.navigateTo("artists");
                } catch (IOException e) {
                    App.showInfoMessage("Page not found", "we couldn't find this page...");
                }
                break;
            case 3:
                try {
                    App.navigateTo("albums");
                } catch (IOException e) {
                    App.showInfoMessage("Page not found", "we couldn't find this page...");
                }
                break;
            case 4:
                try {
                    App.navigateTo("songs");
                } catch (IOException e) {
                    App.showInfoMessage("Page not found", "we couldn't find this page...");
                }
                break;
        }

    }
    public static HashMap<String, Integer> createCountries() {
        HashMap<String, Integer> countriesMap = new HashMap<>();
        countriesMap.put("Aruba", 533);
        countriesMap.put("Afghanistan", 4);
        countriesMap.put("Angola", 24);
        countriesMap.put("Anguilla", 660);
        countriesMap.put("Åland Islands", 248);
        countriesMap.put("Albania", 8);
        countriesMap.put("Andorra", 20);
        countriesMap.put("United Arab Emirates", 784);
        countriesMap.put("Argentina", 32);
        countriesMap.put("Armenia", 51);
        countriesMap.put("American Samoa", 16);
        countriesMap.put("Antarctica", 10);
        countriesMap.put("French Southern Territories", 260);
        countriesMap.put("Antigua and Barbuda", 28);
        countriesMap.put("Australia", 36);
        countriesMap.put("Austria", 40);
        countriesMap.put("Azerbaijan", 31);
        countriesMap.put("Burundi", 108);
        countriesMap.put("Belgium", 56);
        countriesMap.put("Benin", 204);
        countriesMap.put("Bonaire, Sint Eustatius and Saba", 535);
        countriesMap.put("Burkina Faso", 854);
        countriesMap.put("Bangladesh", 50);
        countriesMap.put("Bulgaria", 100);
        countriesMap.put("Bahrain", 48);
        countriesMap.put("Bahamas", 44);
        countriesMap.put("Bosnia and Herzegovina", 70);
        countriesMap.put("Saint Barthélemy", 652);
        countriesMap.put("Belarus", 112);
        countriesMap.put("Belize", 84);
        countriesMap.put("Bermuda", 60);
        countriesMap.put("Bolivia, Plurinational State of", 68);
        countriesMap.put("Brazil", 76);
        countriesMap.put("Barbados", 52);
        countriesMap.put("Brunei Darussalam", 96);
        countriesMap.put("Bhutan", 64);
        countriesMap.put("Bouvet Island", 74);
        countriesMap.put("Botswana", 72);
        countriesMap.put("Central African Republic", 140);
        countriesMap.put("Canada", 124);
        countriesMap.put("Cocos (Keeling) Islands", 166);
        countriesMap.put("Switzerland", 756);
        countriesMap.put("Chile", 152);
        countriesMap.put("China", 156);
        countriesMap.put("Côte d'Ivoire", 384);
        countriesMap.put("Cameroon", 120);
        countriesMap.put("Congo, The Democratic Republic of the", 180);
        countriesMap.put("Congo", 178);
        countriesMap.put("Cook Islands", 184);
        countriesMap.put("Colombia", 170);
        countriesMap.put("Comoros", 174);
        countriesMap.put("Cabo Verde", 132);
        countriesMap.put("Costa Rica", 188);
        countriesMap.put("Cuba", 192);
        countriesMap.put("Curaçao", 531);
        countriesMap.put("Christmas Island", 162);
        countriesMap.put("Cayman Islands", 136);
        countriesMap.put("Cyprus", 196);
        countriesMap.put("Czechia", 203);
        countriesMap.put("Germany", 276);
        countriesMap.put("Djibouti", 262);
        countriesMap.put("Dominica", 212);
        countriesMap.put("Denmark", 208);
        countriesMap.put("Dominican Republic", 214);
        countriesMap.put("Algeria", 12);
        countriesMap.put("Ecuador", 218);
        countriesMap.put("Egypt", 818);
        countriesMap.put("Eritrea", 232);
        countriesMap.put("Western Sahara", 732);
        countriesMap.put("Spain", 724);
        countriesMap.put("Estonia", 233);
        countriesMap.put("Ethiopia", 231);
        countriesMap.put("Finland", 246);
        countriesMap.put("Fiji", 242);
        countriesMap.put("Falkland Islands (Malvinas)", 238);
        countriesMap.put("France", 250);
        countriesMap.put("Faroe Islands", 234);
        countriesMap.put("Micronesia, Federated States of", 583);
        countriesMap.put("Gabon", 266);
        countriesMap.put("United Kingdom", 826);
        countriesMap.put("Georgia", 268);
        countriesMap.put("Guernsey", 831);
        countriesMap.put("Ghana", 288);
        countriesMap.put("Gibraltar", 292);
        countriesMap.put("Guinea", 324);
        countriesMap.put("Guadeloupe", 312);
        countriesMap.put("Gambia", 270);
        countriesMap.put("Guinea-Bissau", 624);
        countriesMap.put("Equatorial Guinea", 226);
        countriesMap.put("Greece", 300);
        countriesMap.put("Grenada", 308);
        countriesMap.put("Greenland", 304);
        countriesMap.put("Guatemala", 320);
        countriesMap.put("French Guiana", 254);
        countriesMap.put("Guam", 316);
        countriesMap.put("Guyana", 328);
        countriesMap.put("Hong Kong", 344);
        countriesMap.put("Heard Island and McDonald Islands", 334);
        countriesMap.put("Honduras", 340);
        countriesMap.put("Croatia", 191);
        countriesMap.put("Haiti", 332);
        countriesMap.put("Hungary", 348);
        countriesMap.put("Indonesia", 360);
        countriesMap.put("Isle of Man", 833);
        countriesMap.put("India", 356);
        countriesMap.put("British Indian Ocean Territory", 86);
        countriesMap.put("Ireland", 372);
        countriesMap.put("Iran, Islamic Republic of", 364);
        countriesMap.put("Iraq", 368);
        countriesMap.put("Iceland", 352);
        countriesMap.put("Israel", 376);
        countriesMap.put("Italy", 380);
        countriesMap.put("Jamaica", 388);
        countriesMap.put("Jersey", 832);
        countriesMap.put("Jordan", 400);
        countriesMap.put("Japan", 392);
        countriesMap.put("Kazakhstan", 398);
        countriesMap.put("Kenya", 404);
        countriesMap.put("Kyrgyzstan", 417);
        countriesMap.put("Cambodia", 116);
        countriesMap.put("Kiribati", 296);
        countriesMap.put("Saint Kitts and Nevis", 659);
        countriesMap.put("Korea, Republic of", 410);
        countriesMap.put("Kuwait", 414);
        countriesMap.put("Lao People's Democratic Republic", 418);
        countriesMap.put("Lebanon", 422);
        countriesMap.put("Liberia", 430);
        countriesMap.put("Libya", 434);
        countriesMap.put("Saint Lucia", 662);
        countriesMap.put("Liechtenstein", 438);
        countriesMap.put("Sri Lanka", 144);
        countriesMap.put("Lesotho", 426);
        countriesMap.put("Lithuania", 440);
        countriesMap.put("Luxembourg", 442);
        countriesMap.put("Latvia", 428);
        countriesMap.put("Macao", 446);
        countriesMap.put("Saint Martin (French part)", 663);
        countriesMap.put("Morocco", 504);
        countriesMap.put("Monaco", 492);
        countriesMap.put("Moldova, Republic of", 498);
        countriesMap.put("Madagascar", 450);
        countriesMap.put("Maldives", 462);
        countriesMap.put("Mexico", 484);
        countriesMap.put("Marshall Islands", 584);
        countriesMap.put("North Macedonia", 807);
        countriesMap.put("Mali", 466);
        countriesMap.put("Malta", 470);
        countriesMap.put("Myanmar", 104);
        countriesMap.put("Montenegro", 499);
        countriesMap.put("Mongolia", 496);
        countriesMap.put("Northern Mariana Islands", 580);
        countriesMap.put("Mozambique", 508);
        countriesMap.put("Mauritania", 478);
        countriesMap.put("Montserrat", 500);
        countriesMap.put("Martinique", 474);
        countriesMap.put("Mauritius", 480);
        countriesMap.put("Malawi", 454);
        countriesMap.put("Malaysia", 458);
        countriesMap.put("Mayotte", 175);
        countriesMap.put("Namibia", 516);
        countriesMap.put("New Caledonia", 540);
        countriesMap.put("Niger", 562);
        countriesMap.put("Norfolk Island", 574);
        countriesMap.put("Nigeria", 566);
        countriesMap.put("Nicaragua", 558);
        countriesMap.put("Niue", 570);
        countriesMap.put("Netherlands", 528);
        countriesMap.put("Norway", 578);
        countriesMap.put("Nepal", 524);
        countriesMap.put("Nauru", 520);
        countriesMap.put("New Zealand", 554);
        countriesMap.put("Oman", 512);
        countriesMap.put("Pakistan", 586);
        countriesMap.put("Panama", 591);
        countriesMap.put("Pitcairn", 612);
        countriesMap.put("Peru", 604);
        countriesMap.put("Philippines", 608);
        countriesMap.put("Palau", 585);
        countriesMap.put("Papua New Guinea", 598);
        countriesMap.put("Poland", 616);
        countriesMap.put("Puerto Rico", 630);
        countriesMap.put("Korea, Democratic People's Republic of", 408);
        countriesMap.put("Portugal", 620);
        countriesMap.put("Paraguay", 600);
        countriesMap.put("Palestine, State of", 275);
        countriesMap.put("French Polynesia", 258);
        countriesMap.put("Qatar", 634);
        countriesMap.put("Réunion", 638);
        countriesMap.put("Romania", 642);
        countriesMap.put("Russian Federation", 643);
        countriesMap.put("Rwanda", 646);
        countriesMap.put("Saudi Arabia", 682);
        countriesMap.put("Sudan", 729);
        countriesMap.put("Senegal", 686);
        countriesMap.put("Singapore", 702);
        countriesMap.put("South Georgia and the South Sandwich Islands", 239);
        countriesMap.put("Saint Helena, Ascension and Tristan da Cunha", 654);
        countriesMap.put("Svalbard and Jan Mayen", 744);
        countriesMap.put("Solomon Islands", 90);
        countriesMap.put("Sierra Leone", 694);
        countriesMap.put("El Salvador", 222);
        countriesMap.put("San Marino", 674);
        countriesMap.put("Somalia", 706);
        countriesMap.put("Saint Pierre and Miquelon", 666);
        countriesMap.put("Serbia", 688);
        countriesMap.put("South Sudan", 728);
        countriesMap.put("Sao Tome and Principe", 678);
        countriesMap.put("Suriname", 740);
        countriesMap.put("Slovakia", 703);
        countriesMap.put("Slovenia", 705);
        countriesMap.put("Sweden", 752);
        countriesMap.put("Eswatini", 748);
        countriesMap.put("Sint Maarten (Dutch part)", 534);
        countriesMap.put("Seychelles", 690);
        countriesMap.put("Syrian Arab Republic", 760);
        countriesMap.put("Turks and Caicos Islands", 796);
        countriesMap.put("Chad", 148);
        countriesMap.put("Togo", 768);
        countriesMap.put("Thailand", 764);
        countriesMap.put("Tajikistan", 762);
        countriesMap.put("Tokelau", 772);
        countriesMap.put("Turkmenistan", 795);
        countriesMap.put("Timor-Leste", 626);
        countriesMap.put("Tonga", 776);
        countriesMap.put("Trinidad and Tobago", 780);
        countriesMap.put("Tunisia", 788);
        countriesMap.put("Turkey", 792);
        countriesMap.put("Tuvalu", 798);
        countriesMap.put("Taiwan, Province of China", 158);
        countriesMap.put("Tanzania, United Republic of", 834);
        countriesMap.put("Uganda", 800);
        countriesMap.put("Ukraine", 804);
        countriesMap.put("United States Minor Outlying Islands", 581);
        countriesMap.put("Uruguay", 858);
        countriesMap.put("United States", 840);
        countriesMap.put("Uzbekistan", 860);
        countriesMap.put("Holy See (Vatican City State)", 336);
        countriesMap.put("Saint Vincent and the Grenadines", 670);
        countriesMap.put("Venezuela, Bolivarian Republic of", 862);
        countriesMap.put("Virgin Islands, British", 92);
        countriesMap.put("Virgin Islands, U.S.", 850);
        countriesMap.put("Viet Nam", 704);
        countriesMap.put("Vanuatu", 548);
        countriesMap.put("Wallis and Futuna", 876);
        countriesMap.put("Samoa", 882);
        countriesMap.put("Yemen", 887);
        countriesMap.put("South Africa", 710);
        countriesMap.put("Zambia", 894);
        countriesMap.put("Zimbabwe", 716);

        return countriesMap;

    }
}
