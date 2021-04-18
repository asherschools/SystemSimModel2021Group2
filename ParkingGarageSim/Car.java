package ParkingGarageSim;
import java.util.Random;

public class Car {
	
	private int ID;
	private double exitTime;
	private boolean outlier;
	Random randy = new Random();
	
	Car(int ID, double exitTime, boolean outlier){
		this.ID = ID;
		this.exitTime = exitTime;
		this.outlier = outlier;
		if(outlier == true) {
			this.exitTime += randy.nextDouble()*30000;
		}
	}
	
	public int getID(){
		return ID;
	} 
	
	public double getExitTime(){
		return exitTime;
	}

}
