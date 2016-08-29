package org.jboss.tools.teiid.reddeer;

import static org.junit.Assert.assertTrue;

public class AssertBot {

	/*
	 * This class contains prepared console outputs in case that assertion fails
	 * 
	 * e.g. console output contains error with actual transformation + u can find what was expected in code
	 * 
	 * */
	
	public static void transformationEquals(String actualT, String expectedT){
		assertTrue("Actual transformation is:\n" + actualT, actualT.equals(expectedT));
	}
	
	public static void transformationContains(String actualT, String expectedPart){
		assertTrue("Actual transformation is:\n" + actualT, actualT.contains(expectedPart));
	}

}
