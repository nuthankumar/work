package DBConnections;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SQLServerConnectionTest {

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException, ParseException {
		String url = "jdbc:sqlserver://nibc2646:1433;databaseName=nuthan;username=nuthan2;password=May@2018";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager.getConnection(url);
		System.out.println(conn);
		
		String sql = "SELECT BLOBData FROM BLOBTest where Testid=1;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		Object obj=null;
		while(rs.next()) {
		obj=getObjectFromBlob(rs,"BLOBData");
		}
		String array[] = {"Maker","Checker","Administrator"};
		JSONObject jsonObject = (JSONObject) new JSONParser().parse(obj.toString());
		JSONObject navigationItems = merger(array,jsonObject);
		System.out.println(navigationItems);
	}
	/**
	 * Method takes the result set and column name to recreate the json from the database blob object containing the configuration json.
	 * 
	 * @param rs result set
	 * @param colName column name for blob data
	 * @return Object (Character buffer string casted to Object)
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	protected static Object getObjectFromBlob(ResultSet rs, String colName) throws ClassNotFoundException, IOException, SQLException {
		    Blob blobLocator = rs.getBlob(colName);
		    return (Object)decodeCharByteArray(convertBlob(blobLocator),"UTF-8");
		}
	/**
	 * Method takes the blob object to read byte by bite to provide it to be converted into characters 
	 * @param blob
	 * @return byte array of blob in binary stream
	 */
	public static byte[] convertBlob(Blob blob) {
		if(blob==null)return null;
		
		try {
		    InputStream in = blob.getBinaryStream();
		    int len = (int) blob.length();  
		    long pos = 1; 
		    byte[] bytes = blob.getBytes(pos, len); 		    
		    in.close();
		    return bytes;	    
	         } catch (Exception e) {
		        System.out.println(e.getMessage());
		 }
		 return null;
	}
	/**
	 * Method for decoding binary stream to characters 
	 * @param inputArray
	 * @param charSet
	 * @return character Buffer string representation
	 */
	public static String decodeCharByteArray(byte[] inputArray, String charSet) { 
	  	Charset theCharset = Charset.forName(charSet);
		CharsetDecoder decoder = theCharset.newDecoder();
		ByteBuffer theBytes = ByteBuffer.wrap(inputArray);
		CharBuffer inputArrayChars = null;
		try {
			inputArrayChars = decoder.decode(theBytes);
		} catch (CharacterCodingException e) {
			System.err.println("Error decoding");
		}
		//System.out.println(inputArrayChars.toString());
		return inputArrayChars.toString();
	}
	/**
	 * Method takes if the user has Single/multiple roles and merge the children tags so that we do not get repeatations
	 * @param array
	 * @param jsonObject
	 * @return JSONObject that is having final navigation JSONArray for dashboard
	 */
	@SuppressWarnings("unchecked")
	private static JSONObject merger(String[] array, JSONObject jsonObject) {
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
			hmap.put("navigationItems", navigationItems);
			result.putAll(hmap);
		}
		return result;
	}
	
}
