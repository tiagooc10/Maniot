import java.net.*;
import java.io.*;
import java.util.*;

	/*
          Olhe o arquivo README.

	  Step 1: Find the IP address of the brige.

	  Step 2: Register a username as described in
	  http://www.developers.meethue.com/documentation/getting-started. The
	  user name string, to make things standard, should be:

	  {"devicetype":"ManIoT#user"}	  

	  Step 3: Get the user name string, and change it in the
	  appropriate class variable.
	 */


public class ManIoTHueDriver { 

	public static final String ip="150.164.10.57";
	public static final String user="XcZHfLccivHIfVYrn62sQKXRGmQq-IFevXQFc-8q";

	public ManIoTHueDriver() {
       
    	}

	private static boolean sendPutRequest(String httpurl,String body) {
		try {
			Random random = new Random();
			
			URL url = new URL(httpurl);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("PUT");
			connection.setDoOutput(true);
			OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
			osw.write(String.format(body, random.nextInt(30), random.nextInt(20)));
			osw.flush();
			osw.close();
			if(connection.getResponseCode() == 200)
				return true;
			
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;

	}

	private static String sendGetRequest(String urlToRead) {
		try {
			StringBuilder result = new StringBuilder();
			URL url = new URL(urlToRead);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
	public boolean changeOnOff(int lamp, boolean on) {
		String url = "http://"+ip+"/api/"+user+"/lights/"+lamp+"/state";
		String text = "{\"on\":"+on+"}";
		System.out.println(text);
		return sendPutRequest(url,text);
	}

	/* Values: 0 up to 254 */
	public boolean changeBrightness(int lamp, int intensity) {
		String url = "http://"+ip+"/api/"+user+"/lights/"+lamp+"/state";
		String text = "{\"bri\":"+intensity+"}";
		System.out.println(text);
		return sendPutRequest(url,text);
	}

	/* 
	   Color values:
	   http://www.developers.meethue.com/documentation/hue-xy-values.
	   WINET's lamps are LCT001, so gamut is B.
	   
	   @param x - X param in the color value - x must be in the [0,1] range.
 	   @param y - Y param in the color value - y must be in the [0,1] range.
	 */
	public boolean setColor(int lamp, double  x, double y) {

		if(x < 0 || x > 1)
			return false;
		if(y < 0 || y > 1)
			return false;
		
		String url = "http://"+ip+"/api/"+user+"/lights/"+lamp+"/state";
		String text = "{\"xy\": ["+x+","+y+"]}";
		System.out.println(text);
		return sendPutRequest(url,text);

	}

	public String getLampProperties(int lamp) {
		String url = "http://"+ip+"/api/"+user+"/lights/"+lamp;
		return sendGetRequest(url);
	}

	public String getAllLampProperties() {
		String url = "http://"+ip+"/api/"+user+"/lights/";
		return sendGetRequest(url);
	}

}
