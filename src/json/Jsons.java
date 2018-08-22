package json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Jsons {
	 public static void main(String[] args) {

	        JSONParser parser = new JSONParser();

	        try {     
	            Object obj = parser.parse(new FileReader("C:\\work\\Miscellaneous\\src\\config.json"));
	            JSONObject jsonObject =  (JSONObject) obj;
	            Map<String,JSONArray> hmap = new HashMap<String,JSONArray>();
	            JSONArray makerobj = (JSONArray) jsonObject.get("Maker");
	            JSONArray checkerobj = (JSONArray) jsonObject.get("Checker");
	            hmap.put("navigationItems", makerobj);
	            JSONObject result = new JSONObject();
	            result.putAll(hmap);
	            System.out.println(result.toJSONString());
	            if(!result.isEmpty()) {
	            	for (Map.Entry<String, JSONArray> entry : hmap.entrySet())
	            	{
	            	    System.out.println(entry.getValue());
	            	}
	            }
	            
	            
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	    }
}
