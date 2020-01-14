package edu.casetools.icase.mreasoner.actuators.device;

import edu.casetools.icase.mreasoner.actuators.data.Action;
import edu.casetools.icase.mreasoner.actuators.data.ActuatorConfigs;
import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;

public interface Actuator {
	public void performAction(Action action);
	public ActuatorConfigs getConfigs();
	public void setDatabaseOperations(DatabaseOperations db);
	public DatabaseOperations getDatabaseOperation();
	public Action readAction();
	public boolean isDefinedState();
}
