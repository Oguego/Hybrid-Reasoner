package edu.casetools.icase.mreasoner.mw.sensors.vera;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.casetools.icase.mreasoner.mw.dbconnection.*;


public class loadDevices {
	
  static ArrayList<Device> devicesList = new ArrayList<Device>();
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
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }

  public static void load() throws IOException, JSONException{
	  String url = "http://10.12.102.56:3480/data_request?id=user_data&output_format=json";
	   // JSONObject json = readJsonFromUrl("http://10.12.102.156:3480/data_request?id=status&output_format=json");
		 JSONObject json = readJsonFromUrl(url);
		
		 
	//   System.out.println(json.toString());
	    JSONArray devices;
	    JSONArray states;
	    devices = json.getJSONArray("devices");
	    for(int n = 0; n < devices.length(); n++)
	    {
	        JSONObject object = devices.getJSONObject(n);
	        String type=object.get("device_type").toString();
	      //  System.out.println(type);
	        if(type.contains(":")){
	        	type = type.split(":")[type.split(":").length-2]; //get the useful part of the string
	        }

	     /*   if (!type.equals("MotionSensor") && !type.equals("DoorSensor") && !type.equals("BinaryLight")){
	        	continue;
	        }
	        //Switch and energy have the same device_type, so if variable watts exists into states means it is a energy type.
	        if (type.equals("BinaryLight")){
		        states = object.getJSONArray("states");
		        for(int s=0;s < states.length(); s++){
		        	JSONObject state = states.getJSONObject(s);
		           	if(state.get("variable").equals("Watts")){
		        		type="Energy";
		        	}
		        }
	        }*/
	        
	        switch (type){
		        case "MotionSensor":
		        	type="motion";
		        	break;
		        case "DoorSensor":
		        	type="reed";
		        	break;
		        case "BinaryLight":
		        	states = object.getJSONArray("states");
			        for(int s=0;s < states.length(); s++){
			        	JSONObject state = states.getJSONObject(s);
			           	if(state.get("variable").equals("Watts")){
			        		type="energy";
			        	}
			           	else{
			           		type="light";
			           	}
			        }
		        	break;
		        default:
		        	 continue;
	        	
	        }
	      //  state = states.getJSONObject(1);
	        Device device = new Device(object.get("id").toString(),object.get("name").toString(),type);
	        // System.out.println(device.getId());
	        devicesList.add(device);
	      
	        
	    //    System.out.println(state.getString("service"));
	       
	        
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
		  //String insert_sensor_implementation="Insert into sensor_implementations (name, max_value, min_value, has_boolean_values, type, variable, class) VALUES ";
		  
		  for(int i=1; i < devicesList.size(); i++){
			  insert_sensors = insert_sensors + "(" + devicesList.get(i).getId() + ",'" + devicesList.get(i).getType() + "','" + devicesList.get(i).getName().replaceAll("\\s","") + "'),";
			  //insert_sensors = insert_sensors + "(" + devicesList.get(i).getId() + ",'" + devicesList.get(i).getName().replaceAll("\\s","") + "','" + devicesList.get(i).getName().replaceAll("\\s","")+ "','" + devicesList.get(i).getType() + "','" + devicesList.get(i).getVariable() + "'),";
			//  insert_sensor_implementation = insert_sensor_implementation + "('" + devicesList.get(i).getName().replaceAll("\\s","") + "', 0,1,false, '" + devicesList.get(i).getType() + "','" + devicesList.get(i).getVariable() + "', 'vera')," ;
			  //System.out.println(devicesList.get(i).getId() + " -  " + devicesList.get(i).getName() + " -  " + devicesList.get(i).getType() + " -  " + devicesList.get(i).getVariable());
		  }
		  insert_sensors = (insert_sensors.substring(0,insert_sensors.length() - 1) + ";").replace("[","").replace("]","");
		//  insert_sensor_implementation = (insert_sensor_implementation.substring(0,insert_sensor_implementation.length() - 1) + ";").replace("[","").replace("]","");
		//  System.out.print(insert_sensors); 
		  try {
			db.connect();
			String o = "delete from sensors where \"implementation\" in (select name from sensor_implementations where class='vera')";
			db.executeQuery(o, "");
		//	System.out.print(o);
			
			db.executeQuery(insert_sensors,"");
		//	db.executeQuery(insert_sensor_implementation,"");
		
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

