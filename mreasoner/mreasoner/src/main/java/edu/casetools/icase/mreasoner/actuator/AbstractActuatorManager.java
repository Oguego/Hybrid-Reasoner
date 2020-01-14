package edu.casetools.icase.mreasoner.actuator;

import java.util.Vector;

import edu.casetools.icase.mreasoner.actuators.data.Action;
import edu.casetools.icase.mreasoner.actuators.data.ActuatorConfigs;
import edu.casetools.icase.mreasoner.actuators.device.Actuator;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;



public abstract class AbstractActuatorManager extends Thread{

	private boolean running;
	protected Vector<Actuator> actuators;
	
	protected DatabaseOperations databaseoperation;
	
	public AbstractActuatorManager(Vector<Actuator> actuators){	
		this.actuators = actuators;
		running = true;
		
	}

	@Override
	public void run(){
		
		while (running)
		{
			for(int i=0;i<actuators.size();i++){
				Action action = readAction(actuators.get(i).getConfigs());				
				actuators.get(i).performAction(action);
			}
		}
	}
	
	private Action readAction(ActuatorConfigs actuatorConfigs){
		Action action  = new Action();
		String state   = actuatorConfigs.getState();

		String device  = getDevice(state);
		boolean status = getStatus(state);
		
		if(device != null){
			action.setDevice(device);
			action.setValue(status);
		}
		
		return action;
	}

	public void terminate(){
		running = false;
	}
	
	/** If the device needs store in database */
	public void setDataBaseOperations(DatabaseOperations db){
		this.databaseoperation=db;
	}
	
	protected abstract String getDevice(String state);
	protected abstract boolean getStatus(String state); 
	
}
