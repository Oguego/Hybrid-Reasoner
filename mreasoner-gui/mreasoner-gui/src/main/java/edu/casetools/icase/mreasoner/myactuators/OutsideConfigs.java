package edu.casetools.icase.mreasoner.myactuators;
import edu.casetools.icase.mreasoner.actuators.data.ActuatorConfigs;


// This class only is needed to keep the state. The struct of Classes of mreasoner obligate us to change classes or add 
// this kind of classes only to get the state and integrate into the msreasoner struct. 

public class OutsideConfigs extends ActuatorConfigs{

	private String ip;
	private String port;
	
	public OutsideConfigs(String state){
		super(state);
		//IT THE SAME ALL THE NEXT (SEE LAMPCONFIG)
		ip   = "";
		port = "";
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	
	
}
