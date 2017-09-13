package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.condition.ConsoleHasText;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.eclipse.reddeer.workbench.core.condition.JobIsKilled;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.ServerWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author mkralik
 */

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerRequirementState.RUNNING,connectionProfiles={
		ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER
})
public class ServerManipulationTest {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private static final String LOCAL_SERVER_NAME = "localServer";
	private static final String REMOTE_SERVER_NAME = "remoteServer";
	private static final String PROJECT_NAME = "ServerProject";
	private static final String NAME_SOURCE_MODEL = "partsSourceOracle";
	private static final String VDB_NAME = "serverVDB";

	private static final ServersViewExt SView= new ServersViewExt();
	
	@Before
	public void stopAndDeleteAllServers(){
		ServersViewExt SView= new ServersViewExt();
		List<Server> servers = SView.getServers();
		for (int i = 0; i < servers.size(); i++) {
			new WaitUntil(new JobIsKilled("Refreshing server adapter list"), TimePeriod.LONG, false);
			Server tmpServer = servers.get(i);
			try{
				tmpServer.stop();
				AbstractWait.sleep(TimePeriod.SHORT);
				if(!(tmpServer.getLabel().getName().equals(teiidServer.getName()))){
					tmpServer.delete();
				}
			}catch(Exception ex){
				//if server is already stop
				if(!(tmpServer.getLabel().getName().equals(teiidServer.getName()))){
					tmpServer.delete();
				}
			}
		}
	}

	/*
	 * test if server set teiid instance after start
	 */
	@Test
	public void noRefreshTest(){
		SView.open();
		SView.getServer(teiidServer.getName()).start();
		AbstractWait.sleep(TimePeriod.DEFAULT);
		assertTrue(testDeployVDB()); 
	}

	@Test
	public void localServer(){
		String[] type = {"Red Hat JBoss Middleware","Red Hat JBoss Enterprise Application Platform 6.1+"};
		ServerWizard.openWizard()
			.setType(type)
			.setName(LOCAL_SERVER_NAME)
			.nextPage()
			.setTypeServer(ServerWizard.LOCAL_SERVER);
		new WaitUntil(new JobIsKilled("Refreshing server adapter list"), TimePeriod.LONG, false);
		ServerWizard.getInstance().finish();
		
		SView.open();
		Server newServer = SView.getServer(LOCAL_SERVER_NAME);
		SView.startServer(LOCAL_SERVER_NAME);
		AbstractWait.sleep(TimePeriod.DEFAULT);
		assertTrue(testPingToServer(newServer)); //check ping
		
		SView.setDefaultTeiidInstance(LOCAL_SERVER_NAME);
		assertTrue(checkDefaultTeiidName(LOCAL_SERVER_NAME));
		assertTrue(testDeployVDB());

		newServer.restart();
		AbstractWait.sleep(TimePeriod.DEFAULT); 
		assertTrue(testPingToServer(newServer)); //check ping after restart
		
	}

	@Test
	public void remoteServer(){
		SView.startServer(teiidServer.getName());
		
		String[] type = {"Red Hat JBoss Middleware","Red Hat JBoss Enterprise Application Platform 6.1+"};
		ServerWizard.openWizard()
			.setType(type)
			.setName(REMOTE_SERVER_NAME)
			.nextPage()
			.setTypeServer(ServerWizard.REMOTE_SERVER)
			.setControlled(ServerWizard.MANAGEMENT_OPERATIONS)
			.externallyManaged(true)
			.assignRuntime(false)
			.nextPage()
			.setHost("Local")
			.setPathToServer(teiidServer.getServerConfig().getServer().getHome())
			.nextPage();
		new WaitUntil(new JobIsKilled("Refreshing server adapter list"), TimePeriod.LONG, false);
		ServerWizard.getInstance().finish();
		
		SView.open();
		Server newServer = SView.getServer(REMOTE_SERVER_NAME);
		SView.startServer(REMOTE_SERVER_NAME);
		AbstractWait.sleep(TimePeriod.DEFAULT);
		assertTrue(testPingToServer(newServer)); //check ping
		
		SView.setDefaultTeiidInstance(REMOTE_SERVER_NAME);
		assertTrue(checkDefaultTeiidName(REMOTE_SERVER_NAME));
		assertTrue(testDeployVDB());

		newServer.restart();
		AbstractWait.sleep(TimePeriod.DEFAULT); 
		assertTrue(testPingToServer(newServer)); //check ping after restart
		
	}
	
	/**
	 * Check name of default teiid instance
	 */
	private boolean checkDefaultTeiidName(String nameServer){
		new ModelExplorer().activate();
        new WorkbenchView("Teiid Designer", "Connections").activate();
		String defaultServerName = new DefaultHyperlink(0).getText();
		return nameServer.equals(defaultServerName);
	}
	
	/**
	 * Test of teiidName and teiidPassword is correct
	 */
	private boolean testPingToServer(Server server){
		server.open();
		new DefaultCTabItem("Teiid Instance").activate();
		new WorkbenchShell();
		new LabeledText("User name").setText(teiidServer.getServerConfig().getServer().getProperty("teiidUser"));
		new LabeledText("Password").setText(teiidServer.getServerConfig().getServer().getProperty("teiidPassword"));
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultHyperlink("Test JDBC Connection").activate();
		new ShellMenuItem(new WorkbenchShell(), "File", "Save All").select();
		return !(new ConsoleHasText("payload token could not be authenticated by security domain teiid-security").test());
	}
	
	private boolean testDeployVDB(){
		new ModelExplorer().importProject(PROJECT_NAME);
		new ModelExplorer().changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER, PROJECT_NAME, NAME_SOURCE_MODEL);
		
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, NAME_SOURCE_MODEL + ".xmi")
				.finish();
		
		try{
			new ModelExplorer().deployVdb(PROJECT_NAME, VDB_NAME);
		}catch(Error ex){
			EditorHandler.getInstance().closeAll(false);
			new ModelExplorer().deleteAllProjects(true);
			return false;
		}
		EditorHandler.getInstance().closeAll(false);
		new ModelExplorer().deleteAllProjects(true);
		return true;
	}
}
