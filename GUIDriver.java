package ParkingGarageSim;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class GUIDriver extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		//This shit works 
		/*Timer test = new Timer();
		test.tick(15);
		System.out.println(test.toString());
		*/

		//Start Button
		Button startBtn = new Button();
		startBtn.setText("Start");
		startBtn.setMinSize(50,25);
		startBtn.setPrefSize(210, 50);
		startBtn.setStyle("-fx-font-size: 2em; ");
		startBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("The Sim has started");
			}
		});
		
		//Stop Button 
		Button stopBtn = new Button();
		stopBtn.setText("Stop");
		stopBtn.setMinSize(50,25);
		stopBtn.setPrefSize(210, 50);
		stopBtn.setStyle("-fx-font-size: 2em; ");
		stopBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("The Sim has stopped");
			}
		});
		
		
		//Custom Stop Time box
		Label timeLabel = new Label("Custom Start Time:");	
		TextField timeText = new TextField();
		timeText.setText("00:00");
		timeText.setAlignment(Pos.CENTER);
		timeText.setOnKeyPressed(new EventHandler<KeyEvent>() {	//Just takes in time doesn't do anything with it yet
			
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					
					System.out.println("Custom Text: " + timeText.getText());
				}
				
				
			}
		});
		VBox timeBox = new VBox(10, timeLabel, timeText);
		timeBox.setMinSize(50, 25);
		timeBox.setPrefSize(210, 50);
		timeBox.setStyle("-fx-font-size: 2em; ");
		
		//Generate Graph Button
		
				
		//Length of Q1 box
		Label q1Label = new Label("Length of Q1");
		TextField q1Text = new TextField();
		VBox q1Box = new VBox(10, q1Label, q1Text);
		q1Box.setStyle("-fx-font-size: 2em; ");
		
		//Length of Q2 box
		Label q2Label = new Label("Length of Q2");
		TextField q2Text = new TextField();
		VBox q2Box = new VBox(10, q2Label, q2Text);
		q2Box.setStyle("-fx-font-size: 2em; ");
		
		//Average wait time box
		Label avgLabel = new Label("Average wait time");
		TextField avgText = new TextField();
		VBox avgBox = new VBox(10, avgLabel, avgText);
		avgBox.setStyle("-fx-font-size: 2em; ");
		
		//Real time Output Stream
		TextField output = new TextField();
		output.setMinSize(300,150);
		output.setPrefSize(500, 250);	
						
		
		//Put everything together
		VBox left = new VBox(50, startBtn, stopBtn, timeBox);
		VBox right = new VBox(50, q1Box, q2Box, avgBox);
		VBox center = new VBox(50, output);
		HBox everything = new HBox(50, left, center, right);
		everything.setLayoutX(50);
		everything.setLayoutY(130);
		
		Pane root = new Pane();
		root.getChildren().add(everything);		
		Scene scene = new Scene(root, 1200, 520);

		primaryStage.setTitle("Parking Garage Simulation");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
