package ParkingGarageSim;
import java.util.Random;

public class Car {
	
	private double exitTime;
	private double serviceTime;
	private boolean outlier;
	Random randy = new Random();
	
	Car(double serviceTime, double exitTime, boolean outlier){
		this.exitTime = exitTime;
		this.serviceTime = serviceTime;
		this.outlier = outlier;
		if(outlier == true) {
			this.exitTime += randy.nextDouble()*30000;
		}
	}
	
	public double getExitTime(){
		return exitTime;
	}
	
	public double serviceTime(){
		return serviceTime;
	}

}
