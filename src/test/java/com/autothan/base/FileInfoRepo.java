package com.autothan.base;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileInfoRepo {
	
	public List<String> getWidgetScenarioFileinfo(String ScenarioFolderPath){
		
		List<String>  scflCln = new LinkedList<String>();		
		File[] files = new File(ScenarioFolderPath).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 
		for (File file : files) {
		    if (file.isFile()) {
		    	scflCln.add(file.getName());
		    }
		}		
		return scflCln;		
	}

}
