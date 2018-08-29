package com.autothan.base;


public class TransformTestReport {
	
	public void getTCData(RunTCInfo runTCInfo){
		
		System.out.println("*************************************************************************************");
		
		System.out.println("%%%%%%% TC Step id: "+ runTCInfo.getTestStepId());
		System.out.println("%%%%%%% TC Step details"+ runTCInfo.getTestStepName());
	}
	
	
	public void getTStepData(RunTCInfo runTCInfo){
		
		System.out.println("*************************************************************************************");
		System.out.println("%%%%%%% TC Step Status: "+ runTCInfo.getTestStepStatus());
		System.out.println("%%%%%%% TC Step Error: "+ runTCInfo.getTestStepError());
	}
	
	

}
