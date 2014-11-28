package org.jboss.tools.switchyard.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;

/**
 * 
 * @author apodhrad
 *
 */
public class SwitchYardRequirement implements Requirement<SwitchYard>, CustomConfiguration<SwitchYardConfig> {

	private SwitchYardConfig config;
	private SwitchYard switchyard;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface SwitchYard {
		String configurationVersion() default SwitchYardProjectWizard.DEFAULT_CONFIGURATION_VERSION;

		String libraryVersion() default SwitchYardProjectWizard.DEFAULT_LIBRARY_VERSION;

		Server server() default @Server(type = {}, state = ServerReqState.PRESENT);
	}

	@Override
	public Class<SwitchYardConfig> getConfigurationClass() {
		return SwitchYardConfig.class;
	}

	@Override
	public void setConfiguration(SwitchYardConfig config) {
		this.config = config;
	}

	@Override
	public boolean canFulfill() {
		ServerReqType[] type = switchyard.server().type();
		if (type.length == 0) {
			return true;
		}
		for (int i = 0; i < type.length; i++) {
			if (type[i].matches(config.getServerBase())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void fulfill() {
		new CleanWorkspaceRequirement().fulfill();
		ServerBase serverBase = config.getServerBase();
		if (serverBase == null) {
			return;
		}
		if (!serverBase.exists()) {
			serverBase.create();
		}
		serverBase.setState(switchyard.server().state());
	}

	@Override
	public void setDeclaration(SwitchYard switchyard) {
		this.switchyard = switchyard;
	}

	public SwitchYardConfig getConfig() {
		return this.config;
	}

	public String getTargetRuntimeLabel() {
		return getConfig().getTargetRuntime() + " [" + getConfig().getName() + "]"; 
	}
	
	public SwitchYardProjectWizard project(String name) {
		return new SwitchYardProjectWizard(name, config.getConfigurationVersion(), config.getLibraryVersion(),
				config.getTargetRuntime());
	}
}
