package org.jboss.tools.bpmn2.reddeer.dialog;

/**
 * 
 */
public class ProjectPath {

	/**
	 * 
	 * @param systemPath
	 * @return
	 */
	public static String valueOf(String... systemPath) {
		StringBuilder builder = new StringBuilder("/");
		for (String e : systemPath) {
			builder.append(e).append("/");
		}
		return builder.toString();
	}
	
}
