package edu.casetools.icase.mreasoner.mw.actuators.vera;
import edu.casetools.icase.mreasoner.actuators.data.ActuatorConfigs;

public class VeraConfigs extends ActuatorConfigs{

	private String ip;
	private String port;
	
	public VeraConfigs(String state){
		super(state);
		ip   = "10.12.102.56";
		port = "3480";
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
