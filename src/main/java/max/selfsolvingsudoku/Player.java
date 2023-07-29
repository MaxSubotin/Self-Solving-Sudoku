package max.selfsolvingsudoku;

public class Player {
    private String username;
    private int totalMistakesCounter, solvedPuzzlesCounter;
    private int savedGamesCounter = 1;

    public Player(String username, int solved, int mistakes) {
        setUsername(username);
        setSolvedPuzzlesCounter(solved);
        setTotalMistakesCounter(mistakes);
    }


    public int getTotalMistakesCounter() {
        return totalMistakesCounter;
    }

    public void setTotalMistakesCounter(int totalMistakesCounter) {
        this.totalMistakesCounter = totalMistakesCounter;
    }

    public int getSolvedPuzzlesCounter() {
        return solvedPuzzlesCounter;
    }

    public void setSolvedPuzzlesCounter(int solvedPuzzlesCounter) {
        this.solvedPuzzlesCounter = solvedPuzzlesCounter;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSavedGamesCounter(int savedGamesCounter) {
        this.savedGamesCounter = savedGamesCounter;
    }

    public int getSavedGamesCounter() {
        return savedGamesCounter;
    }
}
