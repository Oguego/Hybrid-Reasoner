package edu.casetools.icase.mreasoner.mw.sensors.vera;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class VeraSensor2 extends VeraSensor {
	
	public VeraSensor2(String id, String state, String status, String service,  String variable){
	 	super(id, state, status, service, variable);
	}
	
	
	public String poll(){
		URL service;
		URLConnection connection = null;
		String url ="http://10.12.102.57:3480/data_request?id=variableget&serviceId=" + this.getService() + "&Variable=" + this.getVariable() + "&DeviceNum=" + this.getId();
		BufferedReader in = null;
		String state = null;
	    
		try {
			service = new URL(url);
			connection = service.openConnection();
			
			in = new BufferedReader(new InputStreamReader(
			                            connection.getInputStream()));
			
			state = in.readLine();
			in.close();
		} catch (IOException e) {
			System.out.print("-->" + this.getId() + ": Bad Device or not responding " + url);
			return null;
		}
        return this.statusToStringBool(state);
	}
	
	public String statusToStringBool(String status){
		if (status.equals("0") || status.equals("1")){
			System.out.print("*************** Sensor STATE " + status);
			return status;
		}else{
		
			float fs = Float.parseFloat(status);
			if (fs<30){ //24 watt it is the minimal power without turn on the microwave, when electric is on is 
				
				return "0";
			}
			else {
				return "1";
			}
		}
	}
}
