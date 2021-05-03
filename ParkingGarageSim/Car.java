package ParkingGarageSim;
import java.util.Random;

public class Car {
	
	private double exitTime;
	private double serviceTime;
	private int stuckTimeMax = 150000; //A car can be stuck for up to an extra 2 and 1/2 minutes
	private double stuckTime = 0;
	Random randy = new Random();
	
	Car(double serviceTime, double exitTime, boolean outlier){
		this.exitTime = exitTime;
		this.serviceTime = serviceTime;
		if(outlier == true) {
			stuckTime = randy.nextDouble()*stuckTimeMax;
			this.exitTime += stuckTime;
		}
	}
	
	public double getExitTime(){
		return exitTime;
	}
	
	public double serviceTime(){
		return serviceTime + stuckTime;
	}

}
