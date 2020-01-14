package edu.casetools.icase.mreasoner.mw.sensors.BLE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.casetools.icase.mreasoner.mw.dbconnection.*;
import edu.casetools.icase.mreasoner.mw.sensors.vera.Device;
import edu.casetools.icase.mreasoner.ram.Sensor;


public class loadBLEStates {
	
  static   HashMap<String, String> devicesList = new HashMap<String, String>();
  static dbConnection db = new dbConnection();

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      //System.out.println(jsonText.substring(1, jsonText.length()-1));
      JSONObject json = new JSONObject("{\"devices\":" + jsonText + "}");
      return json;
    } finally {
      is.close();
    }
  }

  public static void load() throws IOException, JSONException{
	  
	     BLESensor.disableSslVerification();
	  	 String url = "https://" + BLESensor.IPSERVER + "/beacon_localisation/services/getStates.php";
	   // JSONObject json = readJsonFromUrl("http://10.12.102.156:3480/data_request?id=status&output_format=json");
		 JSONObject json = readJsonFromUrl(url);
		 
		 String id="";
		 String state = "";
	
	    JSONArray devices;
	 
	    devices = json.getJSONArray("devices");
	    for(int n = 0; n < devices.length(); n++)
	    {
	   // 	System.out.println(devices.getJSONObject(n).toString());
	        JSONObject object = devices.getJSONObject(n);
	      
	        id = object.getString("iduser") + "-" + object.getString("deviceNumber");
	        state = object.getString("name") + object.getString("location");
	        // Put elements to the map
	        if (!devicesList.containsKey(id)) {
	        	devicesList.put(id, state);
	        }
	        
	   }
	    
	    db();
  }
  
  public static void main(String[] args)  {
	
	  try {
		load();
	} catch (IOException | JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public static void db(){
	  
		  String insert_sensors = "Insert into sensors (device, implementation, state) VALUES ";
		  String insert_sensor_implementation="Insert into sensor_implementations (name, max_value, min_value, has_boolean_values,type,class, variable) VALUES ('position', '1','0', true, 'beacon','BLE','room')";
		  
		 
		  for (Entry<String, String> entry : devicesList.entrySet()) {
			  insert_sensors = insert_sensors + "('" +  entry.getKey() + "','position','" + entry.getValue() + "'),";
			//  System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
				
			}
		 
		  insert_sensors = (insert_sensors.substring(0,insert_sensors.length() - 1) + ";").replace("[","").replace("]","");
		  System.out.println(insert_sensors);
		//  insert_sensor_implementation = (insert_sensor_implementation.substring(0,insert_sensor_implementation.length() - 1) + ";").replace("[","").replace("]","");
		  System.out.println(insert_sensor_implementation);
		  try {
			db.connect();
			db.executeQuery("Delete sensors where implementation='position'","");
			db.executeQuery(insert_sensors,"");
			db.executeQuery(insert_sensor_implementation,"");
		
			db.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	  
	 
  }
  
  
}

