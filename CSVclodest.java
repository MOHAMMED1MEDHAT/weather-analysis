import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;


public class CSVclodest {

	public static CSVRecord lowestHumidityInFile(CSVParser parser) {
		CSVRecord lowestSoFar=null;
		for(CSVRecord currentRecord:parser){
			if (lowestSoFar == null) {
				lowestSoFar = currentRecord;
			}
			else {
				String huStr=currentRecord.get("Humidity");
				if(huStr.equals("N/A")){
					continue;
				}else{
					int currentHumidity = Integer.parseInt(currentRecord.get("Humidity"));
					int lowestHumidity = Integer.parseInt(lowestSoFar.get("Humidity"));
					if (currentHumidity < lowestHumidity) {
						lowestSoFar = currentRecord;
					}
				}
			}
		}
		return lowestSoFar;
	}

    public static CSVRecord coldestHourInFile(CSVParser parser) {
		CSVRecord lowestSoFar = null;
		for (CSVRecord currentRow : parser) {
			lowestSoFar = getLowestOfTwo(currentRow, lowestSoFar,"TemperatureF");
		}
        return lowestSoFar;
    }

	public static CSVRecord getLowestOfTwo (CSVRecord currentRow, CSVRecord lowestSoFar,String comperationString) {
		if (lowestSoFar == null) {
			lowestSoFar = currentRow;
		}
		else {
			double currentTemp = Double.parseDouble(currentRow.get(comperationString));
			double lowestTemp = Double.parseDouble(lowestSoFar.get(comperationString));
			if (currentTemp < lowestTemp && currentTemp > -9999) {
				lowestSoFar = currentRow;
			}
		}
		return lowestSoFar;
	}
	
	public static double averageTemperatureInFile(CSVParser parser){
		double averageTemp=0.0;
		int count=0;
		double totalTemp=0.0;
		for(CSVRecord currentRecord:parser){
			totalTemp+=Double.parseDouble(currentRecord.get("TemperatureF"));
			count++;
		}
		averageTemp=totalTemp/count;
		return averageTemp;
	} 

	public static String averageTemperatureWithHighHumidityInFile(CSVParser parser,int value){
		String result="";
		double averageTemp=0.0;
		int count=0;
		double totalTemp=0.0;
		for(CSVRecord currentRecord:parser){
			int currentHumidity=Integer.parseInt(currentRecord.get("Humidity"));
			if(currentHumidity>=value){
				totalTemp+=Double.parseDouble(currentRecord.get("TemperatureF"));
				count++;
			}
		}
		averageTemp=totalTemp/count;
		result="Average Temp when Humidity is "+value+" temperature is "+Double.toString(averageTemp);
		if(count==0){
			result="No temperatures with that humidity";
		}
		return result;
	}

	public static CSVRecord coldestInManyDays() {
		CSVRecord lowestSoFar = null;
		DirectoryResource dr = new DirectoryResource();
		for (File f : dr.selectedFiles()) {
			FileResource fr = new FileResource(f);
			CSVRecord currentRow = coldestHourInFile(fr.getCSVParser());
			lowestSoFar = getLowestOfTwo(currentRow, lowestSoFar,"TemperatureF");
		}
		return lowestSoFar;
	}

	public static void testAverageTemperatureWithHighHumidityInFile(){
		FileResource fr = new FileResource("nc_weather/2013/weather-2013-09-02.csv");
		String result= averageTemperatureWithHighHumidityInFile(fr.getCSVParser(),80);
		System.out.println(result);
	}

	public static void testColdestInDay () {
		FileResource fr = new FileResource("nc_weather/2014/weather-2014-05-01.csv");
		CSVRecord lowest = coldestHourInFile(fr.getCSVParser());
		System.out.println("coldest temperature was " + lowest.get("TemperatureF") +" at " + lowest.get("TimeEDT"));
	}

	public static void testAverageInFile () {
		FileResource fr = new FileResource("nc_weather/2013/weather-2013-08-10.csv");
		double average = averageTemperatureInFile(fr.getCSVParser());
		System.out.println("Average temperature was " + average);
	}
	
	public static CSVRecord lowestHumidityInManyDays() {
		CSVRecord lowestSoFar = null;
		DirectoryResource dr = new DirectoryResource();
		for (File f : dr.selectedFiles()) {
			FileResource fr = new FileResource(f);
			CSVRecord currentRow = lowestHumidityInFile(fr.getCSVParser());
			lowestSoFar = getLowestOfTwo(currentRow, lowestSoFar,"Humidity");
		}
		return lowestSoFar;
	}


	public static void testColdestInManyDays () {
		CSVRecord lowest = coldestInManyDays();
		System.out.println("coldest temperature was " + lowest.get("TemperatureF") +" at " + lowest.get("DateUTC"));
	}

	public static void testLowestHumidityInManyDays () {
		CSVRecord lowest = lowestHumidityInManyDays();
		System.out.println("lowest Humidity was " + lowest.get("Humidity") +" at " + lowest.get("DateUTC"));
	}


	public static void main(String[] args) {
		testColdestInManyDays();
		// testLowestHumidityInManyDays();
		// testAverageInFile();
		// testAverageTemperatureWithHighHumidityInFile();
		// testColdestInDa3();
	}

}
