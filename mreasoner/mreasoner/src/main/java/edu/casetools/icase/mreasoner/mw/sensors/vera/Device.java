package edu.casetools.icase.mreasoner.mw.sensors.vera;


public class Device {

	private String id;
	private String name;
	private String type;
	private String variable;

	public  Device(String id, String name, String type){
		this.id=id;
		this.name=name;
		this.type= type;
		String variable = "";
		if(this.type.equals("MotionSensor") || this.type.equals("DoorSensor")){
			variable="Tripped";
		}
	
		if(this.type.equals("BinaryLight")){
			variable="Status";
		}
		
		if(this.type.equals("Energy")){
			variable="Watts";
		}
		this.variable = variable;
		
	}
	
	
	public String getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getType(){
		return this.type;
	}
	
	public String getVariable(){
		return this.variable;
	}
		
}
