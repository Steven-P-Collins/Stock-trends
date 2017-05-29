import java.io.*;
import java.util.*;

public class FileReading {
	final private String opener = "Type the path of the file you wish to open."
			+ "\nUse the form C:/Users/Steven/Desktop/MyFile.csv" //Opening statement
			+ "\nPlease make sure the file is also saved as .csv";
	
	private static File userFile; //stores the 
	private FileReader FR;
	private Scanner fin;
	
	private Vector<Double> adjustedClose = new Vector<Double>();
	static private Vector<String> entireFile = new Vector<String>();
	
	public String getOpener(){
		return opener;
	}
	
	public Boolean openFile(String fileName){//checks to see if path exists
		userFile = new File(fileName);
		
		if(!userFile.exists()){
			System.out.println("Not a valid file.\nPlease try another file path.");
			return false;
		}
		else{
			return userFile.exists();
		}
	}
	
	protected void getData(){//gets data from file called by user
		try{
			FR = new FileReader(userFile);
			fin = new Scanner(FR);
			
			String check = fin.nextLine();
			String[] checkWord = check.split(",");//splits the lines on commas
			
			if(checkWord.length > 7 && checkWord[7].equals("20 Day SMA")){//cehck to see if file has been ran through this program
				System.out.println("This file has already been updated with this data.\n"
						+ "Please try another file.");
				System.exit(0);
			}
			
			entireFile.add(check.concat(",20 Day SMA,20 Day EMA,20 Day SD"));//adds new data calumns to first line
			//Concats first line with updated columns
				
			//System.out.println(entireFile.get(0));
				
			while(fin.hasNext()){	
				String line = fin.nextLine();
				entireFile.add(line);//saves the entire file to a vector to be updated later
		
				String[] lineSplit = line.split(",");
				adjustedClose.add(Double.valueOf(lineSplit[5]));//splits on fifth comma to get adjusted close info
			}
		}
		catch(Exception e){
			//System.out.println(e);
			System.out.println("There was an error reading your file.\nGoodbye.");
			System.exit(1);
		}
	}
	
	protected Vector<Double> getAdjustedClose(){
		return adjustedClose;
	}
	
	public String fileName(){
		return userFile.getName();
	}
	
	protected void printToFile(Vector<Double> SMA, Vector<Double> EMA, Vector<Double> SD){//accepts 3 vectors to print to file
		for(int i = 0; i < SMA.size();i++){
			String newData = String.format(",%f,%f,%f",SMA.get(i),EMA.get(i),SD.get(i));
			entireFile.set(i+1, entireFile.get(i+1).concat(newData));//adds new data to old file vector
			//System.out.println(entireFile.get(i));
		}
		try{
			PrintWriter outputStream = new PrintWriter(userFile);
			for(int i = 0; i < entireFile.size(); i++){//prints the updated file to the old file
				outputStream.println(entireFile.get(i));
			}
			outputStream.close();
		}catch(Exception e){
			System.out.println("Error printing to file. Goodbye");
			System.exit(0);
		}
	}
	protected void closeFile(){//erases/closes everything for future file reading
		try{
			FR.close();
			fin.close();
			adjustedClose.clear();
			entireFile.clear();
			//System.out.println("it worked");
		}
		catch(Exception e){
			System.out.println("");
		}
	}
}
