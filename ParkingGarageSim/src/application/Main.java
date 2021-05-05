package application;
	
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class Main extends Application {

	private boolean isStarted = false;
	public OutputStream out = null;
	public InputStream in = null;
	private String boxText = "Output Here";
	BufferedReader reader = null;
	private Timer test;
	private static int lengthOfSim = 60;
	private boolean running = true;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try { // *for text file reading
			in = new FileInputStream("test.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		reader = new BufferedReader(new InputStreamReader(in)); // *reads the file
		
		// Real time Output Stream
		TextArea output = new TextArea();
		output.setMinSize(300, 150);
		output.setPrefSize(500, 250);
		output.setText(boxText);

	
		// Custom Stop Time box
		Label timeLabel = new Label("Custom Stop Time:");
		TextField timeText = new TextField();
		timeText.setText("0:00");
		timeText.setAlignment(Pos.CENTER);
		timeText.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER && !isStarted) {
					System.out.println("Custom Text: " + timeText.getText());
					Main.lengthOfSim = Integer.valueOf(timeText.getText());
				}
			}
		});
		VBox timeBox = new VBox(10, timeLabel, timeText);
		timeBox.setMinSize(50, 25);
		timeBox.setPrefSize(210, 50);
		timeBox.setStyle("-fx-font-size: 2em; ");

		// Length of Q1 box
		Label q1Label = new Label("Length of Q1");
		TextField q1Text = new TextField();
		VBox q1Box = new VBox(10, q1Label, q1Text);
		q1Box.setStyle("-fx-font-size: 2em; ");

		// Length of Q2 box
		Label q2Label = new Label("Length of Q2");
		TextField q2Text = new TextField();
		VBox q2Box = new VBox(10, q2Label, q2Text);
		q2Box.setStyle("-fx-font-size: 2em; ");

		// Average wait time box
		Label avgLabel = new Label("Average Service Time");
		TextField avgText = new TextField();
		VBox avgBox = new VBox(10, avgLabel, avgText);
		avgBox.setStyle("-fx-font-size: 2em; ");
		
		// Start Button
		Button startBtn = new Button();
		startBtn.setText("Start");
		startBtn.setMinSize(50, 25);
		startBtn.setPrefSize(210, 50);
		startBtn.setStyle("-fx-font-size: 2em; ");
		startBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				isStarted = true;
				System.out.println("The Sim has started");

				try {
					
					//JavaFX thread timer
				    Thread timerThread = new Thread(() -> {
				    	
				    	// a normal car spends 6 to 11 seconds passing through the gate
				    	long waitMIN = 6000;
				    	long waitMAX = 11000;
				    	// at rush hour, a car approaches the gate roughly every 15 seconds
				    	long carSpawnRate = 15000;
				    	// 22% of cars exceed the normal time spent at the gate (get stuck)
				    	double carStuckRate = 0.22;
				    	
				    	// placeholder values
				    	long carEntryTime = 0;
				    	long averageServiceTime = 0;
				    	long totalServiceTime = 0;
				    	int totalCarVolume = 0;
				    	long[] servTimeArray = {0};
				    	final StringBuilder b = new StringBuilder();
				    	Random randy = new Random();
				    	
				    	// creates two queues (ArrayList) of Car type, one for each gate
				    	ArrayList<Car> queue = new ArrayList<>();
				    	ArrayList<Car> queue2 = new ArrayList<>();
				    	
				        while (running) {
				        	
				            try {
				                Thread.sleep(1000); //1 second
				            } catch (InterruptedException e) {
				                e.printStackTrace();
				            }
				            
				            // formats date an time
				        	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			            	LocalDateTime now = LocalDateTime.now();
				        	// checks if a car gets through the gate (out of the system)
							if (queue.size() > 0) {// does gate 1 have a car ready to exit?
								Car served = queue.get(0);
								if (served.getExitTime() < System.currentTimeMillis()) {
									// car leaves queue
									queue.remove(0);
									String temp = "Car Left the Queue for Gate 1 at: " + dtf.format(now)
											+ "\nGate 1's Queue Length is: " + ((queue.size() - 1 == -1) ? (0) : (queue.size() - 1))
											+ "\nAverage Service Time: " + timeToString(averageServiceTime) + "\n\n";
									b.append(temp);

								}
							}
							if (queue2.size() > 0) {// does gate 2 have a car ready to exit?
								Car served2 = queue2.get(0);
								if (served2.getExitTime() < System.currentTimeMillis()) {
									// car leaves queue
									queue2.remove(0);
									b.append("Car Left the Queue for Gate 2 at: " + dtf.format(now));
									b.append(
											"\nGate 2's Queue Length is: " + ((queue2.size() - 1 == -1) ? (0) : (queue2.size() - 1)));
									// Shows new average service time
									b.append("\nAverage Service Time: " + timeToString(averageServiceTime) + "\n\n");
								}
							}
							// checks if a new car appears at the gates
							if (carEntryTime == 0) {
								carEntryTime = System.currentTimeMillis() + carSpawnRate;
							}
							if (carEntryTime < System.currentTimeMillis()) {
								boolean stuck = ((randy.nextDouble() <= carStuckRate) ? (true) : (false));
								if (stuck) {
								}
								double serviceTime = (((Math.random() * (waitMAX - waitMIN)) + waitMIN));
								Car C = new Car(serviceTime, (serviceTime + System.currentTimeMillis()), stuck);
								totalCarVolume++;
								totalServiceTime += C.serviceTime();
								// calculates live average service time including the newly entered car
								averageServiceTime = (totalServiceTime / totalCarVolume);
								servTimeArray[0] = averageServiceTime;

								if (queue.size() > queue2.size()) {// check: is the second gate line shorter?
									// if so, add to queue2
									queue2.add(C);

									b.append("Car Entered the Queue for Gate 2 at: " + dtf.format(now));
									b.append(
											"\nGate 2's Queue Length is: " + ((queue2.size() - 1 == -1) ? (0) : (queue2.size() - 1)) + "\n\n");
									carEntryTime = System.currentTimeMillis() + carSpawnRate;
								} else {
									// else, add to queue1
									queue.add(C);

									b.append("Car Entered the Queue for Gate 1 at: " + dtf.format(now));
									b.append(
											"\nGate 1's Queue Length is: " + ((queue.size() - 1 == -1) ? (0) : (queue.size() - 1)) + "\n\n");
									carEntryTime = System.currentTimeMillis() + carSpawnRate;
								}

							}
							
				            Platform.runLater(() -> {
				            	output.setText(b.toString());
				            	q1Text.setText(String.valueOf(((queue.size() - 1 == -1) ? (0) : (queue.size() - 1))));
				            	q2Text.setText(String.valueOf(((queue2.size() - 1 == -1) ? (0) : (queue.size() - 1))));
				            	avgText.setText(String.valueOf(timeToString(servTimeArray[0])));
				            });
				        }
				    });   timerThread.start();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		// Stop Button
		Button stopBtn = new Button();
		stopBtn.setText("Stop");
		stopBtn.setMinSize(50, 25);
		stopBtn.setPrefSize(210, 50);
		stopBtn.setStyle("-fx-font-size: 2em; ");
		stopBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				isStarted = false;
				System.out.println("The Sim has stopped");
				running = false;
				
			}
		});


		// Put everything together
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

	private void update(TextArea output, TextField q1Text, TextField q2Text, TextField avgText) {
		//All thats needed is getters in Timers to be updated to give actual lengths 
		//Length of q1
		q1Text.setText(test.getQ1Length() + ""); //updates # in the text field
		//Length of q2
		q2Text.setText(test.getQ2Length() + ""); //updates # in the text field
		//Average wait time
		avgText.setText(test.getAvgLengthString() + "");
		
		//the output box stuff
		try {			
			StringBuilder builder = new StringBuilder();
			String line;		
			while(( line = reader.readLine()) != null)
			{
				builder.append(line);
				builder.append(System.lineSeparator());
			}
			output.setText(builder.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String timeToString(long ms) {
		String formatted = (ms * (0.001)) + " Seconds";
		return formatted;
	}

}

