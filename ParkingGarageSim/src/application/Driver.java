package application;

import javafx.stage.Stage;

public class Driver {

	public static void main(String[] args) {
			Timer test = new Timer();
			try {
				test.tick();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
