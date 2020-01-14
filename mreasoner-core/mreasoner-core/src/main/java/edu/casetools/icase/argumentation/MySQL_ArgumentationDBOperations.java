package edu.casetools.icase.argumentation;

import java.util.Vector;

import edu.casetools.icase.argumentation.ArgumentationDBOperations;
import edu.casetools.icase.mreasoner.core.elements.rules.SameTimeRule;
import edu.casetools.icase.mreasoner.core.elements.states.State;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.Statement;
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;




public class MySQL_ArgumentationDBOperations extends ArgumentationDBOperations{

	String resultsQuery;
//	private Statement stmt;
	public static int numberOfConflicts = 1;
	
	@Override
	public void connect(){
		
		try {
			dbConnection.connect();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reconnect() {
		// TODO Auto-generated method stub
		dbConnection = new ArgumentationDBConnection();
		connect();
	}



	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		try {
			dbConnection.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertConflict(String consequence1, String consequence2) {
		// TODO Auto-generated method stub
		try {
			String query = "INSERT INTO conflict (idconflict, rule1Consequence, rule2Consequence, occurrenceTime)"
					   +" VALUES(null, '" + consequence1 + "', '" + consequence2 +  "', current_timestamp)";

			dbConnection.executeUpdate(query);
    	
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

//	public void createPossibleConflicts(){
//
//		String query = "CREATE TABLE IF NOT EXISTS PossibleConflicts ("+
//				"idPossibleConflict int PRIMARY KEY, "+
//				"detected_time Datetime, "+
//				"conflictName VARCHAR(225)";
//		try {
//			dbConnection.executeUpdate(query);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}	
//	}
	
	public void createResolvedConflictsTable1(Vector<SameTimeRule> winner){

		String query = "CREATE TABLE IF NOT EXISTS ResolvedConflicts ("+
				"idResolvedConflict bigint PRIMARY KEY, "+
				"iteration bigint NOT NULL, "+
				"resolved_time DateTime";

		for(int i=0;i<winner.size();i++){
			query = query + ", " + winner.get(i).getConsequence().getName() + " boolean";
		}
		query = query + ", solved_reason VARCHAR(225))";
		
		//System.out.println(query); //CommentedOnWednesday
		
		try {
			dbConnection.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	
	@Override
	public void createResolvedConflictsTable2(Vector<SameTimeRule> winner){

		String query = "CREATE TABLE IF NOT EXISTS ResolvedConflicts ("+
				"iteration bigint PRIMARY KEY, "+
				"resolved_time DateTime";

		for(int i=0;i<winner.size();i++){
			query = query + ", " + winner.get(i).getConsequence().getName() + " boolean";
		}
		query = query + ")";
		
		//System.out.println(query); //CommentedOnWednesday
		
		try {
			dbConnection.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		
	}
	
	public void createResolvedConflictsTable(Vector<State> systemStatus){
		Vector<String> states = new Vector<String>();
		for(int i = 0;i<systemStatus.size();i++){
			states.add(systemStatus.get(i).getName());
		}
		
		String query ="";

		query = "CREATE TABLE IF NOT EXISTS ResolvedConflicts ("+
				"idResolvedConflict bigint PRIMARY KEY, "
				+"iteration bigint,"+
				"resolved_time DateTime";	
					
		for(int i=0;i<states.size();i++){
			query = query + ","+states.get(i)+" boolean";
		}
		query = query + ", solved_reason VARCHAR(225))";
		
		try {
			dbConnection.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createInsertResolvedConflictsQuery(int iteration, Vector<SameTimeRule> winner){
		
		try{
			resultsQuery = "INSERT INTO ResolvedConflicts (iteration,resolved_time";
			for(int i=0;i<winner.size();i++){
				resultsQuery = resultsQuery + ","+ winner.get(i).getConsequence().getName();
			}
			resultsQuery = resultsQuery + ") VALUES (" + iteration + ", current_timestamp";
			for(int i=0;i<winner.size();i++){
				if(winner.get(i).getConsequence().getStatus()){
					resultsQuery = resultsQuery + ",1";
				}else{
					resultsQuery = resultsQuery + ",0";
				}
			}
			resultsQuery = resultsQuery + ")";
			//System.out.println(resultsQuery);//CommentedOnWednesday
			dbConnection.executeUpdate(resultsQuery);
		
		}catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	
	@Override
	public void createInsertResolvedConflictsQuery1(int iteration, Vector<SameTimeRule> winner, Vector<Integer> resolvedReasons){
		try{
			for(int i=0;i<winner.size();i++){
				resultsQuery = "INSERT INTO ResolvedConflicts (idResolvedConflict,iteration,resolved_time";
				resultsQuery = resultsQuery + ","+ winner.get(i).getConsequence().getName();
				resultsQuery = resultsQuery + ", solved_reason) VALUES (" + numberOfConflicts + ", " + (iteration-1) + ", current_timestamp";
				if(winner.get(i).getConsequence().getStatus()){
					resultsQuery = resultsQuery + ",1";
				}else{
					resultsQuery = resultsQuery + ",0";
				}
				resultsQuery = resultsQuery + " ,'" + PossibleResolutionMethod.getStringFromNumber(resolvedReasons.get(i)) + "')";
				//System.out.println(resultsQuery); //CommentedOnWednesday
				dbConnection.executeUpdate(resultsQuery);
				numberOfConflicts ++;
			}
			
		}catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void createUpdateResultsTablesWithConflictQuery(int iteration, Vector<SameTimeRule> winner){
		try{
			for(int i=0;i<winner.size();i++){
				resultsQuery = "UPDATE results SET " + winner.get(i).getConsequence().getName() + " = ";
				if(winner.get(i).getConsequence().getStatus()){
					resultsQuery = resultsQuery + "1";
				}else{
					resultsQuery = resultsQuery + "0";
				}
				resultsQuery = resultsQuery + " WHERE iteration = " + (iteration-1);
				//System.out.println(resultsQuery); //CommentedOnWednesday
				dbConnection.executeUpdate(resultsQuery);
				numberOfConflicts ++;
			}
			
		}catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
		@Override
		public void eraseResolvedConflictsTable() {
		
			try {
				String query = "DELETE FROM ResolvedConflicts";
				dbConnection.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		@Override
		public void dropResolvedConflictsTable() {
		
			try {
				String query = "DROP TABLE IF EXISTS ResolvedConflicts";
				dbConnection.executeUpdate(query);
	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		

		@Override
		public void erasePossibleConflicts() {
			try {
				String query = "DELETE FROM PossibleConflicts";
				dbConnection.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		
//		@Override
//		public void dropPossibleConflictsTable(){
//			try{
//				String query = "DROP TABLE IF EXISTS PossibleConflicts";
//				dbConnection.executeUpdate(query);
//			}catch (SQLException e){
//				e.printStackTrace();
//			}
//		}

		@Override
		public void insertPossibleConflicts(Vector<SameTimeRule> winner) {
			// TODO Auto-generated method stub
			try {
				for(int i=0;i<winner.size();i++){
					String query = "INSERT INTO PossibleConflicts (idPossibleConflict, detected_time, conflictName)"
							   +" VALUES (" + (i+1) + ", current_timestamp, '" + winner.get(i).getConsequence().getName() + "')";
					//System.out.println(query); //CommentedOnWednesday
					dbConnection.executeUpdate(query);
				}
	    	} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}

		@Override
		public ResultSet getPossibleConflicts() {
			ResultSet result = null;
			String query;

		    try {
				query = "select * from sensors.possibleconflicts";
				result =  dbConnection.executeQueryOpenStatement(query);		
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return result;
		}

		@Override
		public ResultSet getResolvedConflicts() {
				ResultSet Rresult = null;
				String query;

			    try {
					query = "select * from sensors.resolvedconflicts";
					Rresult =  dbConnection.executeQueryOpenStatement(query);		
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return Rresult;			
		}
		
		@Override
		public ResultSet getResultsTableContent() {
			String 	  query     = null;
			ResultSet resultSet = null;
			try {
				query = "select * from results";
				
				resultSet = dbConnection.executeQueryOpenStatement(query);
//			} catch (PSQLException e) {
			//	dbConnection.setConnection(-1);
			} catch (SQLException e) {
			//	dbConnection.setConnection(-2);
			}
			return resultSet;
		}
		
		@Override
		public ResultSet getResolvedConflicts(int iteration) {
			ResultSet Rresult = null;
			String query;

		    try {
				query = "select * from sensors.resolvedconflicts"
						+ " where iteration = " + iteration;
				Rresult =  dbConnection.executeQueryOpenStatement(query);		
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return Rresult;			
		}

		
//	NEW 
		
		@Override
		public void erasePossibleConflictsTable() {
		
			try {
				String query = "DELETE FROM sensors.possibleconflicts";
				dbConnection.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ResultSet getResultsTableContentPG() {
			ResultSet resultSet = null;
			String query;
			try {
				Class.forName("org.postgresql.Driver");
				Connection connPG = DriverManager.getConnection( "jdbc:postgresql://localhost:5432/sensors", "postgres", "123456");		    		    
			
				query = "select * from \"results\"";
				Statement stmt = connPG.createStatement();
				resultSet = stmt.executeQuery(query);
//				stmt.close();
				return resultSet;
				
//			} catch (PSQLException e) {
			//	dbConnection.setConnection(-1);
			} catch (SQLException | ClassNotFoundException e) {
			//	dbConnection.setConnection(-2);
				//System.out.println("Hola!!!"); //CommentedOnWednesday
				e.printStackTrace();
			}
			return resultSet;
		}

		//public void executeUpdate(String query) {
			// TODO Auto-generated method stub
			
		//}

		
		
		

}