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
            String array[] = {"Maker"};
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

	@SuppressWarnings("unchecked")
	private static void merger(String[] array, JSONObject jsonObject) {
		Map<String, JSONArray> hmap = new HashMap<String, JSONArray>();
		JSONArray navigationItems = new JSONArray();
		JSONObject result = new JSONObject();
		if (array.length < 1) {
			System.out.println("Empty");
		}
		if (array.length == 1) {
			navigationItems = (JSONArray) jsonObject.get(array[0]);
			hmap.put("navigationItems", navigationItems);
			result.putAll(hmap);
		}
		if (array.length > 1) {
			for (String arr : array) {
				JSONArray temp = (JSONArray) jsonObject.get(arr);
				boolean flag = true;
				for(Object obj:temp) {
					JSONObject obj3=(JSONObject)obj;
					for(Object navObj: navigationItems) {
						JSONObject navObj3 = (JSONObject)navObj;
						if(obj3.get("label").equals(navObj3.get("label"))) {
							JSONArray tempArray = (JSONArray)obj3.get("children");
							JSONArray navArray = (JSONArray)navObj3.get("children");
							for(Object obj4 : tempArray) {
								JSONObject test = (JSONObject)obj4;
								if(!navArray.contains((JSONObject)test)) {
									navArray.add(test);
								}
							}
							flag=false;
						}
					}
					if(flag) {
					navigationItems.add((JSONObject)obj);
					}
				}
			}
			System.out.println(navigationItems.toJSONString());
			hmap.put("navigationItems", navigationItems);
			result.putAll(hmap);
		}
		
	}
}
