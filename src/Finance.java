import java.util.*;

public class Finance extends FileReading{
	
	private Vector<Double> twentyDaySums = new Vector<Double>();//vectors created to store running totals
	private Vector<Double> simpleMovingAverage = new Vector<Double>();
	private Vector<Double> exponentialMovingAverage = new Vector<Double>();
	private Vector<Double> twentyDayStandardDeviation = new Vector<Double>();
	
	Finance(){
		getData();//reads the necessary info from the file
		twentyDaySums = addedTotals(getAdjustedClose());//gets the sum of the previous 20 days
		simpleMovingAverage = computeSMA(null);//gets the simple average across 20 days
		//computeEMA();
		//computeStandardDeviation();
	}
	
	private Vector<Double> addedTotals(Vector<Double> passedVector){
		Vector<Double> addedVector = new Vector<Double>(); 
		addedVector.clear(); //lets vector be used by multiple other methods
		int i = 0;
		double addedValue = 0;
		
		for(double dailyAdjustedClose: passedVector){
			addedValue += dailyAdjustedClose; //stores value of previous days while i is under 20
			if(i > 19){ //once i hits 20 removes the oldest value and adds the newest
				addedValue -= passedVector.get(i-20);
			}
			addedVector.add(addedValue);
			i++;
		}
		//for(double dailyAdjustedValue: addedVector){
			//System.out.println(dailyAdjustedValue);
		//}
		return addedVector;
		//System.out.println("\n\n\n");
	}
	
	protected Vector<Double> computeSMA(Vector<Double> passedVector){
		Vector<Double> avg = new Vector<Double>();//doubles as method to get general avg and simple moving avg
		avg.clear();
		if(passedVector == null){//If nothing is passed then use existing known data
			passedVector = twentyDaySums;
		}
		for(int i = 0; i <= 19; i++){//works out data for dates with less than 20 days prior
			avg.add(passedVector.get(i)/(i+1));
			//System.out.println(avg.get(i));
		}
		for(int i = 20; i < passedVector.size(); i++){//works out data for all data with 20 dates prior
			avg.add(passedVector.get(i)/20);
			//System.out.println(avg.get(i));
		}
		return avg;
	}
	
	protected void computeEMA(){//computes exponential moving average across 20 day span and less when data is missing
		exponentialMovingAverage.add(simpleMovingAverage.get(0));
		
		for(int i = 1; i <= 19; i++){
			exponentialMovingAverage.add((getAdjustedClose().get(i) - exponentialMovingAverage.get(i - 1))
										* 2.0/(i + 1.0) + exponentialMovingAverage.get(i - 1));
			//System.out.println(exponentialMovingAverage.get(i));
		}
		for(int i = 20; i < simpleMovingAverage.size(); i++){
			exponentialMovingAverage.add((getAdjustedClose().get(i) - exponentialMovingAverage.get(i - 1))
										* 2.0/21.0 + exponentialMovingAverage.get(i - 1));
			//System.out.println(exponentialMovingAverage.get(i));
		}
	}
	
	protected void computeStandardDeviation(){//standard deviaiton across 20 days and less when data is missing
		for(int i = 0; i < simpleMovingAverage.size(); i++){
			double subtractedSMA = getAdjustedClose().get(i) - simpleMovingAverage.get(i);
			twentyDayStandardDeviation.add(subtractedSMA * subtractedSMA);
			//System.out.println(twentyDayStandardDeviation.get(i));
		}
		twentyDayStandardDeviation = addedTotals(twentyDayStandardDeviation);
		twentyDayStandardDeviation = computeSMA(twentyDayStandardDeviation);
		
		for(int i = 0; i < twentyDayStandardDeviation.size(); i++){
			twentyDayStandardDeviation.set(i, Math.sqrt(twentyDayStandardDeviation.get(i)));
			//System.out.println(twentyDayStandardDeviation.get(i));
		}
	}
	
	protected void printNewData(){//sends vectors with new data to filereading object to be printed
		printToFile(simpleMovingAverage, exponentialMovingAverage, twentyDayStandardDeviation);
	}
	
	protected void close(){//Clears all vectors for updated version that allows multiple files to be processed
		twentyDaySums.clear();
		simpleMovingAverage.clear();
		exponentialMovingAverage.clear();
		twentyDayStandardDeviation.clear();
		closeFile();
	}
	
}
