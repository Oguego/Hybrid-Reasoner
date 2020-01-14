package edu.casetools.icase.argumentation;

import java.sql.ResultSet;
import java.util.Vector;

import edu.casetools.icase.mreasoner.core.elements.rules.SameTimeRule;

public abstract class ArgumentationDBOperations {
	
	protected ArgumentationDBConnection dbConnection;
	
	
	
	public ArgumentationDBOperations() {
		this.dbConnection = new ArgumentationDBConnection();
	}
	
	public abstract void connect();
	public abstract void reconnect();
	public abstract void disconnect();

	public abstract void insertConflict(String consequence1, String consequence2);
	public abstract void eraseResolvedConflictsTable();
	public abstract void dropResolvedConflictsTable();
	public abstract void createResolvedConflictsTable2(Vector<SameTimeRule> winner);
	public abstract void erasePossibleConflicts();
	public abstract void insertPossibleConflicts(Vector<SameTimeRule> winner);
	public abstract void createInsertResolvedConflictsQuery1(int iteration, Vector<SameTimeRule> winner, Vector<Integer> resolvedReasons);
	public abstract ResultSet getPossibleConflicts();
	public abstract ResultSet getResolvedConflicts();
	public abstract ResultSet getResolvedConflicts(int iteration);
	public abstract ResultSet getResultsTableContent();

	public abstract void erasePossibleConflictsTable(); //deleting conflict table

//	public abstract void dropPossibleConflictsTable();
//	public abstract void createPossibleConflicts();
}
