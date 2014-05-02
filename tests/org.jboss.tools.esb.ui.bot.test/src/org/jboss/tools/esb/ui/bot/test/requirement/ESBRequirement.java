package org.jboss.tools.esb.ui.bot.test.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.junit.requirement.PropertyConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.tools.esb.ui.bot.test.requirement.ESBRequirement.ESB;

/**
 * 
 * @author apodhrad
 * 
 */
public class ESBRequirement implements Requirement<ESB>, PropertyConfiguration {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ESB {
		String version() default "4.10";
	}

	private ESB esb;

	private String version;
	private String path;

	@Override
	public boolean canFulfill() {
		return compareVersions(version, esb.version()) >= 0;
	}

	@Override
	public void fulfill() {
		System.out.println("Fullfilling ESB requirement with version = '" + version + "' and path = '" + path + "'");
	}

	@Override
	public void setDeclaration(ESB esb) {
		this.esb = esb;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static int compareVersions(String v1, String v2) {
		// BUG: 1.1.0 is bigger than 1.1

		String[] components1 = v1.split("\\.");
		String[] components2 = v2.split("\\.");
		int length = Math.min(components1.length, components2.length);
		for (int i = 0; i < length; i++) {
			int result = new Integer(components1[i]).compareTo(Integer.parseInt(components2[i]));
			if (result != 0) {
				return result;
			}
		}
		return Integer.valueOf(components1.length).compareTo(Integer.valueOf(components2.length));
	}

}
