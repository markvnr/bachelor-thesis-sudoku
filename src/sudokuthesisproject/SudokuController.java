package sudokuthesisproject;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import static sudokuthesisproject.SudokuConstants.*;

public class SudokuController {

    private final Pane view;
    private final SudokuCell[][] grid;
    private SudokuCell selected;
    private final Button[] buttons;
    private final Button btnCheck;
    private final Button btnClear;
    private final MenuBar menuBar;

    private int[][] model;

    public SudokuController() {
        this.view = new Pane();
        this.menuBar = new MenuBar();
        this.grid = new SudokuCell[9][9];
        this.buttons = new Button[9];
        this.btnCheck = new Button();
        this.btnClear = new Button();
        this.selected = null;
    }

    private void createSudoku() {
        this.model = SudokuUtils.createSudokuSolution();
        SudokuUtils.puzzlize(this.model);
        for (int col = 0; col < SIZE; col++) {
            for (int row = 0; row < SIZE; row++) {
                this.grid[row][col] = new SudokuCell(row, col);
                int value = this.model[col][row];
                if (value != 0) {
                    this.grid[row][col].lock();
                    this.grid[row][col].assign(value);
                }
                this.grid[row][col].setOnMouseClicked(this::handleSudokuCellClick);
                this.view.getChildren().add(this.grid[row][col]);
            }
        }
    }

    private void drawVerticalLines() {
        for (int i = GRID_PADDING; i <= LIMIT; i += 3 * CELL_SIZE) {
            Line line = new Line();
            line.setStartX(i);
            line.setStartY(GRID_PADDING);
            line.setEndX(i);
            line.setEndY(LIMIT);
            line.setStrokeWidth(3);
            this.view.getChildren().add(line);
        }
    }

    private void drawHorizontalLines() {
        for (int i = GRID_PADDING; i <= LIMIT; i += 3 * CELL_SIZE) {
            Line line = new Line();
            line.setStartX(GRID_PADDING);
            line.setStartY(i);
            line.setEndX(LIMIT);
            line.setEndY(i);
            line.setStrokeWidth(3);
            this.view.getChildren().add(line);
        }
    }

    private void createNumericButtons() {
        for (int i = 0; i < SIZE; i++) {
            this.buttons[i] = new Button();
            this.buttons[i].setText(String.valueOf(i + 1));
            this.buttons[i].setTranslateX(30 + 25 * i);
            this.buttons[i].setTranslateY(360);
            this.buttons[i].setOnAction(this::handleNumericButtonClick);
            this.view.getChildren().add(buttons[i]);
        }
    }

    private void createCheckButton() {
        this.btnCheck.setText("check");
        this.btnCheck.setTranslateX(254);
        this.btnCheck.setTranslateY(360);
        this.btnCheck.setOnAction(this::handleCheckButtonClick);
        this.view.getChildren().add(this.btnCheck);
    }

    private void handleNumericButtonClick(ActionEvent evt) {
        Button btn = (Button) evt.getSource();
        if (this.selected == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("warnning");
            alert.setHeaderText("No Selected Cell");
            alert.setContentText("you have to select a cell before you assign a number");
            alert.showAndWait();
        } else {
            if (!selected.isLocked()) {
                selected.assign(Integer.valueOf(btn.getText()));
                this.model[this.selected.getY()][this.selected.getX()] = Integer.valueOf(btn.getText());
            }
        }
    }

    private void handleClearButtonClick(ActionEvent evt) {
        if (this.selected == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("warnning");
            alert.setHeaderText("No Selected Cell");
            alert.setContentText("you have to select a cell before you clear a cell");
            alert.showAndWait();
        } else {
            if (!selected.isLocked()) {
                selected.clear();
                this.model[this.selected.getY()][this.selected.getX()] = 0;
            }
        }
    }

    private void handleCheckButtonClick(ActionEvent evt) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        if (SudokuUtils.isComplete(this.model)) {
            if (SudokuUtils.isValid(this.model)) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("information");
                alert.setHeaderText("excellent!!!");
                alert.setContentText("you have solved the sudoku!");
                alert.showAndWait();
            } else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("you made a mistake!!!");
                alert.setContentText("check your solution again!");
                alert.showAndWait();
            }
        } else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("you have not completed the sudoku");
            alert.setContentText("some cells are empty");
            alert.showAndWait();
        }
    }

    private void handleSudokuCellClick(MouseEvent evt) {
        if (this.selected == null) {
            this.selected = (SudokuCell) evt.getSource();
            this.selected.highlight(Color.LIGHTBLUE);
        } else {
            this.selected.highlight(Color.WHITE);
            this.selected = (SudokuCell) evt.getSource();
            this.selected.highlight(Color.LIGHTBLUE);
        }
    }

    private void handleNewGameMenuItemClick(ActionEvent evt) {
        unlockAll();
        createSudoku();
        drawVerticalLines();
        drawHorizontalLines();
        this.selected = null;

    }

    private void createClearButton() {
        this.btnClear.setText("Clear");
        this.btnClear.setTranslateX(302);
        this.btnClear.setTranslateY(360);
        this.btnClear.setOnAction(this::handleClearButtonClick);
        this.view.getChildren().add(this.btnClear);
    }

    private void initMenu() {
        Menu gameMenu = new Menu("Game");
        MenuItem newGameMenuItem = new MenuItem("New Game...");
        newGameMenuItem.setOnAction(this::handleNewGameMenuItemClick);
        gameMenu.getItems().add(newGameMenuItem);
        this.menuBar.getMenus().add(gameMenu);
        this.view.getChildren().add(this.menuBar);
    }

    public void initUI() {
        this.view.setPrefSize(360, 400);
        initMenu();
        createSudoku();
        drawVerticalLines();
        drawHorizontalLines();
        createNumericButtons();
        createCheckButton();
        createClearButton();
    }

    public Pane getView() {
        return this.view;
    }

    public void unlockAll() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.grid[i][j].unlock();
            }
        }
    }
}
