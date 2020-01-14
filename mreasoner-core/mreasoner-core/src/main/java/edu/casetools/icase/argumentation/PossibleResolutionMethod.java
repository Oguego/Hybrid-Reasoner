package edu.casetools.icase.argumentation;

public class PossibleResolutionMethod {
	public final static String SPECIFICITY = "Specificity";
	public final static String PREFERENCE = "User Preferences";
	public final static String PERSISTENCY = "Persistency";
	
	public static String getStringFromNumber(int method){
		switch(method){
			case 1:
				return SPECIFICITY;
				
			case 2:
				return PREFERENCE;
				
			case 3:
				return PERSISTENCY;
				
			default:
				return "Invalid";			
		}
	}
	
	public static int getNumberFromString(String method){
		switch(method){
			case SPECIFICITY:
				return 1;
				
			case PREFERENCE:
				return 2;
				
			case PERSISTENCY:
				return 3;
				
			default:
				return -1;			
		}
	}

}
