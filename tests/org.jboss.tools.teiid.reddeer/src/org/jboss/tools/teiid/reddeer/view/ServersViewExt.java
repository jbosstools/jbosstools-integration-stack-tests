package org.jboss.tools.teiid.reddeer.view;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.forms.finder.SWTFormsBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.hamcrest.Matcher;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewException;
import org.jboss.reddeer.swt.api.ToolItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.ServerHasState;

/**
 * 
 * @author lfabriko
 *
 */
public class ServersViewExt extends ServersView {
	
	private static final String OPEN_LAUNCH_CONFIG = "Open launch configuration";
	private static final Logger log = Logger.getLogger(ServersView.class);
	public static final String DV6_PREFIX_URL = "mm://localhost:9999";
	public static final String EDS5_PREFIX_URL = "mms://localhost:31443";
	private static final String REFRESH = "Refresh / Reconnect Teiid Instance Connection";
	private static final String TEIID_INSTANCE_CONFIG = "Teiid Instance Configuration";
	
	public enum ServerType{
		 EAP6, EDS5, AS5, DV6
	}
	
	public ServersViewExt() {
		super();
	}

	/*public void editLaunchConfigVMArgs(String serverName, String newText){

	}*/
	
	public void editLaunchConfigProgramArgs(String serverName, String appendedText){
		openServerOverview(serverName);
		
		new SWTFormsBot().hyperlink(OPEN_LAUNCH_CONFIG).click();
		String text = new SWTWorkbenchBot().text(1).getText();
		new SWTWorkbenchBot().text(1).setText(text + " "+ appendedText); 
		new PushButton("Apply").click();
		new PushButton("OK").click();
		//close the configuration tab
		new SWTWorkbenchBot().cTabItem(serverName).close();
	}
	
	/*public void refreshTeiidInstance(String serverName){
		
	}*/
	
	private void openServerOverview(String serverName){
		String state = new ServersView().getServer(serverName).getLabel().getState().getText();
		new DefaultTreeItem(serverName + "  ["+state+"]").select();
		new ContextMenu("Open").select();
		new SWTWorkbenchBot().cTabItem();
	}
	
	public boolean isVDBDeployed(String serverName, ServerType serverType, String vdbName) {
		boolean result = false;
		String url = getServerURLPrefix(serverType);
		String[] path = new String[5];
		Server server = new ServersView().getServer(serverName); 
		/*String state = server.getLabel().getState().getText();
		String publishState = server.getLabel().getPublishState().getText();*/ 
		
		//path[0] = serverName + "  [Started, Synchronized]";//or serverName+" [Started]" ---> z LABEL
		//path[0] = serverName + "  [" + state + ", " + publishState + "]";
		path[0] = getServerLabel(serverName);
		path[1] = TEIID_INSTANCE_CONFIG;//constant
		path[2] = url + "  [default]";//or url --> redo ala label
		path[3] = "VDBs";
		if (vdbName.contains(".")){
			path[4] = vdbName.substring(0, vdbName.indexOf("."));
		} else {
			path[4] = vdbName;
		}
		
		try {
			new DefaultTreeItem(path).select();
			result = true;
		} catch (Exception e) {
			log.error("Couldn't find VDB on path " + path[0] + "/" + path[1] + "/" + path[2] + "/" + path[3] + "/" + path[4]);
			System.err.println(e.getMessage());
		}
		try {
			path[2] = url;
			new DefaultTreeItem(path).select();
			result = true;
		} catch (Exception e) {
			log.error("Couldn't find VDB on path " + path[0] + "/" + path[1] + "/" + path[2] + "/" + path[3] + "/" + path[4]);
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	public static String getServerURLPrefix(ServerType type){
		String url = null;
		if (type.equals(ServerType.EAP6) || type.equals(ServerType.DV6)){
			url = DV6_PREFIX_URL;
		}
		if (type.equals(ServerType.EDS5) || type.equals(ServerType.AS5)){
			url = EDS5_PREFIX_URL;
		}
		return url;
	}
	
	public static ServerType determineServerType(String serverName){//move to serversview ext
		if ((serverName.contains("EAP") || serverName.contains("DV")) && serverName.contains("6")){
			return ServerType.DV6;
		}
		else if ((serverName.contains("EAP") || serverName.contains("EDS") || serverName.contains("AS") || serverName.contains("SOA")) && serverName.contains("5")){
			return ServerType.EDS5;//smazat eap
		}
		return ServerType.DV6;
	}
	
	public void startServer(String serverName){
		ServerType type = determineServerType(serverName);
		Server server = new ServersView().getServer(serverName);
		server.start();
		new WaitUntil(new ServerHasState(serverName), TimePeriod.LONG);
		//additional steps
		if (type.equals(ServerType.EDS5)){
			String label = getServerLabel(serverName);
			//refresh 
			new DefaultTreeItem(label, TEIID_INSTANCE_CONFIG).select();
			new DefaultToolItem(REFRESH).click();
			//server was refreshed
			new PushButton("OK").click();
		}
	}
	
	public String getServerLabel(String serverName){//TEST!!!
		Server server = new ServersView().getServer(serverName);
		String state = server.getLabel().getState().getText();
		String publishState = server.getLabel().getPublishState().getText();
		if (publishState != null){
			return serverName + "  [" + state + ", " + publishState  + "]";
		}
		return serverName + "  [" + state + "]";
	}
	
	public void stopServer(String serverName){//server.stop?
		Server server = new ServersView().getServer(serverName);
		server.stop();
		
		log.info("Stopping server " + server.getLabel().getName());
		/*ServerState state = server.getLabel().getState();
		if (!ServerState.STARTING.equals(state) && !state.isRunningState()){
			throw new ServersViewException("Cannot stop server because it not running");
		}
//		try {
//			new DefaultTreeItem(serverName + "  [Started, Synchronized]").select();
//		} catch (Exception ex){
//			new DefaultTreeItem(serverName + "  [Started]").select();
//		}
		new DefaultTreeItem(serverName + "  " + state.getText()).select();
		
		new ContextMenu("Stop").select();
		AbstractWait.sleep(TimePeriod.SHORT.getSeconds() * 1000);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);*/
	}
	
}
