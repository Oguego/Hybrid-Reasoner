package edu.casetools.icase.mreasoner.deployment.sensors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.sshtools.util.Arrays;
import edu.casetools.icase.mreasoner.mw.sensors.SensorManager;
import edu.casetools.icase.mreasoner.configs.data.MConfigs;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperationsFactory;


//import edu.casetools.icase.mreasoner.*;



public class MDataManagerPoll implements Runnable {

	DatabaseOperations databaseOperations;
	ArrayList<Sensors> sensorsList; 
	ArrayList<String> sensorsError;
	Boolean running;
	
	public MDataManagerPoll(MConfigs configs){
		//this.databaseOperations = databaseOperations;
		
		this.databaseOperations = DatabaseOperationsFactory.getDatabaseOperations(configs.getDBConfigs());
		
		//this.sensorsList = new ArrayList<Sensors>();
		this.sensorsError = new ArrayList<String>();
		this.sensorsList = SensorManager.getSensors(databaseOperations);
		this.running=true;	
	//	System.out.println("************************* MDataManagerPoll  **************************");
		
	}
	

	public void run(){
		String status;
		System.out.println("************************* MDataManagerPoll RUN : " + this.sensorsList.size() +" **************************");
		
		while (this.running){
			long start = System.currentTimeMillis();
			for (Sensors sensor:this.sensorsList ) {
				//System.out.println("************************* MDataManagerPoll WHILE : " + sensor.getId()+" **************************");
				if (!this.sensorsError.contains(sensor.getId())){
			//		System.out.println("************************* MDataManagerPoll POLL : " + sensor.getId()+" **************************");
					status = null;
					status = sensor.poll();		
				//	System.out.println("************************* MDataManagerPoll STATUS : " + status +" **************************");
					if (status!=null){
					// VALUES Thats is managing by each class
					//	status = this.statusToStringBool(status);
			
						if (!status.equals(sensor.getStatus())){
							System.out.println("*********  Change on State: "+ status +" ******* INFO: " + sensor.getId() + ": " + sensor.getState() + " change " + sensor.getStatus() + " to "  +  status);
					    	//System.out.println(sensor.getId() + ": " + sensor.getState() + " change " + sensor.getStatus() + " to "  +  status);
					       	sensor.setStatus(status);
					    	this.createEvent(sensor);
					    	
					    }
					}else{
						this.sensorsError.add(sensor.getId());
					//	System.out.println("************************* MDataManagerPoll ERROR : " + status +" **************************");
					    //this.sensorsList.remove(sensor);
					}
					
				 }
			}
			long sleeptime = System.currentTimeMillis() - start;
			
			try {
				if(sleeptime<1000){
					Thread.sleep(1000 - sleeptime);
				}else{
					System.out.println(" ********** Warning !!!  POLLER PROCESS SPAN " + sleeptime + " *****");
				}
			//	poolBLE=false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void terminate(){
		this.running = false;
		
	}
	


	protected void createEvent(Sensors sensor) {
		 	
		 if(sensor.getState() != null && sensor.getStatus() != null){
				
		     	 databaseOperations.insertEvent(sensor.getState(), "" + sensor.getStatus(),"-1", sensor.getDate(), sensor.getTime());
		     	// printVariable(variable, state);
		  } else {
		     		 // printVariableWarning(variable);
		  }
		 	//      System.out.println("***************************************************************************");	
	}

	
	
}
