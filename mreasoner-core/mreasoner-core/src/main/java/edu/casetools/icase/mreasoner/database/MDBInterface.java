package edu.casetools.icase.mreasoner.database;

import edu.casetools.icase.mreasoner.core.elements.MStatus;
import edu.casetools.icase.mreasoner.core.elements.states.State;
import edu.casetools.icase.mreasoner.core.elements.time.Time;
import edu.casetools.icase.mreasoner.core.elements.time.top.TemporalOperator;

public interface MDBInterface {
	
	public MStatus findLatestEvents(MStatus systemStatus);
	
	public boolean checkTemporalOperator(TemporalOperator TOp,Time time);
	
	public void    insertInernalEvent(State consequence,Time time);
	
	public void    writeLog(MStatus systemStatus);
	
	public void    disconnect();
	
}
