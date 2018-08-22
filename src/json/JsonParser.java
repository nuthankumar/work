package json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {

    public static void main(String[] args) {

        JSONParser parser = new JSONParser();

        try {     
            Object obj = parser.parse(new FileReader("C:\\work\\Miscellaneous\\src\\config.json"));
            String array[] = {"Maker","Checker"};
            JSONObject jsonObject =  (JSONObject) obj;
            
            merger(array,jsonObject);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
	private static void merger(String[] array,JSONObject jsonObject) {
		JSONArray result= new JSONArray();
		JSONArray role = new JSONArray();
        if(array.length<1) {
        	System.out.println("Empty");
        }
		if(array.length==1) {
			result = (JSONArray) jsonObject.get(array[0]);
		}
		if(array.length>1) {
			result = (JSONArray) jsonObject.get(array[0]);
			JSONObject temp = new JSONObject();
	        JSONArray tempArray = new JSONArray();
			for(int i=1;i<array.length;i++) {
				role=(JSONArray) jsonObject.get(array[i]);
				for(int j=0;j<role.size(); j++) {
		        	temp = (JSONObject) role.get(j);
		        	for(int k=0;j<temp.size(); k++) {
		        		temp.get("label");
		        		tempArray = (JSONArray) temp.get("children");
		        		for(int l=0;l<tempArray.size();l++) {
		        			System.out.println(temp.get("label"));
		        		}
		        	}
		        }
			}
			
		}
        
        
        System.out.println(result.toJSONString());
	}
}
