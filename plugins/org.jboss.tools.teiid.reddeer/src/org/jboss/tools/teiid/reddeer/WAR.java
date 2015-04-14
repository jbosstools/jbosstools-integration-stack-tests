package org.jboss.tools.teiid.reddeer;

import java.io.File;
import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.reference.ReferencedComposite;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;

/**
 * 
 * @author lfabriko
 *
 */
public class WAR {

	//private static final String PROP_CONTEXT_NAME = "contextName";
	private ProjectItem warProjectItem;
	private Properties warProps;
	private ProjectItem vdbProjectItem;
	
	//properties
	//public static String PROP_TYPE = "type";
	public static String JBOSSWS_CXF_TYPE = "Generate SOAP War";
	public static String RESTEASY_TYPE = "Generate REST War";
	
	private static final String CONTEXT_NAME="Context Name:";
	private static final String VDB_JNDI_NAME="VDB JNDI Name:";
	private static final String SAVE_LOC="REST WAR File Save Location:";
	private static final String MARK_AS_DEPLOYABLE = "Mark as Deployable";
	private static final String UNMARK_AS_DEPLOYABLE = "Unmark as Deployable";
	private static final String WEB_SERVER_HOST = "Web Server Host:";
	private static final String WEB_SERVER_PORT = "Web Server Port:";
	private static final String ENABLE_MTOM = "Enable MTOM";
	private static final String JBOSSWSCXF_SAVE_LOC = "WAR File Save Location:";
	public static final String USERNAME = "Username:";
	public static final String PASSWORD = "Password:";
	private static final String TARGET_NS = "Target namespace:";
	
	public static String NONE_SECURITY="None";
	public static String HTTPBasic_SECURITY="HTTPBasic";
	public static String WS_SECURITY = "WS-Security (Username Token)";
	
	private String[] pathToVDB;
	
	//to operate existing war
	public WAR(String projectName, String warName){
		if (! warName.contains(".war")){
			warName = warName.concat(".war");
		}
		this.warProjectItem = new ModelExplorer().getProject(projectName).getProjectItem(warName);
	}
	
	//to create war
	public WAR(Properties props, String... pathToVDB){
		this.warProps = props;
		this.pathToVDB = pathToVDB;
		if (! this.pathToVDB[1].contains(".vdb")){
			this.pathToVDB[1] = this.pathToVDB[1].concat(".vdb");
		}
		this.vdbProjectItem = new ModelExplorer().getProject(pathToVDB[0]).getProjectItem(pathToVDB[1]);
	}

	public void createWAR(){
		if ((warProps != null) && (vdbProjectItem != null)){
			vdbProjectItem.select();
			//props: either rest or ws cxf war
			new ContextMenu("Modeling", warProps.getProperty("type")).select();
			if (warProps.getProperty("type").equals(RESTEASY_TYPE)){
				setupRESTWAR();
			}
			if (warProps.getProperty("type").equals(JBOSSWS_CXF_TYPE)){
				setupJBossWSCXFWAR();
			}
			
			new PushButton("OK").click();
			try{//overwrite existing war
				new PushButton("Yes").click();
			} catch (Exception ex){
				
			}
			new PushButton("OK").click();
			
		}
	}

	private void setupJBossWSCXFWAR() {
		setupCommon();
		new DefaultTabItem("General").activate();
		if (warProps.containsKey("saveLocation")){
			String absPath = new File(warProps.getProperty("saveLocation")).getAbsolutePath();
			new SWTWorkbenchBot().textWithLabel(JBOSSWSCXF_SAVE_LOC).setText(warProps.getProperty("saveLocation"));
		}
		//other war creation info
		if (warProps.containsKey("host")){
			new SWTWorkbenchBot().textWithLabel(WEB_SERVER_HOST).setText(warProps.getProperty("host"));
		}
		if (warProps.containsKey("port")){
			new SWTWorkbenchBot().textWithLabel(WEB_SERVER_PORT).setText(warProps.getProperty("port"));
		}
		if (warProps.containsKey("username") && warProps.containsKey("password")){
			new SWTWorkbenchBot().textWithLabel(USERNAME).setText(warProps.getProperty("username"));
			new SWTWorkbenchBot().textWithLabel(PASSWORD).setText(warProps.getProperty("password"));
		}
		if (warProps.containsKey("enableMtom")){
			new CheckBox(ENABLE_MTOM).click();
		}
		if (warProps.containsKey("ns")){
			new SWTWorkbenchBot().textWithLabel(TARGET_NS).setText(warProps.getProperty("ns"));
		}
	}
	
	private void setupRESTWAR(){
		setupCommon();
		//save location
		if (warProps.containsKey("saveLocation")){
			String absPath = new File(warProps.getProperty("saveLocation")).getAbsolutePath();
			new SWTWorkbenchBot().textWithLabel(SAVE_LOC).setText(warProps.getProperty("saveLocation"));
		}	
	}

	private void setupCommon() {
		//getprop contextName
		if (warProps.containsKey("contextName")){
			//new LabeledText(CONTEXT_NAME).setText(warProps.getProperty("contextName"));
			new SWTWorkbenchBot().textWithLabel(CONTEXT_NAME).setText(warProps.getProperty("contextName"));
		}
		//vdb jndi name
		if (warProps.containsKey("vdbJndiName")){
			new SWTWorkbenchBot().textWithLabel(VDB_JNDI_NAME).setText(warProps.getProperty("vdbJndiName"));
		}
		//security  --> if httpbasic,...
		if (warProps.containsKey("securityType")){
			new RadioButton(warProps.getProperty("securityType")).click();
			if (warProps.getProperty("securityType").equals(HTTPBasic_SECURITY)){
				if(!warProps.getProperty("type").equals( WAR.RESTEASY_TYPE))
				new DefaultTabItem("HTTPBasic Options").activate();
				//realm, role
				if (warProps.containsKey("realm")){
					new SWTWorkbenchBot().textWithLabel("Realm:").setText(warProps.getProperty("realm"));
				}
				if (warProps.containsKey("role")){
					new SWTWorkbenchBot().textWithLabel("Role:").setText(warProps.getProperty("role"));
				}
				if(!warProps.getProperty("type").equals( WAR.RESTEASY_TYPE))
				new DefaultTabItem("General").activate();
			}
		}
	}
	
	public void deploy() {

		if (warProjectItem != null) { 

			//newly created war is not by default in the project...
			warProjectItem.select();
		} else {

			new ModelExplorer().getProject(pathToVDB[0]).getProjectItem(warProps.getProperty("contextName")+".war").select();
		}
	
		new ContextMenu(MARK_AS_DEPLOYABLE).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		//select a server to publish
		new DefaultShell().setFocus();
		if (new DefaultShell().getText().equals("Select a server to publish to")) {

			new DefaultTreeItem(warProps.getProperty("serverName")).select();
			new PushButton("OK").click();
		}
	}
	
	public void undeploy(){
		if (warProjectItem != null) {
			warProjectItem.select();
		} else {
			new ModelExplorer().getProject(pathToVDB[0]).getProjectItem(warProps.getProperty("contextName")+".war").select();
		}
		warProjectItem.select();
		new ContextMenu(UNMARK_AS_DEPLOYABLE).select();
	}

	public void delete(){
		if (warProjectItem != null) {
			warProjectItem.select();
		} else {
			new ModelExplorer().getProject(pathToVDB[0]).getProjectItem(warProps.getProperty("contextName")+".war").select();
		}
		warProjectItem.select();
		new ContextMenu("Delete").select();
	}

}
