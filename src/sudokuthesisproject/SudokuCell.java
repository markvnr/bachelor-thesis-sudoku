package sudokuthesisproject;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.*;

import static sudokuthesisproject.SudokuConstants.*;

public class SudokuCell extends StackPane {

    private final int x;
    private final int y;
    private final Rectangle background;
    private final Text value;
    private boolean locked;

    public SudokuCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.locked = false;
        this.background = new Rectangle(CELL_SIZE, CELL_SIZE);
        this.background.setFill(Color.WHITE);
        this.background.setStroke(Color.BLACK);
        this.background.setStrokeWidth(1);
        this.value = new Text();
        this.value.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        this.value.setFill(Color.BLACK);

        setTranslateX(GRID_PADDING + x * CELL_SIZE);
        setTranslateY(GRID_PADDING + y * CELL_SIZE);
        getChildren().addAll(background, value);

    }

    public void assign(int value) {
        if (this.locked) {
            this.value.setFill(Color.BLACK);
        } else {
            this.value.setFill(Color.BLUE);
        }

        this.value.setText(String.valueOf(value));
    }

    public void highlight(Color color) {
        this.background.setFill(color); 
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }
    
    public void clear(){
        this.value.setText("");
    }

    public boolean isLocked() {
        return this.locked;
    }

    public int getX(){
        return this.x;
    }
    
        public int getY(){
        return this.y;
    }
}
