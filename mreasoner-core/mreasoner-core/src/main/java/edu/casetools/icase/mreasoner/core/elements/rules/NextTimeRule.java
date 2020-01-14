package edu.casetools.icase.mreasoner.core.elements.rules;

import edu.casetools.icase.mreasoner.core.elements.MStatus;
import edu.casetools.icase.mreasoner.database.MDBInterface;

public class NextTimeRule extends SameTimeRule {

	public NextTimeRule(){
		super();
	}
	
	@Override
	public MStatus assertRule(MStatus systemStatus, MDBInterface database){
		boolean result = true;

		result = checkAntecedents(systemStatus);

		if(result) 
			result = checkPastBoundedAntecedents(systemStatus,database);
		if(!result)
			firstTime = true;
		if(result && firstTime){
		 database.insertInernalEvent(this.consequence, systemStatus.getTime());
		 printRuleChange();	
		 firstTime = false;
		}

		return systemStatus;

	}
	
	/**********************************************************************************************************************************************
	 *  THESE ARE ONLY PRINTING FUNCTIONS
	 ********************************************************************************************************************************************/	
	private void printRuleChange(){
		String negation = "";
		if(!consequence.getStatus())negation = "!";
		System.out.println("\t NEXT TIME RULE: "+negation+this.consequence.getName()+" WILL BE TRIGGERED ON THE NEXT STEP");
	}
	
}
