package edu.casetools.icase.mreasoner.mw.actuators.outside;


import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.icase.mreasoner.mw.actuators.outside.connection.Connection;
import edu.casetools.icase.mreasoner.actuators.data.Action;
import edu.casetools.icase.mreasoner.actuators.data.ActuatorConfigs;
import edu.casetools.icase.mreasoner.actuators.device.Actuator;

//Gines: Created to send messages to external RESTFUL api. 
// THIS METHOD is using SUCCESS PROJECT in case that server house sends directly the outcomes to cloud server.

public class OutsideActuator implements Actuator{
	

	Action lastAction;
	ActuatorConfigs configs;
	DatabaseOperations dbo;
		
	public OutsideActuator(ActuatorConfigs configs){
		this.configs = configs;
		lastAction = new Action();
		
	}
	
	public void performAction(Action action) {
		DatabaseOperations db = this.getDatabaseOperation();
		if(!lastAction.equals(action)){
		//	System.out.print("PERFORM ACTION " + action.getDevice() + " time " + action.getValue());
			db.setOutsideActuatorResult(this.getConfigs().getState(), "time", action.getValue());
		//	System.out.println("STORING RESULT " + action.getDevice()+" TO VALUE "+action.getValue());
		    Connection.sendUrlServer(action, this);
		  }
		
		lastAction = action;
	}
		
	

	public ActuatorConfigs getConfigs() {
		return this.configs;
	}

	public void setDatabaseOperations(DatabaseOperations db) {
		this.dbo=db;
		
	}
	
	public DatabaseOperations getDatabaseOperation() {
		// TODO Auto-generated method stub
		return this.dbo;
	}

	public Action readAction() {
		Action action  = new Action();
		String state   = this.getConfigs().getState();
	//	System.out.println("***************************************** " + state + " ****************************************************************** ");
		boolean status = dbo.getStatus(state);
		
		action.setDevice(state);
		action.setValue(status);
				
		return action;
	}

	public boolean isDefinedState() {
		// TODO Auto-generated method stub
		return this.dbo.hasColumn("results", this.getConfigs().getState());
		
	}
	
	/*public String[] getValues(String state){
			
		String[] mierda = null;
		
		this.dbo.getActuatorImplementationTableContent();
		
		
		
		return mierda;
	}*/
	
	
	
	
}
