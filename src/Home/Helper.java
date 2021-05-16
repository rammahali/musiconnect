package Home;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.security.MessageDigest;
public class Helper {
    private Helper() {
    }
    public static boolean execute(String query) {
        try {
            System.out.println("query = " + query);
            System.out.println("Running query...");

            Statement st = App.connection.createStatement();
            st.execute(query);

            System.out.println("query successfully executed!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

    /**
     * Method to execute queries with expected results. e.g.
     *      SELECT x FROM y;
     * @param query string of SQL query without semicolon
     * @return ResultSet , e.g. list of albums
     */
    public static ResultSet executeQuery(String query) {
        ResultSet rs = null;

        try {
            System.out.println("query = " + query);
            System.out.println("Running query...");

            Statement st = App.connection.createStatement();
            rs = st.executeQuery(query);

            System.out.println(rs.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
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
}
