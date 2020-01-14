package edu.casetools.icase.mreasoner.compiler;

import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.casetools.icase.mreasoner.compiler.iterations.MCompiler_Iteration;
import edu.casetools.icase.mreasoner.compiler.realtime.MCompiler;
import edu.casetools.icase.mreasoner.core.MSpecification;
import edu.casetools.icase.mreasoner.core.elements.time.conf.TimeConfigs.EXECUTION_MODE;

public class MSpecificationLoader {

	
	
	public MSpecification readSystemSpecifications_RealTime(String configsFileName) throws FileNotFoundException, edu.casetools.icase.mreasoner.compiler.realtime.ParseException{
		MCompiler reader = new MCompiler(new FileReader(configsFileName));
		return reader.readSystemSpecifications();
	}
	
	public MSpecification readSystemSpecifications_Iteration(String configsFileName) throws FileNotFoundException, edu.casetools.icase.mreasoner.compiler.iterations.ParseException{
		MCompiler_Iteration reader = new MCompiler_Iteration(new FileReader(configsFileName));
		return reader.readSystemSpecifications();
	}
	
	public MSpecification getMSpecification(EXECUTION_MODE mode, String specificationFilePath) throws FileNotFoundException, edu.casetools.icase.mreasoner.compiler.iterations.ParseException, edu.casetools.icase.mreasoner.compiler.realtime.ParseException{
		MSpecification minput = new MSpecification();
		
		
		if(mode.equals(EXECUTION_MODE.SIMULATION_ITERATION)){
			minput = readSystemSpecifications_Iteration( specificationFilePath );
		}else if(mode.equals(EXECUTION_MODE.SIMULATION_REAL_TIME) || mode.equals(EXECUTION_MODE.REAL_ENVIRONMENT)  ){
			minput =  readSystemSpecifications_RealTime( specificationFilePath );
		}

		return minput;
	}
	

}
