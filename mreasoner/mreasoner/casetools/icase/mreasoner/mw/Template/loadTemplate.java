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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
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
import edu.casetools.icase.mreasoner.mw.sensors.BLE.BLESensor;


public  class loadTemplate {

  static String IPSERVER  = "10.12.102.61"; //10.12.102.61
//  final String state; 
  final	static String head="states(";	
  final static String isDef="is("; 
  final static String hold="holdsAt(#"; 
  final static String end= ");\n"; 
  
  static String stateParameters="";
  static int numStates = 0;
  static String stateIs = "";
  static String stateHolds = "";
  static String templateRules = "";

  
  
  public static void main(String[] args) {
	  //loadInterfaceConfigutaion();
	  try {
		System.out.println(load("C:/Users/Josegines1/Documents/MReasoner/TestingFilesReasoner/Master/Template.txt"));
	} catch (IOException | JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
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
	  	
	 //type 1 
	  if(!stateParameters.contains(state)){
		  stateParameters = stateParameters + state + ",";
	  }
	  	
	  	if(type==1){
	  		if(!stateIs.contains(state)){
	  			stateIs = stateIs + isDef + state + end;
		  		stateIs = stateIs + isDef + "#" + state + end;
			  	stateHolds = stateHolds + hold + state + ",0" +  end;	
	  		}
	  		
	  	}else{
	  		if(!stateHolds.contains(state)){
	  			stateHolds = stateHolds + hold + state + ",0" +  end;
	  		}
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
				
			   //loadVeraSensors(); //load the sensor
			  // loadBLESensors();
		
			   
			   templateRules =  loadTemplateText(file);
			   loadInterfaceConfigutaion();
			   loadSensors();
			   
		 	   
			   stateParameters = stateParameters.substring(0, stateParameters.length() - 1) + end; //Close the head of state declaration
			   return stateParameters + stateIs + stateHolds + templateRules;
			/*   System.out.println(stateParameters );
			   System.out.println(stateIs );
			   System.out.println(stateHolds );
			   System.out.println(rules );*/
			}
  
  public static void loadSensors() {
	  
	  dbConnection db = new dbConnection();
	  ResultSet resultSet=null;
	  String query = "Select state from sensors";
	  String state = "";
	  try {
			db.connect();
		    resultSet = db.executeQuery(query, "select");
	  
		  while (resultSet.next()) {
			    state = resultSet.getString(1);
			    if(templateRules.contains(state)){
			    	addStateTemplate(state,1);
			    	//sensors.add(resultSet.getString(1));	
			    }
			    
		  }
		    
		    
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
 /* public static void loadVeraSensors() throws IOException, JSONException{
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
  }*/
  

  
  public static String loadTemplateText(String file){
	  // String FILENAME = "C:/Users/Josegines1/Documents/MReasoner/TestingFilesReasoner/ActivitiesTemplate.txt"; 
	   /** read template **/
	   BufferedReader br = null;
	   FileReader fr = null;
	   String rules = "";
	
		try {
	
			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(file);
			br = new BufferedReader(fr);
	
			String sCurrentLine;
			String state;
	
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.print("LINEA" + sCurrentLine );
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
		return rules;
  }

  public static void loadInterfaceConfigutaion(){
	 
	 /** Times */
	 String url = "https://" + IPSERVER + "/interface/api/loadtimes.php";
	 disableSslVerification();
	 JSONArray times;
	 String ruleTemp = "ssr((clockBetween($1-$2)) -> $ctx);\n";
	 String notRuleTemp = "ssr((clockBetween($3-$4)) -> #$ctx);\n";
	 JSONArray jsonArray;	 
	 
	 HashMap<String, String> contexts = new HashMap<String, String>();
	 contexts.put("eating","eatSchedule");
	 contexts.put("sleeping","sleepSchedule");
	 contexts.put("wandering","wanderingSchedule");
	 contexts.put("elopement","elopementSchedule");
	 
	 try {
		jsonArray = readJsonArrayFromUrl(url);
		LocalTime tl1;
		LocalTime tl2;
		HashMap<String, LocalTime> lastTime = new HashMap<String, LocalTime>();
		
		for(int i=0; i<jsonArray.length(); i++){
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			String context = jsonObj.get("name").toString();
			String  t1 =jsonObj.get("time1").toString();
			String  t2 =jsonObj.get("time2").toString();
			String rule="";
			String notrule="";
			
			rule = ruleTemp.replace("$ctx", contexts.get(context));
			rule = rule.replace("$1", t1).replace("$2",t2);
			addStateTemplate(contexts.get(context),2);
			
			if(lastTime.get(context)!=null){ //it is second or more
			
				notrule = notRuleTemp.replace("$ctx",  contexts.get(context));
				tl1 = lastTime.get(context).plusSeconds(1);
				tl2 = LocalTime.parse(t1).plusSeconds(-1);
				notrule = notrule.replace("$3", tl1.toString() ).replace("$4",tl2.toString()); 
			}else{ // it is the first
				if (!t1.equals("00:00:00")){
					notrule = notRuleTemp.replace("$ctx",  contexts.get(context));
					tl2= LocalTime.parse(t1).plusSeconds(-1);
					notrule = notrule.replace("$3", "00:00:00").replace("$4",tl2.toString());
				
				}
			}
			templateRules = templateRules + notrule + rule;
			lastTime.put(context, LocalTime.parse(t2));
			
		}
		
		//fulfill the schedules at the bigining of the day and at the end
		for(HashMap.Entry<String, String> entry : contexts.entrySet()) {
		    String ctx = entry.getKey();
		    String ctxSch = entry.getValue();
		
		    if (lastTime.get(ctx) == null){
		        System.out.println("\n******* "  + ctx + " -1- " + ctxSch);
		    	String rule = ruleTemp.replace("$ctx", ctxSch);
				templateRules = templateRules + rule.replace("$1", "00:00:00").replace("$2","23:59:59") + "\n";
				addStateTemplate(ctxSch, 2);
		    }else{
		    	if(!lastTime.get(ctx).toString().equals("23:59")){
		    		 System.out.println("\n******* "  + ctx + " -2- " + lastTime.get(ctx));
		    		String notrule = notRuleTemp.replace("$ctx", ctxSch);
		    		notrule = notrule.replace("$3", lastTime.get(ctx).plusMinutes(1).toString()).replace("$4","23:59:59");
		    		templateRules = templateRules +  notrule;
		    	}
		    }
		}
		
	
	
		
		
		 /** Alerts */
		 url = "https://" + IPSERVER + "/interface/api/loadAlerts.php";
		jsonArray = readJsonArrayFromUrl(url);
		 String typeAlert="";
		 String ctx = "";
		// System.out.println(templateRules);
		 for(int i=0; i<jsonArray.length(); i++){
			
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				String context = jsonObj.get("context").toString();
				String  type =jsonObj.get("type").toString();
				String  time =jsonObj.get("time").toString();
				if(type.equals("1")){
					typeAlert="User";
				}
				if(type.equals("2")){
					typeAlert="Carer";
				}
				switch(context){
					case "1":
						ctx="eat";
						break;
					case "2":
						ctx="sleep";
						break;
					case "3":
						ctx="wandering";
						break;
					case "4":
						ctx="elopement";
						break;
					default:
						break;
				}
				if(type.equals("3")){
					 templateRules = templateRules.replaceAll("\\$wanderingTime", time);
					 
				}else{
					 String replace = "\\$"+ctx+"Alert"+typeAlert;
				//	 System.out.println("MODIFICANDO " + replace);
					templateRules = templateRules.replaceAll(replace, time);
					//templateRules = templateRules.replaceAll("ssrw", "JODER");
				}
				
		 }
	//	 templateRules = templateRules.replaceAll("\\$wanderingTime", "10");	
		 //System.out.println(templateRules);
		
	} catch (IOException | JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		 
	//   System.out.println(json.toString());
	   
	  	
  }
 
  static void disableSslVerification() {
	    try
	    {
	        // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	            public void checkClientTrusted(X509Certificate[] certs, String authType) {
	            }
	            public void checkServerTrusted(X509Certificate[] certs, String authType) {
	            }
	        }
	        };

	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (KeyManagementException e) {
	        e.printStackTrace();
	    }
	}

}
