package edu.casetools.icase.mreasoner.deployment.sensors;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.casetools.dcase.m2nusmv.data.elements.Time;
import edu.casetools.icase.mreasoner.core.elements.time.absolute.Date;

public class AbtractSensor {
	
	private String id;
	private String service;
	private String variable;
	private String state;
	private String status;
	private Timestamp date;
	private SimpleDateFormat  dateFormat;
		
	public AbtractSensor(String id, String state, String status, String service,  String variable){
			
			
			this.id=id;
			this.state=state;
			this.status = status;
			this.service=service;
			this.variable=variable;			
			this.date = new Timestamp(System.currentTimeMillis());
			this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("Creating sensor " + id + ":" + state + ":" + service + ":" + variable);
			//System.out.println("Sensor create " + state);
			
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
		
	/*	switch(this.service){
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
		*/
		return service;
	}
	
	
	
}
