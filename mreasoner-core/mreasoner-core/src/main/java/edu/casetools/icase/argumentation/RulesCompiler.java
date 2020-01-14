package edu.casetools.icase.argumentation;


import edu.casetools.icase.mreasoner.compiler.MSpecificationLoader;
import edu.casetools.icase.mreasoner.core.MSpecification;
import edu.casetools.icase.mreasoner.core.elements.rules.SameTimeRule;
import edu.casetools.icase.mreasoner.core.elements.states.State;

import java.util.Vector;

public class RulesCompiler {
	public static String mainFile;

//	public static void main(String[] args) throws Exception {
//		//mainfile = "C:\\Users\\Chimezie\\Test.txt";
//		MSpecificationLoader loader = new MSpecificationLoader();
//		MSpecification mSpecifications = loader.readSystemSpecifications_RealTime(HybridMain.mainfiles);
//
//		Vector<SameTimeRule>  sameTimeRules = mSpecifications.getSystemRules().getSameTimeRules();
//		for(int i=0;i<sameTimeRules.size();i++){
//			sameTimeRules.get(i).printRule();
//		}
//		checkForPossibleConflicts(sameTimeRules, mSpecifications.getSystemStatus().getSystemStatus());
//	}

	public static void checkConflictsFromFile (String mainFile1) throws Exception {
		//mainfile = "C:\\Users\\Chimezie\\Test.txt";
		mainFile = mainFile1;
		MSpecificationLoader loader = new MSpecificationLoader();
		MSpecification mSpecifications = loader.readSystemSpecifications_RealTime(mainFile);

		Vector<SameTimeRule>  sameTimeRules = mSpecifications.getSystemRules().getSameTimeRules();
		for(int i=0;i<sameTimeRules.size();i++){
			sameTimeRules.get(i).printRule();
		}
		checkForPossibleConflicts(sameTimeRules, mSpecifications.getSystemStatus().getSystemStatus());
	}


	public static void checkForPossibleConflicts(Vector<SameTimeRule>  sameTimeRules, Vector<State> systemStatus){

		SameTimeRuleManager ruleManager = new SameTimeRuleManager();
		ruleManager.rules = sameTimeRules;
		ruleManager.systemStatus = systemStatus;

		if(ruleManager.rules.size()>1){
			ruleManager.compareRulesForArgumentation();  	
		}
	}

}



//public class RulesCompiler {
//	public static String nextLine;
//	
//	//public void readFile(){
//	public static void main(String[] args) throws Exception {
//		
//		
//	 try {
//         BufferedReader inputReader = new BufferedReader(new FileReader("MRule.txt"));
//
//         while ((nextLine = inputReader.readLine()) != null){
//        	 if (nextLine.equals("ssr((")) {
//        	     //do something
//        		 System.out.println();
//        	   }
//        	 }
//
//     } catch (FileNotFoundException e1) {
//         // TODO Auto-generated catch block
//         e1.printStackTrace();
//     } catch (IOException ep) {
//         // TODO Auto-generated catch block
//         ep.printStackTrace();
//     }
//	}
//	  	
//}
