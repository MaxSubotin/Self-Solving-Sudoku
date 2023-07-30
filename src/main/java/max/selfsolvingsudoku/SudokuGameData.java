package max.selfsolvingsudoku;

import java.util.Date;

public class SudokuGameData {
    private int[][] gameArray;
    private int[][] solutionArray;

    private String difficulty, timer;
    private int mistakes;

    public SudokuGameData(int[][] gameArray, int[][] solutionArray, String difficulty, String timer, int mistakes) {
        this.gameArray = gameArray;
        this.solutionArray = solutionArray;
        this.difficulty = difficulty;
        this.timer = timer;
        this.mistakes = mistakes;
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }
}

