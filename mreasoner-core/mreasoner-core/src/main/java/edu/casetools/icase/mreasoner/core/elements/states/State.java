package edu.casetools.icase.mreasoner.core.elements.states;

import edu.casetools.icase.mreasoner.core.elements.time.Time;

public  class State extends DefaultState{

	public State(){
		
	}
	
	public State(String name){
		super(name);
	}
	@Override
	public boolean assertState(Time actualTime) {
		return this.status;
	}

	@Override
	public void print() {
		String sign = "";
		if(!status) sign = "!";
		System.out.print(sign+name);
		
	}
	
	//Get the whole name as a String;
	public String getWholeName() {
		String sign = "";
		if(!status) sign = "!";
		return sign+name;		
	}
	
	//Compare with other State
	
//	public boolean equals(State other){
//		if(this.name.equals(other.getName()) && this.status==other.status){
//			return true;
//		}else{
//			return false;
//		}
//	}
	
	@Override
	public boolean equals(Object other) {
		State otherState = (State) other;
		if(this.name.equals(otherState.getName()) && this.status==otherState.status){
			return true;
		}else{
			return false;
		}
	}

	//If it is an opposite State
	public boolean opposite(State other){
		if(this.name.equals(other.getName()) && this.status!=other.status){
			return true;
		}else{
			return false;
		}
	}

}
