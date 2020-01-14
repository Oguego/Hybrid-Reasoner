package edu.casetools.icase.argumentation;

public class Preference {
	private String preference;
	private int priority;
	public Preference(String preference, int priority) {
		this.preference = preference;
		this.priority = priority;
	}
	public String getPreference() {
		return preference;
	}
	
	public void setPreference(String preference) {
		this.preference = preference;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}

}
