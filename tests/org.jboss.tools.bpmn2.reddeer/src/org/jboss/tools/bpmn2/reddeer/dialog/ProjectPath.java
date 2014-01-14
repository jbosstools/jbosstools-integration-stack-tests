package org.jboss.tools.bpmn2.reddeer.dialog;

/**
 * 
 * @author
 */
public class ProjectPath {

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String valueOf(String... path) {
		StringBuilder builder = new StringBuilder("/");
		for (String e : path) {
			builder.append(e);
			builder.append("/");
		}
		return builder.toString();
	}
	
}
