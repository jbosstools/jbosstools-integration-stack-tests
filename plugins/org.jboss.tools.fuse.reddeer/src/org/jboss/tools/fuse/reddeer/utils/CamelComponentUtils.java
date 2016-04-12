package org.jboss.tools.fuse.reddeer.utils;

/**
 * 
 * @author apodhrad
 *
 */
public class CamelComponentUtils {

	public static final int MAX = 30;
	public static final String DOTS = "...";

	public static String getLabel(String description) {
		if (description.length() <= MAX) {
			return description;
		} else {
			return description.substring(0, MAX - 2) + DOTS;
		}
	}

}
