package max.selfsolvingsudoku;

public class DatabaseConfig {
    private String url;
    private String user;
    private String password;

    public DatabaseConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

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

