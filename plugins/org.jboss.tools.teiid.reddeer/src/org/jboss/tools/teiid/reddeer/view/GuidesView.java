package org.jboss.tools.teiid.reddeer.view;

import static org.junit.Assert.assertEquals;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.matcher.TreeItemRegexMatcher;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.condition.IsPreviewInProgress;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt.ServerType;

public class GuidesView extends WorkbenchView {

	public GuidesView(){
		super("Teiid Designer", "Guides");
	}

	private static final String CONFIRM_UNCHANGED = "Default Instance unchanged";
	
	/**
	 * Choose specific action from action set.
	 * @param actionSet 
	 * @param action
	 */
	@SuppressWarnings("unchecked")
	public void chooseAction(String actionSet, String action) {
		open();
		
		new DefaultCombo().setSelection(actionSet);
		new DefaultTreeItem(new TreeItemRegexMatcher(action + ".*")).doubleClick();
	}
	
	/**
	 * Preview data (table) via guides action
	 * @param calledFirstTime true - sets up display property of unresolvable SQL results
	 * @param path to table (e.g. "ProjectName", "ModelName", "TABLE")
	 */
	/*public void previewData(boolean calledFirstTime, String... path){//just try-catch, no boolean param
		new GuidesView().chooseAction("Model JDBC Source", "Preview Data");
		new SWTWorkbenchBot().button("...").click();
		new DefaultTreeItem(path).select();
		new SWTWorkbenchBot().button("OK").click();
		new SWTWorkbenchBot().button("OK").click();
		
		//setup display property; only 1st time
		try {
			//what property?
			new SWTWorkbenchBot().activeShell().bot().button("Yes").click();
		} catch (Exception ex){
			//do nothing
		}
		
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
	}*/
	
	/**
	 * 
	 * @param path to table
	 */
	public void previewData(String... path){//just try-catch, no boolean param
		new GuidesView().chooseAction("Model JDBC Source", "Preview Data");
		new DefaultShell("Preview Data");
		new PushButton("...").click();
		new DefaultShell("Table or Procedure Selection");
		new DefaultTreeItem(path).select();
		new PushButton("OK").click();
		new DefaultShell("Preview Data");
		new PushButton("OK").click();
		
		//setup display property; only 1st time
		try {
			//what property?
			new DefaultShell("Change Property");
			new PushButton("Yes").click();
		} catch (Exception ex){
			//do nothing
		}
		
		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		AbstractWait.sleep(TimePeriod.SHORT);
		new WaitWhile(new IsPreviewInProgress(), TimePeriod.VERY_LONG);
	}

	public boolean canPreviewData(String expectedErrorMessage, String[] pathToTable, String previewSQL) {//nechat tak??? OR ???guides mgr (msg, string[] pathTotable, )
		if (expectedErrorMessage != null) {//OR to guides view
			new GuidesView().chooseAction("Model JDBC Source", "Preview Data");
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			assertEquals(new SWTWorkbenchBot().activeShell().getText(), expectedErrorMessage);
			new SWTWorkbenchBot().activeShell().close();
			return false;
		} else {
			new GuidesView().previewData(pathToTable);
			//SQLResult result = new SQLResultView().getByOperation(previewSQL);
			SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(previewSQL);// "select * from \""+MODEL_NAME.substring(0,MODEL_NAME.indexOf("."))+"\".\""+tableName+"\""
			assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
			return true;
		}
	}
	
	
	public boolean canPreviewData(String expectedErrorMessage, String[] pathToTable) {//nechat tak??? OR ???guides mgr (msg, string[] pathTotable, )
		String tableName = pathToTable[pathToTable.length-1];//last
		String modelName = pathToTable[pathToTable.length-2];
		if (modelName.contains(".")){
			modelName = modelName.substring(0, modelName.indexOf("."));
		}
		String previewSQL = "select * from \""+modelName+"\".\""+tableName+"\"";
		return canPreviewData(expectedErrorMessage, pathToTable, previewSQL);
	}
	
	public void setDefaultTeiidInstance(String serverName, ServerType type) {//move to guides view
		String serverURL = "No Default";
		if ((serverName != null) && (type != null)){
			serverURL = ServersViewExt.getServerURLPrefix(type) + "::admin (" + serverName + ")";
		}
		
		new GuidesView().chooseAction("Teiid",
				"Set the Default JBoss / Teiid Instance");
		
		//1 teiid server instance
		if (new SWTWorkbenchBot().activeShell().getText().equals(CONFIRM_UNCHANGED)){
			new PushButton("OK").click();
			return;
		}
		
		try {
			new DefaultCombo().setSelection(serverURL);
			new PushButton("OK").click();
		} catch (Exception ex){
			//dialog doesn't appear if only 1 server instance is defined
		}
		
		//dialog doesn't apper if restart of server precedes setup of default teiid instance
		try {
			if (new SWTWorkbenchBot().activeShell().getText().equals("Change of Server Version")){
				new PushButton("Yes").click();// save all opened editors
			} else {
				new PushButton("No").click();// disconnect from actual teiid instance (Disconnect Current Default Teiid Instance)
			}
		} catch (Exception ex){
			System.err.println(ex.getCause() + "," + ex.getMessage());
		}
		
		new PushButton("OK").click();// confirm change of default server
	}
	
	//TODO modeling actions... - the grey context menu
	//TODO support other action sets
}
