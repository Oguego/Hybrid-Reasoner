package edu.casetools.icase.mreasoner.actuators.device;

import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;

public abstract class ActuatorDB implements Actuator {

	private DatabaseOperations dbo;
	
	public void setDatabaseOperations(DatabaseOperations db){
			this.dbo=db;
	}
	
	public DatabaseOperations getDatabaseOperation(){
		return dbo;
	}
}
