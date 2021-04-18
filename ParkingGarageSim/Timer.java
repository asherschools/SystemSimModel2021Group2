package ParkingGarageSim;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class Timer {

	long waitMIN = 5000;
	long waitMAX = 7000;
	long carSpawnRate = 6000;
	long carEntryTime = 0;
	Random randy = new Random();

	// formats date an time
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	// create a queue (ArrayList) of Customer type
	ArrayList<Car> queue = new ArrayList<>();

	public void tick() {
		boolean run = true;

		while (run) {
			//get current time
			LocalDateTime now = LocalDateTime.now();
			// checks if a car gets through the gate
			if (queue.size() > 0) {
				Car served = queue.get(0);
				if (served.getExitTime() < System.currentTimeMillis()) {
					// car leaves queue
					queue.remove(0);
					System.out.println("Car Left Queue at: " + dtf.format(now));
					System.out.println("Queue Length: " + ((queue.size() - 1 == -1) ? (0):(queue.size() - 1)));
				}
			}

			// checks if a car appears at the gate
			if (carEntryTime == 0) {
				carEntryTime = System.currentTimeMillis() + carSpawnRate;
			}
			if (carEntryTime < System.currentTimeMillis()) {
				boolean stuck = (( randy.nextDouble() <= .15) ? (true):(false));
				Car C1 = new Car(1, (((Math.random() * (waitMAX - waitMIN)) + waitMIN) + System.currentTimeMillis()),
						stuck);
				// add to queue
				queue.add(C1);

				System.out.println("Car Entered Queue at: " + dtf.format(now));
				System.out.println("Queue Length: " + ((queue.size() - 1 == -1) ? (0):(queue.size() - 1)));
				carEntryTime = System.currentTimeMillis() + carSpawnRate;
			}

			try {
				// ticks once per second
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
