package edu.casetools.icase.mreasoner.mw.Template;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.casetools.icase.mreasoner.mw.dbconnection.*;


public  class loadTemplateBAK {

//  final String state; 
  final	static String head="states(";	
  final static String isDef="is("; 
  final static String hold="holdsAt(#"; 
  final static String end= ");\n"; 
  
  static String stateParameters="";
  static int numStates = 0;
  static String stateIs = "";
  static String stateHolds = "";
  static String rules = "";
  
  
  public static void main(String[] args) {
	
	  
  }
  /******************************/
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
  
  public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONArray json = new JSONArray(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
}
/*********************************/
  
  
  public static void addStateTemplate(String state, int type){
	  	
	  	stateParameters = stateParameters + state + ",";
	  	if(type==1){
	  		stateIs = stateIs + isDef + state + end;
	  		stateIs = stateIs + isDef + "#" + state + end;
		  	stateHolds = stateHolds + hold + state + ",0" +  end;	
	  	}else{
	  		stateHolds = stateHolds + hold + state + ",0" +  end;	
	  	}
	  	
  }
  
 /* public static void main(String[] args) throws IOException, JSONException {
		System.out.println(load());
  }*/
  /**
   * Load from VERA all sensors
   * @param file
   * @return
   * @throws IOException
   * @throws JSONException
   */
  
  public static String load(String file) throws IOException, JSONException{
	  			
	  		  stateParameters=head;
			  stateIs = "";
			  stateHolds = "";
			  rules = "";
			  String url = "http://10.12.102.56:3480/data_request?id=user_data&output_format=json";
			  // JSONObject json = readJsonFromUrl("http://10.12.102.156:3480/data_request?id=status&output_format=json");
			   JSONObject json = readJsonFromUrl(url);
			//   System.out.println(json.toString());
			   JSONArray devices;
			  
			   devices = json.getJSONArray("devices");
			   for(int n = 0; n < devices.length(); n++)
			   {
			       JSONObject object = devices.getJSONObject(n);
			       String type=object.get("device_type").toString();
			    //   System.out.println(type);
			       if(type.contains(":")){
			       	type = type.split(":")[type.split(":").length-2]; //get the useful part of the string
			       }
			       if (!type.equals("MotionSensor") && !type.equals("DoorSensor") && !type.equals("BinaryLight")){
			       	continue;
			       }
			       

			       addStateTemplate(object.get("name").toString().replaceAll("[^a-zA-Z0-9]", ""),1);
			 
			      
			   }
			   
			  // String FILENAME = "C:/Users/Josegines1/Documents/MReasoner/TestingFilesReasoner/ActivitiesTemplate.txt"; 
			   /** read template **/
			   BufferedReader br = null;
				FileReader fr = null;
			
				try {
			
					//br = new BufferedReader(new FileReader(FILENAME));
					fr = new FileReader(file);
					br = new BufferedReader(fr);
			
					String sCurrentLine;
					String state;
			
					while ((sCurrentLine = br.readLine()) != null) {
					//	System.out.print("LINEA" + sCurrentLine );
						if(sCurrentLine.contains("->")){
							state = sCurrentLine.substring(sCurrentLine.lastIndexOf(">") + 1, sCurrentLine.indexOf(");")).trim();
							state = state.replace("#","");
						//	System.out.println("  -- STATE " + state );
						    if(!stateParameters.contains("," + state + ",")){
								addStateTemplate(state, 2);
							}
							
						}
						rules = rules + sCurrentLine + "\n"; //add even the comments
						
					}
			
				} catch (IOException e) {
			
					e.printStackTrace();
			
				} finally {
			
					try {
			
						if (br != null)
							br.close();
			
						if (fr != null)
							fr.close();
			
					} catch (IOException ex) {
			
						ex.printStackTrace();
			
					}
			
				}
			   
			   
			   stateParameters = stateParameters.substring(0, stateParameters.length() - 1) + end;
			   
			   return stateParameters + stateIs + stateHolds + rules;
			/*   System.out.println(stateParameters );
			   System.out.println(stateIs );
			   System.out.println(stateHolds );
			   System.out.println(rules );*/
			}


}
