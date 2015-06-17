package org.jboss.tools.fuse.reddeer.utils;

/**
 * 
 * @author apodhrad
 *
 */
public class CamelComponentUtils {

	public static String getLabel(String description) {
		if (description.length() <= 20) {
			return description;
		} else {
			return description.substring(0, 17) + "...";
		}
	}

}
