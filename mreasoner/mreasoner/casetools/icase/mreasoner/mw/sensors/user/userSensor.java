package edu.casetools.icase.mreasoner.mw.sensors.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import edu.casetools.icase.mreasoner.deployment.sensors.Sensors;

public class userSensor extends Sensors {
	static String IPSERVER  = "10.12.102.61"; //10.12.102.61
	public userSensor(String id, String state, String status, String service,  String variable){
		super(id, state, status, service, variable);
		disableSslVerification();
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getState(){
		return this.state;
	}
	
	public String getStatus(){
		return this.status;
	}
			
	public String getDate(){
		return this.dateFormat.format(this.date).split(" ")[0];
		
	}
	
	public String getTime(){
		return this.dateFormat.format(this.date).split(" ")[1];
	}
	
	public void setStatus(String status){
		this.status = status;
		this.date = new Timestamp(System.currentTimeMillis());
	}
	
	public String getVariable(){
		return this.variable;
	}
	
	public String getService(){
		
		return this.service;
	}
	
	public String poll(){
		//	System.out.println("Polling User posicion " +  this.getId());
			URL service;
			
			URLConnection connection = null;
			//String user = this.getId().substring(0, this.getId().lastIndexOf('-'));
			String url = "https://"+ IPSERVER +"/success/tests/getStatus.php?state=" + this.state ;
			BufferedReader in = null;
			String status = null;
			//System.out.println("Polling User posicion " + user + " Using: "+ url);
			try {
				service = new URL(url);
				connection = service.openConnection();
				
				in = new BufferedReader(new InputStreamReader(
				                            connection.getInputStream()));
				
				status = in.readLine();
				in.close();
			} catch (IOException e) {
				
			   System.out.println("User Sensor-->" + this.getId() + ": Bad Device or not responding " + url);
			   return status;
			}
				
			return status;
		}	
	
	//***
	public static void disableSslVerification() {
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
