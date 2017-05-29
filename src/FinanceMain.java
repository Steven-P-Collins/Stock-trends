import java.util.*;

public class FinanceMain{
	
	public static void main(String[] args){
		FileReading File1 = new FileReading();
		
		System.out.printf("%s\n",File1.getOpener());
		
		Scanner Keyboard = new Scanner(System.in);
		String userInfo;
		
		do{
			userInfo = Keyboard.nextLine();//lets user enter file until they enter proper format
		}while(!File1.openFile(userInfo));
		Keyboard.close();
		
		Finance Finance1 = new Finance(); // creates new finance object
	
		Finance1.computeEMA();
		Finance1.computeStandardDeviation();
		Finance1.printNewData();//prints the updated data to the existing file
		
		System.out.printf("Your file has been updated with the 20 day exponential moving average, \n"
				+ "the 20 day simple moving average, and the 20 day standard deviation. \nThe file is "
				+ "saved in the same location as: %s",File1.fileName());
		
		Finance1.close();//closes and clears everything for future files to be updated
		
	}
}
