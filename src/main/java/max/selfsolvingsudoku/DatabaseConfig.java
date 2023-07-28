package max.selfsolvingsudoku;

public class DatabaseConfig {
    private String url = "jdbc:postgresql://localhost:5432/SudokuUsers";
    private String user = "postgres";
    private String password = "148369";

    public DatabaseConfig() {}

    // Getters for url, user, and password
    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}

