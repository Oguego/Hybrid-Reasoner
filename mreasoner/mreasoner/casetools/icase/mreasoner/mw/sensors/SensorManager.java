package edu.casetools.icase.mreasoner.mw.sensors;

import java.awt.List;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.casetools.icase.mreasoner.database.core.operations.DatabaseOperations;
import edu.casetools.icase.mreasoner.deployment.sensors.Sensors;
import edu.casetools.icase.mreasoner.mw.sensors.BLE.BLESensor;
import edu.casetools.icase.mreasoner.mw.sensors.user.userSensor;
import edu.casetools.icase.mreasoner.mw.sensors.vera.VeraSensor;
import edu.casetools.icase.mreasoner.mw.sensors.vera.VeraSensor2;

public class SensorManager {

	
		public static ArrayList<Sensors> getSensors(DatabaseOperations db){
				ArrayList<Sensors> sensors = new ArrayList<Sensors>(); 
				ResultSet dbsensors = db.getSensors();
				//List columns = new List();
				Map<String, Integer> columns = new HashMap<String, Integer>();
				
				try {
					ResultSetMetaData metadata = dbsensors.getMetaData();
				 	int columnCount = metadata.getColumnCount();    
				 	
				 	for (int i = 1; i <= columnCount; i++) {
				        columns.put(metadata.getColumnName(i), i);  
				    }
					while (dbsensors.next()) {
						//If the state is declared them is created
						
						if(db.hasColumn("results",dbsensors.getString(columns.get("state")))){
							//System.out.println(" ******************************************** MIIIIIIERDA **********************" + dbsensors.getString(columns.get("class")));
							switch (dbsensors.getString(columns.get("class"))){
									case "ble":
										sensors.add( new BLESensor(dbsensors.getString(columns.get("device")),dbsensors.getString(columns.get("state")), "0",dbsensors.getString(columns.get("type")), dbsensors.getString(columns.get("variable"))));
										System.out.println("BLE: " + dbsensors.getString(columns.get("state")));
										break;
									case "vera":
										sensors.add( new VeraSensor(dbsensors.getString(columns.get("device")),dbsensors.getString(columns.get("state")), "0",dbsensors.getString(columns.get("type")), dbsensors.getString(columns.get("variable"))));
										System.out.println("Vera: " + dbsensors.getString(columns.get("state")));
										break;
									case "vera2":
										sensors.add( new VeraSensor2(dbsensors.getString(columns.get("device")),dbsensors.getString(columns.get("state")), "0",dbsensors.getString(columns.get("type")), dbsensors.getString(columns.get("variable"))));
										break;
									case "user":
										
										sensors.add( new userSensor(dbsensors.getString(columns.get("device")),dbsensors.getString(columns.get("state")), "0",dbsensors.getString(columns.get("type")), dbsensors.getString(columns.get("variable"))));
										break;
									default:
										//nothing
										break;
							}
						
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return sensors;			
		}
}
