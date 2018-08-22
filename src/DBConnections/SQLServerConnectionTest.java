package DBConnections;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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

import org.json.simple.JSONObject;

public class SQLServerConnectionTest {

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {
		String url = "jdbc:sqlserver://nibc2646:1433;databaseName=nuthan;username=nuthan2;password=May@2018";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager.getConnection(url);
		System.out.println(conn);
		
		String sql = "SELECT BLOBData FROM BLOBTest where Testid=1;";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
		Object obj=getObjectFromBlob(rs,"BLOBData");
		}
		
	}
	protected static Object getObjectFromBlob(ResultSet rs, String colName)
		    throws ClassNotFoundException, IOException, SQLException {

		    Blob blobLocator = rs.getBlob(colName);
		    return (Object)decodeCharByteArray(convertBlob(blobLocator),"UTF-8");
		}
	
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
		System.out.println(inputArrayChars.toString());
		return inputArrayChars.toString();
	}
	
}
