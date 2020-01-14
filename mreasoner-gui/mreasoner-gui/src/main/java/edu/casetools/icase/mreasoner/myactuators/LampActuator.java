package edu.casetools.icase.mreasoner.myactuators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.casetools.icase.mreasoner.actuators.data.Action;
import edu.casetools.icase.mreasoner.actuators.data.ActuatorConfigs;
import edu.casetools.icase.mreasoner.actuators.device.Actuator;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;



public class LampActuator implements Actuator {
	
	URL service;
	URLConnection connection;
	//PressurePadConfigs configs1;
	LampConfigs configs;
	Action lastAction;
	DatabaseOperations dbo;
	
	public LampActuator(LampConfigs configs){
		this.configs = configs;
		lastAction = new Action();
	}
	

	public void performAction(Action action) {
		// TODO Auto-generated method stub
		//System.out.println("Performing Action");
		try {
		if(!lastAction.equals(action)){
			
			String url = "http://"+configs.getIp()+":"+configs.getPort()+
					"/data_request?id=lu_action&output_format=xml&DeviceNum="+action.getDevice()+
					"&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue="+
					action.getValue();
			
			service = new URL(url);
			connection = service.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                                    connection.getInputStream()));
	        
	  // String inputLine;
	        System.out.println("ACTUATOR MANAGER: SWITCHING " + configs.getState() + " " +action.getDevice()+" TO VALUE "+action.getValue());
//	        while ((inputLine = in.readLine()) != null) 
//	            System.out.println(inputLine);
	        
	       // System.out.println(url);
	        in.close();
	        
		}
	} catch (MalformedURLException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
		lastAction = action;

	}


	public ActuatorConfigs getConfigs() {
		// TODO Auto-generated method stub
		return this.configs;
	}


	public void setDatabaseOperations(DatabaseOperations db) {
		this.dbo = db;
		
	}


	public DatabaseOperations getDatabaseOperation() {
		// TODO Auto-generated method stub
		return this.dbo;
	}


	public Action readAction() {
		Action action  = new Action();
		String state   = this.getConfigs().getState();

		String device  = dbo.getDevice(state);
		boolean status = dbo.getStatus(state);
		
		if(device != null){
			action.setDevice(device);
			action.setValue(status);
		}
		
		return action;
	}
	
	public boolean isDefinedState() {
		// TODO Auto-generated method stub
		return this.dbo.hasColumn("results", this.getConfigs().getState());
		
	}
	

//	public PressurePadConfigs getConfigs1() {
//			return configs1;
//	}

//service = new URL("http://10.12.102.56:3480/data_request?id=lu_action&output_format=xml&DeviceNum=39&serviceId=urn:upnp-org:serviceId:SwitchPower1&action=SetTarget&newTargetValue=1");

	
}
