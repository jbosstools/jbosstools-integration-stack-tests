package org.jboss.tools.runtime.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement.SAP;

/**
 * 
 * @author apodhrad
 * 
 */

public class SAPRequirement implements Requirement<SAP>, CustomConfiguration<SAPConfig> {

	private static final Logger LOGGER = Logger.getLogger(SAPRequirement.class);

	private SAPConfig config;
	private SAP sap;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface SAP {

	}

	@Override
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {

	}

	@Override
	public void setDeclaration(SAP sap) {
		this.sap = sap;
	}

	@Override
	public Class<SAPConfig> getConfigurationClass() {
		return SAPConfig.class;
	}

	@Override
	public void setConfiguration(SAPConfig config) {
		this.config = config;
	}

	public SAPConfig getConfig() {
		return this.config;
	}
}
