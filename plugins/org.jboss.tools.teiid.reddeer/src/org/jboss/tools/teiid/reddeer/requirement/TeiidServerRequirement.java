package org.jboss.tools.teiid.reddeer.requirement;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerConnType;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileHelper;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.util.FileUtils;
import org.jboss.tools.teiid.reddeer.util.TeiidDriver;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;

/**
 * 
 * @author apodhrad
 *
 */
public class TeiidServerRequirement implements Requirement<TeiidServer>, CustomConfiguration<TeiidConfiguration> {

	private TeiidConfiguration serverConfig;
	private TeiidServer teiid;
	

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface TeiidServer {

		ServerReqType[] type() default { ServerReqType.AS, ServerReqType.EAP };

		String[] connectionProfiles() default {};
		
		ServerReqState state();
		
		ServerConnType[] connectionType() default ServerConnType.LOCAL;
		
	}

	@Override
	public Class<TeiidConfiguration> getConfigurationClass() {
		return TeiidConfiguration.class;
	}

	@Override
	public void setConfiguration(TeiidConfiguration serverConfig) {
		this.serverConfig = serverConfig;
	}

	@Override
	public boolean canFulfill() {
		ServerReqType[] type = teiid.type();
		boolean typeMatches = false;
		boolean connectionTypeMatches = false;
		
		if (type.length == 0) {
			typeMatches = true;
		}
		for (int i = 0; i < type.length; i++) {
			if (type[i].matches(serverConfig.getServerBase())) {
				typeMatches = true;
			}
		}
		
		ServerConnType[] connTypes = teiid.connectionType();
		if (connTypes.length == 0) {
			connectionTypeMatches = true;
		}
		for (int i = 0; i < connTypes.length; i++) {
			if (connTypes[i].matches(serverConfig.getServerBase())) {
				connectionTypeMatches = true;
			}
		}
		
		for (String cp : teiid.connectionProfiles()){
			if (serverConfig.getConnectionProfile(cp) == null){
				return false;
			}
		}
		return typeMatches && connectionTypeMatches;
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

		// set username and password
		try {
			new WaitUntil(new ConsoleHasText("started in"), TimePeriod.LONG);
		} catch (Exception e) {
		}
		
		if (teiid.state() == ServerReqState.RUNNING){
			new ServersViewExt().refreshServer(getName());
		}
		
		ServersView servers = new ServersView();
		servers.open();
		servers.getServer(serverConfig.getName()).open();
		new DefaultCTabItem("Teiid Instance").activate();
		new DefaultText(0).typeText(serverConfig.getServerBase().getProperty("teiidUser"));
		new DefaultText(1).typeText(serverConfig.getServerBase().getProperty("teiidPassword"));
		new DefaultToolItem(new WorkbenchShell(), 0, new WithTooltipTextMatcher(new RegexMatcher("Save All.*"))).click();
		
		// create connection profiles
		ConnectionProfileHelper connectionProfileHelper = new ConnectionProfileHelper();
		for (String cp : teiid.connectionProfiles()){
			ConnectionProfileConfig connectionProfile = getServerConfig().getConnectionProfile(cp);
			connectionProfileHelper.createConnectionProfile(connectionProfile);
		}
		
	}

	@Override
	public void setDeclaration(TeiidServer teiid) {
		this.teiid = teiid;
	}

	public TeiidConfiguration getServerConfig() { 
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
