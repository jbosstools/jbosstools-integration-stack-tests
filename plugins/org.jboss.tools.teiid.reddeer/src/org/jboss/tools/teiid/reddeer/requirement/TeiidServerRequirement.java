package org.jboss.tools.teiid.reddeer.requirement;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerConfig;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.util.FileUtils;
import org.jboss.tools.teiid.reddeer.util.TeiidDriver;

/**
 * 
 * @author apodhrad
 *
 */
public class TeiidServerRequirement implements Requirement<TeiidServer>, CustomConfiguration<ServerConfig> {

	private ServerConfig serverConfig;
	private TeiidServer teiid;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface TeiidServer {

		ServerReqType[] type() default { ServerReqType.AS, ServerReqType.EAP };

		ServerReqState state();
	}

	@Override
	public Class<ServerConfig> getConfigurationClass() {
		return ServerConfig.class;
	}

	@Override
	public void setConfiguration(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	@Override
	public boolean canFulfill() {
		ServerReqType[] type = teiid.type();
		if (type.length == 0) {
			return true;
		}
		for (int i = 0; i < type.length; i++) {
			if (type[i].matches(serverConfig.getServerBase())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void fulfill() {
		new WorkbenchShell().maximize();
		new CleanWorkspaceRequirement().fulfill();
		new TeiidPerspective().open();
		ServerBase serverBase = serverConfig.getServerBase();
		if (serverBase == null) {
			return;
		}
		if (!serverBase.exists()) {
			serverBase.create();
		}
		serverBase.setState(teiid.state());
	}

	@Override
	public void setDeclaration(TeiidServer teiid) {
		this.teiid = teiid;
	}

	public ServerConfig getServerConfig() { 
		return serverConfig;
	}
	
	public String getName() {
		return serverConfig.getName();
	}
	
	public String getTeiidDriverPath() {
		String serverPath = serverConfig.getServerBase().getHome();
		List<File> files = FileUtils.find(serverPath, "teiid.*[jdbc|client].jar");
		if (files.isEmpty() || !files.get(0).exists()) {
			throw new RuntimeException("Cannot find teiid driver");
		}
		return files.get(0).getAbsolutePath();
	}

	public TeiidDriver getTeiidDriver() {
		String teiidDriverPath = getTeiidDriverPath();
		return new TeiidDriver(teiidDriverPath);
	}

}
