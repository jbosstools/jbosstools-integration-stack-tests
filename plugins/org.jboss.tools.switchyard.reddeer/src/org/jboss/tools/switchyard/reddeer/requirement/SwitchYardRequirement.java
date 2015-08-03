package org.jboss.tools.switchyard.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.direct.preferences.Preferences;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.handler.EditorHandler;
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

	private static final Logger LOGGER = Logger.getLogger(SwitchYardRequirement.class);
	
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
		deleteAllProjects();
		ServerBase serverBase = config.getServerBase();
		if (serverBase == null) {
			return;
		}
		List<String> preferences = serverBase.getProperties("preference");
		for (String preference : preferences) {
			// Example: org.eclipse.m2e.core/eclipse.m2.userSettingsFile=settings.xml
			if (preference.matches("([^/=]+)/([^/=]+)=.+")) {
				String[] parts = preference.split("=");
				String key = parts[0];
				String value = parts[1];
				parts = key.split("/");
				String plugin = parts[0];
				String pluginKey = parts[1];
				Preferences.set(plugin, pluginKey, value);
			} else {
				LOGGER.warn("Preference '" + preference + "' doesn't match the patterm. SKIPPED");
			}
		}
		if (!serverBase.exists()) {
			serverBase.create();
		}
		serverBase.setState(switchyard.server().state());
	}
	
	public void deleteAllProjects() {
		new WorkbenchShell().maximize();
		EditorHandler.getInstance().closeAll(true);

		// workaround for deleting all projects
		Exception exception = null;
		for (int i = 0; i < 10; i++) {
			try {
				exception = null;
				new WorkbenchShell().setFocus();
				new ProjectExplorer().deleteAllProjects();
				break;
			} catch (Exception e) {
				exception = e;
			}
		}
		if (exception != null) {
			throw new RuntimeException("Cannot delete all projects", exception);
		}
	}

	@Override
	public void setDeclaration(SwitchYard switchyard) {
		this.switchyard = switchyard;
	}

	public SwitchYardConfig getConfig() {
		return this.config;
	}

	public String getTargetRuntimeLabel() {
		String targetRuntime = getConfig().getTargetRuntime(); 
		if (targetRuntime == null) {
			return null;
		}
		return targetRuntime + " [" + getConfig().getServerBase().getRuntimeName() + "]"; 
	}
	
	public SwitchYardProjectWizard project(String name) {
		return new SwitchYardProjectWizard(name, config.getConfigurationVersion(), config.getLibraryVersion(),
				getTargetRuntimeLabel());
	}
}
