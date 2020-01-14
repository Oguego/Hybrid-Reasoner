package edu.casetools.icase.argumentation;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import edu.casetools.icase.mreasoner.core.elements.rules.SameTimeRule;
import edu.casetools.icase.mreasoner.core.elements.states.State;


public class SameTimeRuleManager{

	protected State consequence;
	private Connection conn = null;
	private Statement stmt = null;
	public Vector<SameTimeRule> rules;
	public Vector<State> systemStatus;
	private MySQL_ArgumentationDBOperations db;
	private ConflictAnalyser conflictAnalyser;
	

	public SameTimeRuleManager() {
		rules = new Vector<SameTimeRule>();
		db = new MySQL_ArgumentationDBOperations();
		db.connect();
		conflictAnalyser = new ConflictAnalyser();
	}			

	//	public void compareRules(){
	//		int numberConflicts = 0;
	//		List<String> displayList = new ArrayList<>();
	//		ConflictAnalyser conflictAnalyser;
	//		for(int i=0;i<rules.size();i++){
	//			for(int j=i+1;j<rules.size();j++){
	//				conflictAnalyser = new ConflictAnalyser(rules.get(i),rules.get(j));
	//				if(rules.get(i).isConflict(rules.get(j))){ //not recent comment
	//				if(conflictAnalyser.isConflict()){
	//					//Save conflicts
	//					
	//					long millis=System.currentTimeMillis();  
	//					java.sql.Date todayDate=new java.sql.Date(millis);  				
	//					String temporalDate = todayDate.toString();	
	//
	//					String consequence1, consequence2;
	//					consequence1 = rules.get(i).getConsequence().getName();
	//					if(!rules.get(i).getConsequence().getStatus()){
	//						consequence1 = "!" + consequence1;
	//					}
	//
	//					consequence2 = rules.get(j).getConsequence().getName();
	//					if(!rules.get(j).getConsequence().getStatus()){
	//						consequence2 = "!" + consequence2;
	//					}
	//					
	//					db.insertConflict(consequence1, consequence2);
	//
	//					numberConflicts ++;
	//				}
	//
	//				displayList.add(conflictAnalyser.getDisplay());
	//			}
	//		}
	//		System.out.println("There are " + numberConflicts + " conflicts.");
	//		if(numberConflicts == 1){
	//			infoBox("There is only " + numberConflicts + " conflict.", "Warning: Conflict Detected" );
	//		}else{
	//			infoBox("There are " + numberConflicts + " conflicts.", "Warning: Conflict Detected" );
	//		}
	//		String allConflict = ""; 
	//		int conflictNo = 0;
	//		for(int i=0;i<displayList.size();i++){
	//			if(displayList.get(i)!=null){
	//				allConflict = allConflict + "Conflict" + " " + ++conflictNo + ":" + " " + displayList.get(i) + "\n";
	//				//infoBox(displayList.get(i),"Conflict Resolved");
	//			}			// Chimezie 
	//		}
	//		infoBox(allConflict, "Conflict Resolved");
	//	}

	public void compareRulesForArgumentation(){
		Vector<SameTimeRule> consequences = new Vector<>();
		Vector<Integer> resolvedReasons = new Vector<>();
		for(int i=0;i<rules.size();i++){
			ArrayList<SameTimeRule> group1 = new ArrayList<>();
			ArrayList<SameTimeRule> group2 = new ArrayList<>();
			group1.add(rules.get(i));
			
			for(int j=i+1;j<rules.size();j++){
				boolean repeated = false;
				for(int k=0;k<i;k++){
					if(rules.get(i).getConsequence().equals(rules.get(k).getConsequence()) ||
							rules.get(i).getConsequence().opposite(rules.get(k).getConsequence())){
						repeated = true;
					}
				}
				
				if(!repeated){
					if(rules.get(i).getConsequence().equals(rules.get(j).getConsequence())){
						group1.add(rules.get(j));
					}else{
						if(rules.get(i).getConsequence().opposite(rules.get(j).getConsequence())){
							group2.add(rules.get(j));
						}
					}					
				}
			}
			
			if(group2.size()>0){
				//System.out.println("There is conflict"); //CommentedOnWednesday
				consequences.add(compareGroups(group1,group2,resolvedReasons));
			}
		}
		
		db.dropResolvedConflictsTable();
		db.createResolvedConflictsTable(systemStatus);
		db.erasePossibleConflicts();
		db.insertPossibleConflicts(consequences);
	}
	
	public void compareRules(){
//		int numberConflicts = 0;
//		List<String> displayList = new ArrayList<>();
//		ConflictAnalyser conflictAnalyser;
//		List<ConflictAnalyser> conflicts = new ArrayList<>();
		Vector<SameTimeRule> consequences = new Vector<>();
		Vector<Integer> resolvedReasons = new Vector<>();
		for(int i=0;i<rules.size();i++){
			ArrayList<SameTimeRule> group1 = new ArrayList<>();
			ArrayList<SameTimeRule> group2 = new ArrayList<>();
			group1.add(rules.get(i));
			
			for(int j=i+1;j<rules.size();j++){
				boolean repeated = false;
				for(int k=0;k<i;k++){
					if(rules.get(i).getConsequence().equals(rules.get(k).getConsequence()) ||
							rules.get(i).getConsequence().opposite(rules.get(k).getConsequence())){
						repeated = true;
					}
				}
				
				if(!repeated){
					if(rules.get(i).getConsequence().equals(rules.get(j).getConsequence())){
						group1.add(rules.get(j));
					}else{
						if(rules.get(i).getConsequence().opposite(rules.get(j).getConsequence())){
							group2.add(rules.get(j));
						}
					}					
				}
				
				
//				conflictAnalyser = new ConflictAnalyser(rules.get(i), rules.get(j));
				
//				if(conflictAnalyser.isConflict() && isNotRepeated(conflicts, conflictAnalyser)){
//					conflictAnalyser.solveConflict2();
//					conflicts.add(conflictAnalyser);
//					//Save conflicts
//
//					long millis=System.currentTimeMillis();  
//					java.sql.Date todayDate=new java.sql.Date(millis);  				
//					String temporalDate = todayDate.toString();	
//
//					String consequence1, consequence2;
//					consequence1 = rules.get(i).getConsequence().getName();
//					if(!rules.get(i).getConsequence().getStatus()){
//						consequence1 = "!" + consequence1;
//					}
//
//					consequence2 = rules.get(j).getConsequence().getName();
//					if(!rules.get(j).getConsequence().getStatus()){
//						consequence2 = "!" + consequence2;
//					}				
//
//					db.insertConflict(consequence1, consequence2);
//
//					numberConflicts ++;
//				}
//
//				displayList.add(conflictAnalyser.getDisplay());
			}
			
			if(group2.size()>0){
				//System.out.println("There is conflict"); //CommentedOnWednesday
				consequences.add(compareGroups(group1,group2,resolvedReasons));
			}
		}
		
		db.dropResolvedConflictsTable();
		db.createResolvedConflictsTable(systemStatus);
		db.erasePossibleConflicts();
		db.insertPossibleConflicts(consequences);
		
//		System.out.println("There are " + conflicts.size() + " conflicts.");
//		if(conflicts.size() == 1){
//			infoBox2("There is only " + conflicts.size() + " conflict.", "Warning: Conflict Detected" );
//		}else{
//			infoBox2("There are " + conflicts.size() + " conflicts.", "Warning: Conflict Detected" );
//		}
//		
//		Vector<SameTimeRule> consequences = new Vector<>();
//		for(int i=0;i<conflicts.size();i++){
//			consequences.add(conflicts.get(i).getWinner());
//		}		
//		String allConflict = "";
//		
//		for(int i=0;i<displayList.size();i++){
//			if(displayList.get(i)!=null){
//				allConflict = allConflict + "Conflict" + " " + (i+1) + ":" + " " + displayList.get(i) + "\n";
//				//infoBox(displayList.get(i),"Conflict Resolved");
//			}
//		}
//		infoBox(allConflict, "Conflict Resolved");
	}
	
	public void checkStatusForConflics(int iteration){
		Vector<SameTimeRule> consequences = new Vector<>();
		Vector<SameTimeRule> possibleRules = new Vector<>();
		Vector<Integer> resolvedReasons = new Vector<>();
		
		//System.out.println("System Status "); //CommentedOnWednesday
		for(int i=0;i<systemStatus.size();i++){
			//systemStatus.get(i).print();//CommentedOnWednesday
			//System.out.println("");//CommentedOnWednesday
		}
		System.out.println("-----------");
		
		for(int i=0;i<rules.size();i++){
			if(compareStates(rules.get(i).getAntecedents(),systemStatus)){
				possibleRules.add(rules.get(i));//CommentedOnWednesday
			}
		}
		
		//System.out.println("Possible Rules so far:");//CommentedOnWednesday
		for(int i=0;i<possibleRules.size();i++){
			//possibleRules.get(i).printRule();//CommentedOnWednesday
		}
		
		for(int i=0;i<possibleRules.size();i++){
			ArrayList<SameTimeRule> group1 = new ArrayList<>();
			ArrayList<SameTimeRule> group2 = new ArrayList<>();
			group1.add(possibleRules.get(i));
			
			for(int j=i+1;j<possibleRules.size();j++){
				boolean repeated = false;
				for(int k=0;k<i;k++){
					if(possibleRules.get(i).getConsequence().equals(possibleRules.get(k).getConsequence()) ||
							possibleRules.get(i).getConsequence().opposite(possibleRules.get(k).getConsequence())){
						repeated = true;
					}
				}
				
				if(!repeated){
					if(possibleRules.get(i).getConsequence().equals(possibleRules.get(j).getConsequence())){
						group1.add(possibleRules.get(j));
					}else{
						if(possibleRules.get(i).getConsequence().opposite(possibleRules.get(j).getConsequence())){
							group2.add(possibleRules.get(j));
						}
					}					
				}
			}
			
			if(group2.size()>0){
				//System.out.println("There is conflict"); //CommentedOnWednesday
				consequences.add(compareGroups(group1,group2,resolvedReasons));
				
			}
		}
		
		if(consequences.size()!=0){
			for(int j=0;j<consequences.size();j++){
				for(int i=0;i<systemStatus.size();i++){
					if(consequences.get(j).getConsequence().getName().equals(systemStatus.get(i).getName())){
						systemStatus.get(i).setStatus(consequences.get(j).getConsequence().getStatus());
					}
				}
			}
		}
				
		db.createInsertResolvedConflictsQuery1(iteration,consequences,resolvedReasons);
//		db.createUpdateResultsTablesWithConflictQuery(iteration,consequences);
	}

	private boolean compareStates(Vector<State> antecedents, Vector<State> systemStatus2) { 
		int count = 0;
//		int prefCount = 0;
		for(int i=0;i<antecedents.size();i++){
			for(int j=0;j<systemStatus2.size();j++){
//				System.out.println("");
//				antecedents.get(i).print();
//				System.out.print(" - ");
//				systemStatus2.get(j).print();
//				if(antecedents.get(i).getName().contains("pref")){
//					prefCount ++;
//				}
				if(antecedents.get(i).equals(systemStatus2.get(j))){
//					System.out.print(" ---- Equals \n");
					count ++;
					break;
				}
			}		
		}
//		System.out.println(count);
		if(count == antecedents.size()){
			return true;
		}
		return false;
	}

	private SameTimeRule compareGroups(ArrayList<SameTimeRule> group1, ArrayList<SameTimeRule> group2, Vector<Integer> resolvedReasons) {
//		ConflictAnalyser conflictAnalyser;
		for(int i=0;i<group1.size();i++){
			for(int j=0;j<group2.size();j++){
//				conflictAnalyser = new ConflictAnalyser(group1.get(i), group2.get(j));
				conflictAnalyser.setGroups(group1.get(i), group2.get(j));
				if(conflictAnalyser.isConflict()){
					if(conflictAnalyser.solveConflict1()!=3){
						resolvedReasons.add(conflictAnalyser.solveConflict1());
						//System.out.println("Conflict Analyser: " + conflictAnalyser.getWinner().getConsequence().getWholeName());//CommentedOnWednesday
						//System.out.println("Group1: " + group1.get(i).getConsequence().getWholeName()); //CommentedOnWednesday
						//System.out.println("Group2: " + group2.get(j).getConsequence().getWholeName()); //CommentedOnWednesday
						
						if(!conflictAnalyser.getWinner().getConsequence().equals(group1.get(i).getConsequence())){
							group1.remove(i);
							//System.out.println("Remove G1 - " + i); //CommentedOnWednesday
							i--;
							break;
						}else{
							group2.remove(j);
							//System.out.println("Remove G2 - " + j); //CommentedOnWednesday
							j--;
						}
					}
				}
			}
		}
		if(group1.size()==0){
			//System.out.println("Group 2 wins"); //CommentedOnWednesday
			return group2.get(0);
		}else{
			if(group2.size()==0){
				//System.out.println("Group 1 wins"); //CommentedOnWednesday
				return group1.get(0);
			}else{
//				System.out.println("COIN TOSS: " + group1.size() + " - " + group2.size());
//				COIN TOSS
//				if(coinToss()<=1){
//					System.out.println("Group 1 wins");
//					return group1.get(0);
//				}else{
//					System.out.println("Group 2 wins");
//					return group2.get(0);
//				}

				//new
				SameTimeRule winner = new SameTimeRule();
				for(int i=0;i<systemStatus.size();i++){
					if(systemStatus.get(i).equals(group1.get(0).getConsequence())){
						winner = group1.get(0);
					}else{
						if(systemStatus.get(i).equals(group2.get(0).getConsequence())){
							winner = group2.get(0);
						}
					}										
				}
				resolvedReasons.add(3);
				return winner;
				//new
			}
		}
		
	}
	
//	private double coinToss() {
//		double coin = Math.random()*2;
//		System.out.println("Coin value: " + coin);
//		return coin;
//	}

	private boolean isNotRepeated(List<ConflictAnalyser> conflicts, ConflictAnalyser conflictAnalyser) {
		// TODO Auto-generated method stub
		for(int i=0;i<conflicts.size();i++){
			if(conflicts.get(i).getRule1().getConsequence().equals(conflictAnalyser.getRule1().getConsequence()) || conflicts.get(i).getRule1().getConsequence().equals(conflictAnalyser.getRule2().getConsequence())){
				return false;
			}
		}
		return true;
	}

	public void infoBox(String infoMessage, String titleBar){
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void infoBox2(String infoMessage, String titleBar){
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.WARNING_MESSAGE);
	}
}


////	
////	public void compareRules(){
////		int numberConflicts = 0;
////		List<String> displayList = new ArrayList<>();
////		ConflictAnalyser conflictAnalyser;
////		for(int i=0;i<rules.size();i++){
////			for(int j=i+1;j<rules.size();j++){
////				conflictAnalyser = new ConflictAnalyser(rules.get(i),rules.get(j));
//////				if(rules.get(i).isConflict(rules.get(j))){ //not recent comment
////				if(conflictAnalyser.isConflict()){
////					//Save conflicts
////					this.connect();
////										
//////					long millis=System.currentTimeMillis();  
//////					java.sql.Date todayDate=new java.sql.Date(millis);  				
//////					String temporalDate = todayDate.toString();	
////					
////					String consequence1, consequence2, rule1Since, rule1Until, rule2Since, rule2Until;
////					consequence1 = rules.get(i).getConsequence().getName();
////					if(!rules.get(i).getConsequence().getStatus()){
////						consequence1 = "!" + consequence1;
////					}
//////					rule1Since = temporalDate + " " +rules.get(i).getTemporalOperators().get(0).getSinceValue().getTime_of_day().getTimeString();
//////					rule1Until = temporalDate + " " +rules.get(i).getTemporalOperators().get(0).getUntilValue().getTime_of_day().getTimeString();					
////					consequence2 = rules.get(j).getConsequence().getName();
////					if(!rules.get(j).getConsequence().getStatus()){
////						consequence2 = "!" + consequence2;
////					}
//////					rule2Since = temporalDate + " " +rules.get(j).getTemporalOperators().get(0).getSinceValue().getTime_of_day().getTimeString();
//////					rule2Until = temporalDate + " " +rules.get(j).getTemporalOperators().get(0).getUntilValue().getTime_of_day().getTimeString();
////					
////					
////					try{	
//////						String query = "INSERT INTO time_conflict (idConflict, rule1Consequence, rule1Begin, rule1End, rule2Consequence, rule2Begin, rule2End,  ConflictTimeBegin, ConflictTimeEnd, systemTime, occurrenceTime)"
//////								+" VALUES(null, '" + consequence1 + "', '" + rule1Since +  "', '" + rule1Until + "', '" + consequence2 +  "', '" + rule2Since + "', '" + rule2Until + "', '" + temporalDate + " " + conflictAnalyser.getConflictTimeBegin() + "', '" + temporalDate + " " + conflictAnalyser.getConflictTimeEnd() + "', current_timestamp, current_timestamp)";
////
////						String query = "INSERT INTO conflict (idconflict, rule1Consequence, rule2Consequence, occurrenceTime)"
////								+" VALUES(null, '" + consequence1 + "', '" + consequence2 +  "', current_timestamp)";
////
////					
////						System.out.println(query);
////						stmt.executeUpdate(query);
////					}
////					catch (SQLException e1) {
////						// TODO Auto-generated catch block
////						e1.printStackTrace();
////					}
////					numberConflicts ++;
////				}
////				
////				displayList.add(conflictAnalyser.getDisplay());
////			}
////		}
////		System.out.println("There are " + numberConflicts + " conflicts.");
////		if(numberConflicts == 1){
////			infoBox("There is only " + numberConflicts + " conflict.", "Warning: Conflict Detected" );
////		}else{
////			infoBox("There are " + numberConflicts + " conflicts.", "Warning: Conflict Detected" );
////		}
////		
////		for(int i=0;i<displayList.size();i++){
////			if(displayList.get(i)!=null){
////				infoBox(displayList.get(i),"Conflict Resolved");
////			}			
////		}
////	}
////	
////	public void infoBox(String infoMessage, String titleBar){
////        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
////    }
//
//}