package application;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class Timer {

	DataOutputStream out = null;

	// a normal car spends 6 to 11 seconds passing through the gate
	long waitMIN = 6000;
	long waitMAX = 11000;
	// at rush hour, a car approaches the gate roughly every 15 seconds
	long carSpawnRate = 15000;
	// 22% of cars exceed the normal time spent at the gate (get stuck)
	double carStuckRate = 0.22;
	// placeholder values
	long carEntryTime;
	long averageServiceTime;
	long totalServiceTime;
	int totalCarVolume;
	private StringBuilder b = new StringBuilder();

	Random randy = new Random();

	// formats date an time
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	private ArrayList<String> toPrint = new ArrayList<>(); // everything that needs to be displayed as a String

	// creates two queues (ArrayList) of Car type, one for each gate
	ArrayList<Car> queue = new ArrayList<>();
	ArrayList<Car> queue2 = new ArrayList<>();

	public Timer() {
		// I/O Stuff
		try {
			out = new DataOutputStream(new FileOutputStream("test.txt"));

			// out.writeChars("test");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// placeholder values
		carEntryTime = 0;
		averageServiceTime = 0;
		totalServiceTime = 0;
		totalCarVolume = 0;
	}

	public void tick() {
		boolean run = true;

		while (run) {
			// get current time
			LocalDateTime now = LocalDateTime.now();

			// checks if a car gets through the gate (out of the system)
			if (queue.size() > 0) {// does gate 1 have a car ready to exit?
				Car served = queue.get(0);
				if (served.getExitTime() < System.currentTimeMillis()) {
					// car leaves queue
					queue.remove(0);
					String temp = "\nCar Left the Queue for Gate 1 at: " + dtf.format(now)
							+ "\nGate 1's Queue Length is: " + ((queue.size() - 1 == -1) ? (0) : (queue.size() - 1))
							+ "\nAverage Service Time: " + timeToString(averageServiceTime);
					b.append(temp);
					try {
						out.writeChars(temp + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			if (queue2.size() > 0) {// does gate 2 have a car ready to exit?
				Car served2 = queue2.get(0);
				if (served2.getExitTime() < System.currentTimeMillis()) {
					// car leaves queue
					queue2.remove(0);
					b.append("Car Left the Queue for Gate 2 at: " + dtf.format(now));
					b.append(
							"Gate 2's Queue Length is: " + ((queue2.size() - 1 == -1) ? (0) : (queue2.size() - 1)));
					// Shows new average service time
					b.append("Average Service Time: " + timeToString(averageServiceTime));
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

				if (queue.size() > queue2.size()) {// check: is the second gate line shorter?
					// if so, add to queue2
					queue2.add(C);

					b.append("Car Entered the Queue for Gate 2 at: " + dtf.format(now));
					b.append(
							"Gate 2's Queue Length is: " + ((queue2.size() - 1 == -1) ? (0) : (queue2.size() - 1)));
					carEntryTime = System.currentTimeMillis() + carSpawnRate;
				} else {
					// else, add to queue1
					queue.add(C);

					b.append("Car Entered the Queue for Gate 1 at: " + dtf.format(now));
					b.append(
							"Gate 1's Queue Length is: " + ((queue.size() - 1 == -1) ? (0) : (queue.size() - 1)));
					carEntryTime = System.currentTimeMillis() + carSpawnRate;
				}

			}

			try {
				// ticks once per second
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private String timeToString(long ms) {
		String formatted = (ms * (0.001)) + " Seconds";
		return formatted;
	}

	// Modified version of tick to only run for a specified amount of time
	public void tick(int lengthOfSim) throws IOException {
		// boolean run = true;
		int i = 0;
		while (i < lengthOfSim) {
			i++;
			// get current time
			LocalDateTime now = LocalDateTime.now();

			// checks if a car gets through the gate (out of the system)
			if (queue.size() > 0) {// does gate 1 have a car ready to exit?
				Car served = queue.get(0);
				if (served.getExitTime() < System.currentTimeMillis()) {
					// car leaves queue
					queue.remove(0);
					String temp = "\nCar Left the Queue for Gate 1 at: " + dtf.format(now)
							+ "\nGate 1's Queue Length is: " + ((queue.size() - 1 == -1) ? (0) : (queue.size() - 1))
							+ "\nAverage Service Time: " + timeToString(averageServiceTime);
					System.out.println(temp);
					out.writeChars(temp);
				}
			}
			if (queue2.size() > 0) {// does gate 2 have a car ready to exit?
				Car served2 = queue2.get(0);
				if (served2.getExitTime() < System.currentTimeMillis()) {
					// car leaves queue
					queue2.remove(0);
					String temp = "\nCar Left the Queue for Gate 2 at: " + dtf.format(now)
							+ "\nGate 2's Queue Length is: " + ((queue2.size() - 1 == -1) ? (0) : (queue2.size() - 1))
							+ "\nAverage Service Time: " + timeToString(averageServiceTime);
					System.out.println(temp);
					out.writeChars(temp);
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

				if (queue.size() > queue2.size()) {// check: is the second gate line shorter?
					// if so, add to queue2
					queue2.add(C);
					String temp = "\nCar Entered the Queue for Gate 2 at: " + dtf.format(now)
							+ "\nGate 2's Queue Length is: " + ((queue2.size() - 1 == -1) ? (0) : (queue2.size() - 1));
					carEntryTime = System.currentTimeMillis() + carSpawnRate;
					System.out.println(temp);
					out.writeChars(temp);
				} else {
					// else, add to queue1
					queue.add(C);

					String temp = "Car Entered the Queue for Gate 1 at: " + dtf.format(now)
							+ "\nGate 1's Queue Length is: " + ((queue.size() - 1 == -1) ? (0) : (queue.size() - 1));
					carEntryTime = System.currentTimeMillis() + carSpawnRate;
					System.out.println(temp);
					out.writeChars(temp);
				}

			}

			try {
				// ticks once per second
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getQ1Length() {
		return queue.size();
	}

	public int getQ2Length() {

		return queue2.size();
	}

	public String getAvgLengthString() {
		return timeToString(averageServiceTime);
	}

}
