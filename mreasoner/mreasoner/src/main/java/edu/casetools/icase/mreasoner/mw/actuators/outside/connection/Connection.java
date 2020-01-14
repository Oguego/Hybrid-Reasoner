package edu.casetools.icase.mreasoner.mw.actuators.outside.connection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import edu.casetools.icase.mreasoner.actuators.data.Action;
//import edu.casetools.icase.mreasoner.actuators.device.Actuator;
import edu.casetools.icase.mreasoner.mw.actuators.outside.OutsideActuator;

public class Connection {

	static String IPSERVER  = "10.12.102.61";
	
	public static void sendUrlServer(Action action, OutsideActuator actuator){
		
		
		
			
			//System.out.print("SENDED TO SERVER ---->" + url + " :: " + json.toString());
		}
	
	public static SSLContext setUpHttpsConnection()
    {
        try
        {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            File certFile = new File("C:/Users/Josegines1/Documents/Mreasoner/MReasonerLatest/mreasoner-gui/mreasoner-gui/ssl/pilotIP.cer");
            //InputStream caInput = new BufferedInputStream(certFile);
            BufferedInputStream caInput = new BufferedInputStream(new FileInputStream(certFile));
            java.security.cert.Certificate ca = cf.generateCertificate(caInput);
        //    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
 
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
//     
            return context;

       
        }
        catch (Exception ex)
        {
            System.out.println("SLL AAA2,"+ "Failed to establish SSL connection to server: " + ex.toString());
            return null;
        }
    }

	 public static final HostnameVerifier DUMMY_VERIFIER = new HostnameVerifier() {
	        public boolean verify(String hostname, SSLSession session) {
	    //    	System.out.println("RestUtilImpl +Approving certificate for " + hostname);
	            return true;
	        }
	    };

	 public static final void login(){
	    	String user = "jonh@mdx.uk";
	    	String pass = "123456";
	    	String url="https://" + IPSERVER + "/success/login.php?username=" + user + "&" + "password=" + pass;
			HttpsURLConnection con=null;
			//JSONObject json = new JSONObject();
			URL object;
			try {
				cookies.initCokies();
				object = new URL(url);
				SSLContext ssl = setUpHttpsConnection();
				con = (HttpsURLConnection) object.openConnection();
			      
		        con.setHostnameVerifier(DUMMY_VERIFIER);
		        con.setSSLSocketFactory(ssl.getSocketFactory());
		        
		        
            	con.setDoOutput(true);
    			con.setDoInput(true);
		        con.setReadTimeout(10000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("GET");
             			
    			con.setDoInput(true);
                con.setDoOutput(true);
                cookies.getCokiesFrom();
    			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
    			
    			wr.write(" ");
    			wr.flush();
    			wr.close();
    			BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
    			
    			cookies.getCokiesFrom();
    		//	System.out.println("\n ///////////*******************/////////////// \n Login Response: " + in.readLine() + " \n /////////*****************************////////// \n");
    			in.close();
		        
		        
			}catch(Exception e){
				System.out.println("EXCCEPTION lOGIN" + e.toString());
                             
            }finally{
            	if (con!=null){
            		con.disconnect();	
            	}
            	
            }
	    }
}
