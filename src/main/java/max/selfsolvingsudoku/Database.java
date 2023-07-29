package max.selfsolvingsudoku;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;


public class Database {


    public static void addUserToDatabase(DatabaseConfig config, String userUsername, String userPassword) {
        int Id;
        String Username = userUsername;
        String Password = userPassword;

        String lastIdQuery = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
        String insertQueryUsers = "INSERT INTO users(id, username, password) VALUES(?, ?, ?)";
        String insertQueryUsersInfo = "INSERT INTO users_info(id, username, solved, mistakes, saves_games) VALUES(?, ?, ?, ?, ?)";

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
            insertPstUsersInfo.setInt(5, 1);
            insertPstUsersInfo.executeUpdate();

        } catch (SQLException e) {
            catchBlockCode(e);
        }
    }

    public static Player getUserFromDatabase(DatabaseConfig config, String userUsername) {
        String query = "SELECT * FROM users_info WHERE username = ?";
        Player userFromDatabase = null;

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, userUsername);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                userFromDatabase = new Player(rs.getString("username"), rs.getInt("solved"), rs.getInt("mistakes"));
                userFromDatabase.setSavedGamesCounter(rs.getInt("saved_games"));
            }

        } catch (SQLException e) {
            catchBlockCode(e);
        }

        return userFromDatabase;
    }

    // ------------------------------------------------------------------ //

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
            catchBlockCode(e);
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
            catchBlockCode(e);
        }

        return credentialsCorrect;
    }

    // ------------------------------------------------------------------ //

    public static void saveCurrentGame(DatabaseConfig config, Player currentPlayer, int[][] currentGame, int[][] gameSolution) {
        String saveQuery = "INSERT INTO users_games(username, date, game, solution) VALUES(?, ?, ?::json, ?::json)";
        String updateQuery = "UPDATE users_info SET saved_games = ? WHERE username = ?";

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement pstSave = con.prepareStatement(saveQuery);
             PreparedStatement pstUpdate = con.prepareStatement(updateQuery)) {

            // Get the current date
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = currentDate.format(formatter);

            pstSave.setString(1, currentPlayer.getUsername());
            pstSave.setString(2, formattedDate + "-" + LoginController.currentPlayer.getSavedGamesCounter());
            pstSave.setString(3, new Gson().toJson(currentGame));
            pstSave.setString(4, new Gson().toJson(gameSolution));
            pstSave.executeUpdate();

            LoginController.currentPlayer.setSavedGamesCounter(LoginController.currentPlayer.getSavedGamesCounter() + 1);

            pstUpdate.setInt(1, currentPlayer.getSavedGamesCounter());
            pstUpdate.setString(2, currentPlayer.getUsername());
            pstUpdate.executeUpdate();

        } catch (SQLException e) {
            catchBlockCode(e);
        }
    }

    public static ArrayList<String> getAllUserGameDates(DatabaseConfig config, String userUsername) {
        String query = "SELECT * FROM users_games WHERE username = ?";
        ArrayList<String> dates = new ArrayList<String>();

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1,userUsername);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                dates.add(rs.getString("date"));
            }

        } catch (SQLException e) {
            catchBlockCode(e);
        }

        return dates;
    }

    public static SudokuGameData getGameByUsernameAndDate(DatabaseConfig config, String userUsername, String gameDate) {
        String query = "SELECT * FROM users_games WHERE username = ? AND date = ?";
        SudokuGameData requestedGame = null;

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, userUsername);
            pst.setString(2, gameDate);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                requestedGame = turnJsonIntoArray(rs);
            }

        } catch (SQLException e) {
            catchBlockCode(e);
        }

        return requestedGame;
    }

    public static SudokuGameData getLastGame(DatabaseConfig config, String userUsername) {
        String query = "SELECT * FROM users_games WHERE username = ? ORDER BY date DESC LIMIT 1";
        SudokuGameData requestedGame = null;

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, userUsername);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                requestedGame = turnJsonIntoArray(rs);
            }

        } catch (SQLException e) {
            catchBlockCode(e);
        }
        return requestedGame;
    }

    public static void setUserMistakesCounter(DatabaseConfig config, String userUsername, int count) {
        String query = "UPDATE users_info SET mistakes = ? WHERE username = ?";

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, count);
            pst.setString(2, userUsername);
            pst.executeUpdate();

        } catch (SQLException e) {
            catchBlockCode(e);
        }
    }

    public static void setUserSolvedCounter(DatabaseConfig config, String userUsername, int count) {
        String query = "UPDATE users_info SET solved = ? WHERE username = ?";

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, count);
            pst.setString(2, userUsername);
            pst.executeUpdate();

        } catch (SQLException e) {
            catchBlockCode(e);
        }
    }


    // ------------------------------------------------------------------ //

    public static void deleteRowsByUsername(DatabaseConfig config, String userUsername) {
        String deleteQuery = "DELETE FROM users_games WHERE username = ?";
        String updateQuery = "UPDATE users_info SET saved_games = ? WHERE username = ?";

        try (Connection con = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
             PreparedStatement pstDelete = con.prepareStatement(deleteQuery);
             PreparedStatement pstUpdate = con.prepareStatement(updateQuery)) {

            pstDelete.setString(1, userUsername);
            pstDelete.executeUpdate();

            pstUpdate.setInt(1,1);
            pstUpdate.setString(2,userUsername);
            pstUpdate.executeUpdate();

        } catch (SQLException e) {
            catchBlockCode(e);
        }
    }

    // ------------------------------------------------------------------ //

    private static void catchBlockCode(SQLException e) {
        Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, e.getMessage(), e);
    }

    private static SudokuGameData turnJsonIntoArray(ResultSet rs) throws SQLException {
        String json = rs.getString("game");
        Gson gson = new Gson();
        int[][] first = gson.fromJson(json, int[][].class);

        json = rs.getString("solution");
        gson = new Gson();
        int[][] second = gson.fromJson(json, int[][].class);

        return new SudokuGameData(first, second);
    }

}
