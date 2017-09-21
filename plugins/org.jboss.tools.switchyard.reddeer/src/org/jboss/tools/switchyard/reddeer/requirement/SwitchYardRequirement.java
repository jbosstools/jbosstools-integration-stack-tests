package org.jboss.tools.switchyard.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.direct.preferences.Preferences;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.junit.requirement.AbstractConfigurableRequirement;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.impl.ServerAS;
import org.jboss.tools.runtime.reddeer.impl.ServerKaraf;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;

/**
 * 
 * @author apodhrad
 *
 */
public class SwitchYardRequirement extends AbstractConfigurableRequirement<SwitchYardConfiguration, SwitchYard> {

	private static final Logger LOGGER = Logger.getLogger(SwitchYardRequirement.class);

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface SwitchYard {
		ServerRequirementState state() default ServerRequirementState.PRESENT;
	}

	@Override
	public Class<SwitchYardConfiguration> getConfigurationClass() {
		return SwitchYardConfiguration.class;
	}

	@Override
	public void fulfill() {
		deleteAllProjects();
		ServerBase serverBase = configuration.getServer();
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
		serverBase.setState(annotation.state());
	}

	public void deleteAllProjects() {
		new WorkbenchShell().maximize();
		EditorHandler.getInstance().closeAll(true);

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.activate();
		pe.deleteAllProjects();
	}

	public String getTargetRuntimeLabel() {
		String targetRuntime = configuration.getTargetRuntime();
		ServerBase server = configuration.getServer();
		if (targetRuntime != null) {
			if (server != null && !targetRuntime.matches(".* \\[.*\\]")) {
				targetRuntime += " [" + server.getRuntimeName() + "]";
			}
			return targetRuntime;
		}
		if (server == null) {
			return "<None>";
		} else if (server instanceof ServerAS) {
			targetRuntime = "SwitchYard: AS7 Extension " + configuration.getSwitchyardVersion();
		} else if (server instanceof ServerKaraf) {
			targetRuntime = "SwitchYard: Karaf Extension " + configuration.getSwitchyardVersion();
		}
		return targetRuntime + " [" + server.getRuntimeName() + "]";
	}

	public String getLibraryVersionLabel() {
		String libraryVersion = configuration.getLibraryVersion();
		if (libraryVersion != null) {
			return libraryVersion;
		}
		return configuration.getSwitchyardVersion();
	}

	public SwitchYardProjectWizard project(String name) {
		SwitchYardProjectWizard project = new SwitchYardProjectWizard(name);
		project.config(configuration.getConfigurationVersion());
		project.library(getLibraryVersionLabel());
		project.runtime(getTargetRuntimeLabel());
		IntegrationPack integrationPack = configuration.getIntegrationPack();
		if (integrationPack != null) {
			project.intpkg(integrationPack.getIntegrationPackVersion());
			project.intpkgKie(integrationPack.getKieVersion());
		}
		return project;
	}

	@Override
	public void cleanUp() {
		// TODO cleanUp()
	}

}
