package edu.casetools.icase.mreasoner.actuators.data;

public abstract class ActuatorConfigs {
	 String state;
	 
	 public ActuatorConfigs(String state){
		 this.state = state;
	 }

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	 
	
}
