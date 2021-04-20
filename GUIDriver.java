package ParkingGarageSim;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUIDriver extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Timer test = new Timer();
        test.tick(15);
        System.out.println(test.toString());



    }
}