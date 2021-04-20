package ParkingGarageSim;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUIDriver extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("ITS WORKING");

        System.out.println("Maybe its committed now?");
        Timer test = new Timer();
        test.tick();



    }
}