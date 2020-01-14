package edu.casetools.icase.mreasoner.mw.sensors.vera;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import edu.casetools.icase.mreasoner.deployment.sensors.Sensors;

public class VeraSensor extends Sensors {
	
	public VeraSensor(String id, String state, String status, String service,  String variable){
	 	super(id, state, status, service, variable);
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
		
		String service = "";
		
		switch(this.service){
			case "DoorSensor":
				service = "urn:micasaverde-com:serviceId:SecuritySensor1";
				break;
			case "BinaryLight":
				service = "urn:upnp-org:serviceId:SwitchPower1";
				break;
			case "MotionSensor":
				service = "urn:micasaverde-com:serviceId:SecuritySensor1";
				break;
			case "Energy":
				service = "urn:micasaverde-com:serviceId:EnergyMetering1";
				break;
			default:
				break;
			
		}
		
		return service;
	}
	
	public String poll(){
		URL service;
		URLConnection connection = null;
		String url ="http://10.12.102.56:3480/data_request?id=variableget&serviceId=" + this.getService() + "&Variable=" + this.getVariable() + "&DeviceNum=" + this.getId();
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
			System.out.print("-->" + this.getId() + ": 2 Bad Device or not responding " + url);
			return null;
		}
        return this.statusToStringBool(state);
	}
	
	public String statusToStringBool(String status){
		
		if (status.equals("0") || status.equals("1")){
		//	System.out.println("*************** Sensor STATE  " +this.getId() + " is " + status);
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
