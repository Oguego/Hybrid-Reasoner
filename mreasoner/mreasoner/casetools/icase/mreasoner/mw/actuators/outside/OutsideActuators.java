package edu.casetools.icase.mreasoner.mw.actuators.outside;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import edu.casetools.icase.mreasoner.actuators.device.Actuator;
import edu.casetools.icase.mreasoner.mw.dbconnection.dbConnection;

//Move the logical to MODEL.JAVA en GUI for automatic process using the database
public class OutsideActuators {

		static dbConnection db = new dbConnection();
		
		public static Vector<Actuator> loadActuatorsConfig(Vector<Actuator> actuators) {
			HashMap<String, String> params = null;
			int numberOfColumns;
		
			try {
				ResultSet rs = getOutsideActuators();
			
				
				ResultSetMetaData metadata = rs.getMetaData();
				numberOfColumns = metadata.getColumnCount();
			//	metadata.le
			//	System.out.print("DATA " + rs.getFetchSize() + " Columns " + numberOfColumns);		
					while(rs.next()){
				//		System.out.print("Element");
						int i = 1;
						params = new HashMap<String, String>();
						while(i <= numberOfColumns) {
							params.put(metadata.getColumnName(i),rs.getString(i));
							i++;
				        }
											
						OutsideConfigs configs =  new OutsideConfigs(params);
						OutsideActuator actuator = new OutsideActuator(configs);	
						System.out.print("Loading Actuator-State " + actuator.getConfigs().getState() );
						actuators.add(actuator);
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		//	actuators.push();
			db.close();
			
			return actuators;
			
			
		}

		private static ResultSet getOutsideActuators() {
			ResultSet resultSet=null;
			String query = "Select context, state, type  from actuators";
			try {
			   db.connect();
			   resultSet = db.executeQuery(query, "select");
		//		System.out.print("DATA " + resultSet.
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultSet;
		}
}
