package max.selfsolvingsudoku;
import java.util.Random;

public class Sudoku {
    private int size, SIZE;
    public int[][] game;

    public Sudoku(int _size) {
        size = _size;
        SIZE = size * size;
        game = new int[SIZE][SIZE];
        generateGame();
    }

    private void generateGame() {
        // Set everything to -1 so that it cannot be a value
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                game[i][j] = -1;

        createGame(0, 0);
    }

    private boolean createGame(int x, int y) {
        // If all rows have been processed, a solution is found
        if (y == SIZE) {
            return true;
        }

        // Calculate the next cell coordinates
        int nx = (x + 1) % SIZE;
        int ny = (nx == 0) ? y + 1 : y;

        // Shuffle the order of numbers to try
        int[] nums = new int[SIZE];
        for (int i = 0; i < SIZE; i++)
            nums[i] = i + 1;
        shuffleArray(nums);

        for (int num : nums) {
            // If the number is valid, place it in the cell
            if (isValid(x, y, num)) {
                game[x][y] = num;

                // Recurse to the next cell
                if (createGame(nx, ny)) {
                    return true;
                }

                // Backtrack by resetting the current cell
                game[x][y] = -1;
            }
        }
        return false;
    }


    private boolean isValid(int x, int y, int num) {
        // Check the row and column
        for (int i = 0; i < SIZE; i++) {
            if (game[i][y] == num || game[x][i] == num)
                return false;
        }

        // Check the box
        int bx = x - x % size;
        int by = y - y % size;
        for (int i = bx; i < bx + size; i++) {
            for (int j = by; j < by + size; j++) {
                if (game[i][j] == num)
                    return false;
            }
        }

        return true;
    }

    private boolean isSolved() {
        // Check if the Sudoku puzzle is fully filled and valid
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (game[i][j] == -1 || !isValid(i, j, game[i][j]))
                    return false;
            }
        }
        return true;
    }

    private void shuffleArray(int[] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
