package final_project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Clean_Data {

	public static void main(String[] args) {
		try {
			String pathname = "car.arff";
			// read file
			File filename = new File(pathname);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			
			// write file
			File writename = new File("cars.arff");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			
			String line = "";  
			String temp = "";
            line = br.readLine();
            while (line != null) {  
            	temp = changeData(line);
            	String[] strings = temp.split(",");
            	if(strings.length == 7){
            		if(strings[5].equals("2") && strings[6].equals("vgood")){
            			System.out.println("@@@");
            		}
            	}
            	System.out.println(temp);
            	out.write(temp+"\r\n");
            	
                line = br.readLine();  
            }
            br.close();
            out.flush();
            out.close();
            
            
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static String changeData(String string){
		string = string.replaceAll("v-high", "5").replaceAll("vhigh", "5").replaceAll("5-more", "5").replaceAll("5more", "5");
		string = string.replaceAll("more", "5");
		string = string.replaceAll("high", "4").replaceAll("big", "4");
		string = string.replaceAll("med", "3");
		string = string.replaceAll("small", "2").replaceAll("low", "2");
		return string;
	}

}
