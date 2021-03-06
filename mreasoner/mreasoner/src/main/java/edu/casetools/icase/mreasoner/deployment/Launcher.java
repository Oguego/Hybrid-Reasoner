package edu.casetools.icase.mreasoner.deployment;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import edu.casetools.icase.mreasoner.MReasoner;
import edu.casetools.icase.mreasoner.configs.MConfigsLoader;
import edu.casetools.icase.mreasoner.configs.data.MConfigs;
import edu.casetools.icase.mreasoner.core.MSpecification;
import edu.casetools.icase.mreasoner.core.elements.time.conf.TimeConfigs.EXECUTION_MODE;
import edu.casetools.icase.mreasoner.deployment.actuators.MActuatorManager;
import edu.casetools.icase.mreasoner.deployment.sensors.MDataManagerPoll;
import edu.casetools.icase.mreasoner.deployment.sensors.MVeraLogReader;
import edu.casetools.icase.mreasoner.deployment.simulation.EventSimulator;
import edu.casetools.icase.mreasoner.actuators.device.Actuator;


public class Launcher extends Thread {

	MConfigsLoader 	   	  		  minputLoader;
	MConfigs					  mconfigs;
	MSpecification	      		  minput;
	MReasoner 		  	  		  mtpl;
	MVeraLogReader		  		  sensorManager;
	MDataManagerPoll 			  poller; 
	EventSimulator 	   		      inputSimulator;
//	Vector<LibraryThread> 		  externalLibraries;
	MActuatorManager			  actuatorManager;
	Thread 						  ActManagerThread;
	Vector<Actuator> 			  actuators;  
	Thread						  mreasonerThread;
	Thread 						  pollerThread;
	
	public Launcher(Vector<Actuator> actuators){	
		minputLoader 	     	 = new MConfigsLoader();
//		externalLibraries    	 = new Vector<LibraryThread>();
		this.actuators 		 	 = actuators;
		
		System.out.println("************ Launcher.java ******");
	}
	
	public MConfigs readMSpecification(MConfigs configs) {
		try {
			if(mconfigs != null)
				minput = minputLoader.getMSpecification(
						mconfigs.getTimeConfigs().getExecutionMode(), 
						mconfigs.getFilesConfigs().getSystemSpecificationFilePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (edu.casetools.icase.mreasoner.compiler.realtime.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (edu.casetools.icase.mreasoner.compiler.iterations.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return mconfigs;
	}	
	
	public MConfigs readMConfigs(String configFileName) {
			mconfigs = minputLoader.readConfigs(configFileName);
			
		return mconfigs;
	}	
	
	@Override
	public void run(){

		if(minput != null){
			mtpl = new MReasoner(minput, mconfigs);
			mreasonerThread = new Thread(mtpl);

			if(mconfigs.getTimeConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT)){
				deployModules(mconfigs);
			} else{
		    	inputSimulator    = new EventSimulator( minput.getEventsHistory(), mconfigs );
				inputSimulator.start();
			}
			
			if(isDeployed()){
				System.out.println("\nSTARTING REASONER..\n");
				mreasonerThread.run();	
				if(!mconfigs.getTimeConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT))
					try {
						waitForThreadEnd();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}	

		} else  System.out.println("SSH Connection error");


	}
	

	private boolean isDeployed(){
		if(mconfigs.getTimeConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT)){
			//return sensorManager.checkConnection();
			//return true;
			return pollerThread.isAlive();
		}else{
			return true;
		}
	}


	private void deployModules(MConfigs mConfigs) {
	
		System.out.println("\nDEPLOYING ACTUATOR MANAGER..");
		launchActuatorManager(mConfigs);
		System.out.println("COMPLETE\n");		
		
		
		System.out.println("\nDEPLOYING SENSOR READER..\n");
		poller = new MDataManagerPoll(this.mconfigs);
		pollerThread = new Thread(poller);
		pollerThread.start();
		//launchSensorManager(mConfigs);
		System.out.println("COMPLETE\n");
	//	this.sleepWrapper(250);
	}

	public void launchActuatorManager(MConfigs mConfigs){

		actuatorManager = new MActuatorManager(mConfigs.getDBConfigs(), actuators);
		ActManagerThread = new Thread(actuatorManager);
		ActManagerThread.start();
	}

	private void launchSensorManager(MConfigs mConfigs) {
		sensorManager = new MVeraLogReader(mConfigs);
		sensorManager.start();
		
		while(!sensorManager.getSshClient().isInitializationFinished()){
			sleepWrapper(1);
		}
		
	}
	
	public void terminate() {


			try {
				mtpl.terminate();

				if(mconfigs.getTimeConfigs().getExecutionMode().equals(EXECUTION_MODE.REAL_ENVIRONMENT))
					terminateDeployment();
				else
					waitForThreadEnd();	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
	}


	private void terminateDeployment() throws InterruptedException {
		
	/*	sensorManager.stop();
		while(!sensorManager.getSshClient().isFinalizationFinished()){
			sleepWrapper(1);
		}*/
		
		poller.terminate();
		
		if(actuatorManager != null){
			actuatorManager.terminate();
			actuatorManager.interrupt();
			actuatorManager.join();
		}
//		if(externalLibraries != null){
//			for(LibraryThread library : externalLibraries){
//				library.interrupt();
//				library.join();
//			}
//		}
		waitForThreadEnd();	
	}
	
	public void waitForThreadEnd()  throws InterruptedException {
			mreasonerThread.join();
			if(inputSimulator != null){				
			inputSimulator.terminate();
			inputSimulator.join();
			}
	}
	

	
	private void sleepWrapper(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startSSHConnection() {
	
		Scanner in = new Scanner(System.in);
		in.nextInt();
		sensorManager.stop();
		in.close();
		
	}


	
}
