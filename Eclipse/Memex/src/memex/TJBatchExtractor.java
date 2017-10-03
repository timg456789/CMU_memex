package memex;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class TJBatchExtractor {

	public static void main(String[] args) throws Exception {
		
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("Example_text.txt"), "UTF-8");
		BufferedReader bufferedFileReader = new BufferedReader(inputStreamReader);
		
		BackpageAdParser backpageAdParser = new BackpageAdParser();
		GateOutputModel result = backpageAdParser.Parse(
				Runtime.getRuntime().availableProcessors(), bufferedFileReader);
		
		PrintWriter writer = new PrintWriter("Out.csv","UTF-8");
		writer.println("Perspective_1st,Perspective_3rd,Name,Age,Cost,Height_ft,Height_in,Weight,Cup,Chest,Waist,Hip,Ethnicity,SkinColor,EyeColor,HairColor,Restriction_Type,Restriction_Ethnicity,Restriction_Age,PhoneNumber,AreaCode_State,AreaCode_Cities,Email,Url,Media");
		for(String l : result.results) {
			writer.println(l);
		}
		writer.close();

		PrintWriter writer2 = new PrintWriter("Out.txt","UTF-8");
		for(String l : result.text) {
			writer2.println(l);
		}
		writer2.close();

		System.out.println("All done");
	}
	
}
