package org.jboss.tools.switchyard.reddeer.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.direct.preferences.Preferences;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.impl.ServerAS;
import org.jboss.tools.runtime.reddeer.impl.ServerKaraf;
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

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.activate();
		for (Project project : pe.getProjects()) {
			try {
				DeleteUtils.forceProjectDeletion(project, true);
			} catch (Exception e) {
				LOGGER.debug("Delete project '" + project.getName() + "' via Eclipse API ");
				org.jboss.reddeer.direct.project.Project.delete(project.getName(), true, true);
			}
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
		ServerBase server = getConfig().getServerBase();
		if (targetRuntime != null) {
			if (server != null && !targetRuntime.matches(".* \\[.*\\]")) {
				targetRuntime += " [" + server.getRuntimeName() + "]";
			}
			return targetRuntime;
		}
		if (server == null) {
			return "<None>";
		} else if (server instanceof ServerAS) {
			targetRuntime = "SwitchYard: AS7 Extension " + getConfig().getSwitchyardVersion();
		} else if (server instanceof ServerKaraf) {
			targetRuntime = "SwitchYard: Karaf Extension " + getConfig().getSwitchyardVersion();
		}
		IntegrationPack integrationPack = getConfig().getIntegrationPack();
		if (integrationPack != null) {
			if (server instanceof ServerAS) {
				targetRuntime = "Fuse Integration " + integrationPack.getIntegrationPackVersion();
			}
			if (server instanceof ServerKaraf) {
				targetRuntime = "SwitchYard: Integration Extension " + integrationPack.getIntegrationPackVersion();
			}
		}
		return targetRuntime + " [" + server.getRuntimeName() + "]";
	}

	public String getLibraryVersionLabel() {
		String libraryVersion = getConfig().getLibraryVersion();
		if (libraryVersion != null) {
			return libraryVersion;
		}
		IntegrationPack integrationPack = getConfig().getIntegrationPack();
		if (integrationPack != null) {
			libraryVersion = integrationPack.getIntegrationPackVersion();
		} else {
			libraryVersion = getConfig().getSwitchyardVersion();
		}
		return libraryVersion;
	}

	public SwitchYardProjectWizard project(String name) {
		SwitchYardProjectWizard project = new SwitchYardProjectWizard(name);
		project.config(config.getConfigurationVersion());
		project.library(getLibraryVersionLabel());
		project.runtime(getTargetRuntimeLabel());
		project.switchyardVersion(config.getSwitchyardVersion());
		return project;
	}

	@Override
	public void cleanUp() {
		// TODO cleanUp()
	}

}
