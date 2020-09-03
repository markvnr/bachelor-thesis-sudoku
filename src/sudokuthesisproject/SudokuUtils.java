package sudokuthesisproject;

import static java.lang.System.out;
import java.util.*;

public class SudokuUtils {

    public static void swapRows(int[][] grid,int row1,int row2){
        int temp;
        for(int j=0;j<9;j++){
            temp = grid[row1][j];
            grid[row1][j] = grid[row2][j];
            grid[row2][j] = temp;
        }
    }
    
    public static void swapColumns(int[][] grid,int column1,int column2){
        int temp;
        for(int i=0;i<9;i++){
            temp = grid[i][column1];
            grid[i][column1] = grid[i][column2];
            grid[i][column2] = temp;
            
        }
    }
    
    public static void randomizeRows(int[][] grid){
        int low,high,first,second;
        Random r = new Random();
        for(int i=0;i<9;i+=3){
            low = i;
            high = i+3;
            first = r.nextInt(high-low) + low;
            second = r.nextInt(high-low) + low;
            swapRows(grid,first,second);
        }
    }
    
    public static void randomizeColumns(int[][] grid){
        int low,high,first,second;
        Random r = new Random();
        for(int i=0;i<9;i+=3){
            low = i;
            high = i+3;
            first = r.nextInt(high-low) + low;
            second = r.nextInt(high-low) + low;
            swapColumns(grid,first,second);
        }
    }
    
    public static void shiftArray(int[] array, int amount) {
        for (int j = 0; j < amount; j++) {
            int a = array[0];
            int i;
            for (i = 0; i < array.length - 1; i++) {
                array[i] = array[i + 1];
            }
            array[i] = a;
        }
    }

    public static void shiftRow(int[][] mat, int rowIndex, int amount) {
        for (int j = 0; j < amount; j++) {
            int a = mat[rowIndex][0];
            int i;
            for (i = 0; i < mat[0].length - 1; i++) {
                mat[rowIndex][i] = mat[rowIndex][i + 1];
            }
            mat[rowIndex][i] = a;
        }
    }

    public static void duplicateRow(int[][] mat, int rowIndexFrom, int rowIndexTo) {
        for (int j = 0; j < mat[0].length; j++) {
            mat[rowIndexTo][j] = mat[rowIndexFrom][j];
        }
    }

    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (i != array.length - 1) {
                out.print(array[i] + ", ");
            } else {
                out.print(array[i]);
            }

        }
    }

    public static void printMatrix(int[][] grid) {
        for (int[] row : grid) {
            printArray(row);
            out.println();
        }
    }

    public static int[][] createSudokuSolution() {
        int[][] sudoku = new int[9][9];
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Collections.shuffle(list);

        
        for (int i = 0; i < 9; i++) {
            sudoku[0][i] = list.get(i);
        }

        for (int i = 1; i < 9; i++) {
            duplicateRow(sudoku, i - 1, i);
            if (i % 3 == 1 || i % 3 == 2) {
                shiftRow(sudoku, i, 3);
            } else {
                shiftRow(sudoku, i, 1);
            }
        }
        
        randomizeRows(sudoku);
        randomizeColumns(sudoku);

        return sudoku;
    }

    public static void puzzlize(int[][] sudoku) {
        Set<String> pairs = new HashSet<>();
        Random rand = new Random();
        while (pairs.size() != 81) {
            int rowIndex = rand.nextInt(9);
            int columnIndex = rand.nextInt(9);
            String pair = rowIndex + "," + columnIndex;
            if (!pairs.contains(pair)) {
                pairs.add(pair);
                if (candidatesFor(sudoku, rowIndex, columnIndex).size() == 1) {
                    sudoku[rowIndex][columnIndex] = 0;
                }
            }
        }
    }

    public static int indexOfInRow(int[][] mat, int row, int value) {

        for (int column = 0; column < mat[0].length; column++) {
            if (mat[row][column] == value) {
                return column;
            }
        }

        return -1;
    }

    public static int indexOfInColumn(int[][] mat, int column, int value) {

        for (int row = 0; row < mat[0].length; row++) {
            if (mat[row][column] == value) {
                return row;
            }
        }

        return -1;
    }

    public static int[] findEmpty(int[][] sudoku) {
        int[] index = new int[2];
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[0].length; j++) {
                if (sudoku[i][j] == 0) {
                    index[0] = i;
                    index[1] = j;
                    return index;
                }
            }
        }
        return null;
    }

    public static Set<Integer> candidatesFor(int[][] sudoku, int row, int column) {
        Set<Integer> invalid = new HashSet<>();
        Set<Integer> valid = new HashSet<>();

        for (int j = 0; j < sudoku[0].length; j++) {
            if (j != column && sudoku[row][j] != 0) {
                invalid.add(sudoku[row][j]);
            }
        }

        for (int i = 0; i < sudoku.length; i++) {
            if (i != row && sudoku[i][column] != 0) {
                invalid.add(sudoku[i][column]);
            }
        }

        int startRowIndex = row - row % 3;
        int startColumnIndex = column - column % 3;

        for (int i = startRowIndex; i < startRowIndex + 3; i++) {
            for (int j = startColumnIndex; j < startColumnIndex + 3; j++) {
                if (i != row && j != column && sudoku[i][j] != 0) {
                    invalid.add(sudoku[i][j]);
                }
            }
        }

        for (int i = 1; i <= 9; i++) {
            if (!invalid.contains(i)) {
                valid.add(i);
            }
        }

        return valid;
    }

    public static int[][] duplicate(int[][] grid) {
        return Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
    }

    private static boolean rowHasDuplicates(int[][] grid, int row) {
        Set<Integer> set = new HashSet<>();

        for (int j = 0; j < grid[0].length; j++) {
            if (grid[row][j] != 0 && set.add(grid[row][j]) == false) {
                return true;
            }
        }
        return false;
    }

    private static boolean columnHasDuplicates(int[][] grid, int column) {
        Set<Integer> set = new HashSet<>();

        for (int i = 0; i < grid[0].length; i++) {
            if (grid[i][column] != 0 && set.add(grid[i][column]) == false) {
                return true;
            }
        }
        return false;
    }

    private static boolean blockHasDuplicates(int[][] grid, int row, int column) {
        Set<Integer> set = new HashSet<>();

        for (int i = row; i < row + 3; i++) {
            for (int j = column; j < column + 3; j++) {
                if (grid[i][j] != 0 && set.add(grid[i][j]) == false) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isComplete(int[][] sudoku) {
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[0].length; j++) {
                if (sudoku[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValid(int[][] sudoku) {
        for (int i = 0; i < sudoku.length; i++) {
            boolean result1 = rowHasDuplicates(sudoku, i);
            boolean result2 = columnHasDuplicates(sudoku, i);
            if (result1 || result2) {
                return false;
            }
        }

        for (int i = 0; i < sudoku.length; i += 3) {
            for (int j = 0; j < sudoku[0].length; j += 3) {
                boolean result3 = blockHasDuplicates(sudoku, i, j);
                if (result3) {
                    return false;
                }
            }
        }
        return true;
    }

}
