package edu.casetools.icase.mreasoner.core.elements.time.conf;

public class TimeConfigs {
	public enum  EXECUTION_MODE {SIMULATION_ITERATION,SIMULATION_REAL_TIME,REAL_ENVIRONMENT};
	
	private long 			timeUnit;
	private long 			executionTime;
	private EXECUTION_MODE  executionMode;
	private boolean 	    useFixedIterationTime;

	public TimeConfigs(){
		this.executionMode = EXECUTION_MODE.SIMULATION_ITERATION;
		this.useFixedIterationTime = true;
		this.setTimeUnit(0);
		this.setExecutionTime(0);
	}
	
	public long getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(long timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	public long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
	
	public void setExecutionMode(EXECUTION_MODE executionMode) {
		this.executionMode = executionMode;
	}
	
	public EXECUTION_MODE getExecutionMode() {
		return executionMode;
	}
	
	public boolean isSimulation(){
		boolean simulation = false;
		switch(this.executionMode){
				case REAL_ENVIRONMENT:
					simulation = false;
					break;
				case SIMULATION_ITERATION:
					simulation = true;
					break;
				case SIMULATION_REAL_TIME:
					simulation = false;
					break;
				default:
			break;
		}
		return simulation;
	}
	
	public boolean isFixedIterationTime() {
		return useFixedIterationTime;
	}

	public void setUseFixedIterationTime(boolean useFixedIterationTime) {
		this.useFixedIterationTime = useFixedIterationTime;
	}
	
	public String parseConfigs(){
		String result = "";
		
		result = result+"<EXECUTION_MODE>  \n";
		result = result+ getExecutionMode()+"\n";
		result = result+"</EXECUTION_MODE> \n";
		
		result = result+"<USE_FIXED_ITERATION_TIME> \n";
		result = result+isFixedIterationTime()+"\n";
		result = result+"</USE_FIXED_ITERATION_TIME> \n";
		
		result = result+"<FIXED_ITERATION_TIME> \n";
		result = result+getTimeUnit()+"\n";
		result = result+"</FIXED_ITERATION_TIME> \n";
		
		result = result+"<EXECUTION_TIME> \n";
		result = result+getExecutionTime()+"\n";
		result = result+"</EXECUTION_TIME> \n";	
		
		return result;	
	}

}
