package org.jboss.tools.teiid.reddeer.requirement;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.direct.preferences.PreferencesUtil;
import org.eclipse.reddeer.eclipse.condition.ConsoleHasText;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.eclipse.wst.server.ui.editor.ServerEditor;
import org.eclipse.reddeer.junit.requirement.AbstractConfigurableRequirement;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileHelper;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.preference.TeiidDesignerPreferencePage;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.util.FileUtils;
import org.jboss.tools.teiid.reddeer.util.TeiidDriver;
import org.jboss.tools.teiid.reddeer.view.ConnectionView;
import org.jboss.tools.teiid.reddeer.view.SQLResultView;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;

/**
 * 
 * @author apodhrad
 *
 */
public class TeiidServerRequirement extends AbstractConfigurableRequirement<TeiidConfiguration, TeiidServer> {

	private TeiidConfiguration serverConfig;
	private TeiidServer teiid;

	public static final String SECURE_STORAGE_PASSWORD_TITLE = "Secure Storage Password";

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface TeiidServer {

		String[] connectionProfiles() default {};

		ServerRequirementState state();

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
	public void fulfill() {
		new WorkbenchShell().maximize();

		// the following helps to sort-of-reliably gain focus on windows
		// removing this would cause writing the username/password below
		// somewhere else (i.e. to some other window completely)
		AbstractWait.sleep(TimePeriod.getCustom(3));
		new DefaultShell();

		new CleanWorkspaceRequirement().fulfill();
		new TeiidPerspective().open();

		PreferencesUtil.setConsoleOpenedOnError(false);
		PreferencesUtil.setConsoleOpenedOnOutput(false);
		
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.open();
		new TeiidDesignerPreferencePage(preferences).setAutoToggleDataRoleChildren(true);

		// uncheck build automatically
		PreferencesUtil.setAutoBuildingOff();

		ServerBase serverBase = serverConfig.getServer();
		if (serverBase == null) {
			return;
		}

		if (!serverBase.exists()) {
			createServer(serverBase);
		}

		serverBase.setState(teiid.state());

        if (!serverBase.isRemote()) {
            try {
                new WaitUntil(new ConsoleHasText("started in"), TimePeriod.LONG);
            } catch (Exception e) {
            }
        } else { // causes freeze after define server with lots of DS
            new ConnectionView().close();
        }
		
		AbstractWait.sleep(TimePeriod.DEFAULT); //server is started but teiid instance has not been connected yet

		if (teiid.state() == ServerRequirementState.RUNNING) {
			new ServersViewExt().refreshServer(getName());
		}

		// create connection profiles
		ConnectionProfileHelper connectionProfileHelper = new ConnectionProfileHelper();
		for (String cp : teiid.connectionProfiles()) {
			ConnectionProfileConfig connectionProfile = getServerConfig().getConnectionProfile(cp);
			connectionProfileHelper.createConnectionProfile(connectionProfile);
		}

		new SQLResultView().enableUnresolvableCps();
	}

	private void createServer(ServerBase serverBase) {
		serverBase.create();

		// set username and password
		ServersView2 servers = new ServersView2();
		servers.open();
        servers.getServer(serverConfig.getServer().getName()).open();

		// this is necessary when running locally
		new WaitUntil(new ShellIsAvailable(SECURE_STORAGE_PASSWORD_TITLE), TimePeriod.SHORT, false);
		if (new ShellIsAvailable(SECURE_STORAGE_PASSWORD_TITLE).test()) {
			new DefaultShell(SECURE_STORAGE_PASSWORD_TITLE);
			new LabeledText("Password:").setText(serverConfig.getServer().getProperty("secureStoragePassword"));
			new PushButton("OK").click();
		}

		new DefaultCTabItem("Teiid Instance").activate();
		new DefaultShell();
		new DefaultText(0).setText(serverConfig.getServer().getProperty("teiidUser"));
		new DefaultText(1).setText(serverConfig.getServer().getProperty("teiidPassword"));
		new WorkbenchShell();
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenuItem(new WorkbenchShell(), "File", "Save All").select();
        new ServerEditor(serverConfig.getServer().getName()).close();
	}

	@Override
	public void setDeclaration(TeiidServer teiid) {
		this.teiid = teiid;
	}

	public TeiidConfiguration getServerConfig() {
		return serverConfig;
	}

	public String getName() {
        return serverConfig.getServer().getName();
	}

	public String getTeiidDriverPath() {
		String serverPath = serverConfig.getServer().getHome();
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

	@Override
	public void cleanUp() {
		// TODO cleanUp()
	}

}
