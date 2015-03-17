package org.jboss.tools.bpmn2.reddeer;

public class JBPM6ComplexEnvironment {
	
	private static JBPM6ComplexEnvironment instance = null;
	
	private static final String KEY="graphiti.properties";
	
	private JBPM6ComplexEnvironment() {
		// TODO Auto-generated constructor stub
	}

	public static JBPM6ComplexEnvironment getInstance() {
		if(instance == null) {
			instance = new JBPM6ComplexEnvironment();
		}
		
		return instance;
	}
	
	/**
	 * Use this method if you want selectively specify, which test should be run
	 * with graphiti and which not.
	 * Use -Dgraphiti.properties if you want all tests run with or without graphiti
	 * If -Dgraphiti.properties is used, this method won't take an effect.
	 * @param value
	 */
	public void setUseGraphiti(boolean value) {
		String actualValue = System.getProperty(KEY);
		
		if(actualValue == null) {
			if(value){
				System.setProperty(KEY, "true");
			} else {
				System.setProperty(KEY, "false");
			}
		}
	}
	
	/**
	 * 
	 * @return true if system property graphiti.preperties equals true, otherwise false
	 */
	public boolean useGraphiti() {
		String result =  System.getProperty(KEY);
		if(result != null && "true".compareTo(result) == 0) {
			return true;
		} else {
			return false;
		}
	}
}
