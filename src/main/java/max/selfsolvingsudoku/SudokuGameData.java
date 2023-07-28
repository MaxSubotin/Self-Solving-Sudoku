package max.selfsolvingsudoku;

import java.util.Date;

public class SudokuGameData {
    private String date;
    private int[][] gameArray;
    private int[][] solutionArray;

    public SudokuGameData(String date, int[][] gameArray, int[][] solutionArray) {
        this.date = date;
        this.gameArray = gameArray;
        this.solutionArray = solutionArray;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int[][] getGameArray() {
        return gameArray;
    }

    public void setGameArray(int[][] gameArray) {
        this.gameArray = gameArray;
    }

    public int[][] getSolutionArray() {
        return solutionArray;
    }

    public void setSolutionArray(int[][] solutionArray) {
        this.solutionArray = solutionArray;
    }
}

