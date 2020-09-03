package sudokuthesisproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SudokuApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        SudokuController controller = new SudokuController();
        controller.initUI();
        Scene scene = new Scene(controller.getView());
        primaryStage.setResizable(false);
        primaryStage.setTitle("Sudoku Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
