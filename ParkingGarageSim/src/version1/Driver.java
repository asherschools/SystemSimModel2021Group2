package version1;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		boolean run = true;
		while(run) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			System.out.println(dtf.format(now));  
			
			try {
				
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
