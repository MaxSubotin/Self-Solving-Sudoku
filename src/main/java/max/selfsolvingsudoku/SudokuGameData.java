package max.selfsolvingsudoku;

import java.util.Date;

public class SudokuGameData {
    private int[][] gameArray;
    private int[][] solutionArray;

    public SudokuGameData(int[][] gameArray, int[][] solutionArray) {
        this.gameArray = gameArray;
        this.solutionArray = solutionArray;
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

