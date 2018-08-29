package com.autothan.base;

public class RunTCInfo {
	
	String TestStepId;
	String TestStepName;
	String TestStepStatus;
	String TestStepError;
	String JSError;
	String JSErrorDescription;
	
	
	public RunTCInfo(String testStepId, String testStepName, String testStepStatus, String testStepError, String jsError, String jsErrorDescription){
		
		this.TestStepId = testStepId;
		this.TestStepName = testStepName;
		this.TestStepStatus = testStepStatus;
		this.TestStepError = testStepError;
		this.JSError = jsError;
		this.JSErrorDescription = jsErrorDescription;
	}


	public String getTestStepId() {
		return TestStepId;
	}


	public String getTestStepName() {
		return TestStepName;
	}


	public String getTestStepStatus() {
		return TestStepStatus;
	}


	public String getTestStepError() {
		return TestStepError;
	}


	public String getJSError() {
		return JSError;
	}


	public String getJSErrorDescription() {
		return JSErrorDescription;
	}
	
	
	

}
