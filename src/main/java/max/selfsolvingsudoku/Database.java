package max.selfsolvingsudoku;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {


    public static void addUserToDatabase(DatabaseConfig config, String userUsername, String userPassword) {
        int Id;
        String Username = userUsername;
        String Password = userPassword;

        String lastIdQuery = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
        String insertQueryUsers = "INSERT INTO users(id, username, password) VALUES(?, ?, ?)";
        String insertQueryUsersInfo = "INSERT INTO users_info(id, username, solved, mistakes) VALUES(?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement getLastIdPst = con.prepareStatement(lastIdQuery);
             ResultSet rs = getLastIdPst.executeQuery();
             PreparedStatement insertPstUsers = con.prepareStatement(insertQueryUsers);
            PreparedStatement insertPstUsersInfo = con.prepareStatement(insertQueryUsersInfo)) {

            Id = -1;

            if (rs.next()) {
                Id = rs.getInt("id") + 1;
            }

            insertPstUsers.setInt(1, Id);
            insertPstUsers.setString(2, Username);
            insertPstUsers.setString(3, Password);
            insertPstUsers.executeUpdate();

            insertPstUsersInfo.setInt(1, Id);
            insertPstUsersInfo.setString(2, Username);
            insertPstUsersInfo.setInt(3, 0);
            insertPstUsersInfo.setInt(4, 0);
            insertPstUsersInfo.executeUpdate();

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Database.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static boolean isUsernameUnique(DatabaseConfig config, String userUsername) {
        String Username = userUsername;

        String findUserQuery = "SELECT COUNT(*) as count FROM users WHERE username = ?";
        boolean usernameExists = false;

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement pst = con.prepareStatement(findUserQuery)) {

            pst.setString(1, Username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");
                if (count > 0) {
                    usernameExists = true;
                }
            }

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Database.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

        return usernameExists;
    }

    public static boolean isPasswordCorrectForUsername(DatabaseConfig config, String userUsername, String userPassword) {
        String query = "SELECT COUNT(*) as count FROM users WHERE username = ? AND password = ?";
        boolean credentialsCorrect = false;

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, userUsername);
            pst.setString(2, userPassword);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");
                if (count > 0) {
                    credentialsCorrect = true;
                }
            }

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Database.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
        }

        return credentialsCorrect;
    }


}
