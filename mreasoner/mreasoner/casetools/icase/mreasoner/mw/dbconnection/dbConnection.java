package edu.casetools.icase.mreasoner.mw.dbconnection;

import java.sql.*;

public class dbConnection {
	
	private Connection conn = null;
	private Statement stmt = null;
	private String dbString =  "jdbc:postgresql://localhost:5432/sensors";
	private String dbUser = "postgres";
	private String dbPass = "123456";
	
	public  dbConnection(){ 
		
	}
	
	public void connect() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		   
	    //Class.forName("com.mysql.jdbc.Driver").newInstance();
	    Class.forName("org.postgresql.Driver").newInstance();
		conn = DriverManager.getConnection( this.dbString, this.dbUser, this.dbPass);
	    stmt = conn.createStatement();
	  
	 	    
   }
   
   public ResultSet executeQuery(String query, String type) throws SQLException{
		   ResultSet resultSet = null;
		   if(conn != null){
			   stmt = conn.createStatement();
			   if(stmt != null){
				   switch(type){
					   case "select":
						  // System.out.println("SEEEEELECT");
						   resultSet = stmt.executeQuery(query);
						   break;
					   default:
						   stmt.executeUpdate(query);
						//   resultSet = stmt.executeQuery(query);
						   stmt.close();
						   break;
				   }
			//	   resultSet = stmt.executeQuery(query);
				   
			//	   stmt.close();
			   }
		   }
		   return resultSet;
		}
	   
	public void close(){
		//conn.close();
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
