package org.jboss.tools.teiid.reddeer.view;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.matcher.TreeItemRegexMatcher;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewers.CellEditor;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ccombo.DefaultCCombo;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.condition.IsPreviewInProgress;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;

public class GuidesView extends WorkbenchView {

	public GuidesView() {
		super("Teiid Designer", "Guides");
	}

	/**
	 * Choose specific action from action set.
	 * 
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
	 * 
	 * @param calledFirstTime
	 *            true - sets up display property of unresolvable SQL results
	 * @param path
	 *            to table (e.g. "ProjectName", "ModelName", "TABLE")
	 */
	/*
	 * public void previewData(boolean calledFirstTime, String... path){//just try-catch, no boolean param new
	 * GuidesView().chooseAction("Model JDBC Source", "Preview Data"); new SWTWorkbenchBot().button("...").click(); new
	 * DefaultTreeItem(path).select(); new SWTWorkbenchBot().button("OK").click(); new
	 * SWTWorkbenchBot().button("OK").click();
	 * 
	 * //setup display property; only 1st time try { //what property? new
	 * SWTWorkbenchBot().activeShell().bot().button("Yes").click(); } catch (Exception ex){ //do nothing }
	 * 
	 * new WaitWhile(new IsInProgress(), TimePeriod.LONG); }
	 */

	/**
	 * 
	 * @param path
	 *            to table
	 */
	public void previewData(String... path) {// just try-catch, no boolean param
		previewDataViaActionSet("Model JDBC Source", path);
	}

	public boolean canPreviewData(String expectedErrorMessage, String[] pathToTable, String previewSQL) {// nechat
																											// tak??? OR
																											// ???guides
																											// mgr (msg,
																											// string[]
																											// pathTotable,
																											// )
		if (expectedErrorMessage != null) {// OR to guides view
			new GuidesView().chooseAction("Model JDBC Source", "Preview Data");
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			// assertEquals(new SWTWorkbenchBot().activeShell().getText(), expectedErrorMessage);
			// new SWTWorkbenchBot().activeShell().close();
			// TODO check this
			if (new ShellWithTextIsActive(expectedErrorMessage).test()) {
				new DefaultShell(expectedErrorMessage).close();
			}
			return false;
		} else {
			new GuidesView().previewData(pathToTable);
			// SQLResult result = new SQLResultView().getByOperation(previewSQL);
			SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView()
					.getByOperation(previewSQL);// "select * from
												// \""+MODEL_NAME.substring(0,MODEL_NAME.indexOf("."))+"\".\""+tableName+"\""
			assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
			return true;
		}
	}

	public boolean canPreviewData(String expectedErrorMessage, String[] pathToTable) {// nechat tak??? OR ???guides mgr
																						// (msg, string[] pathTotable, )
		String tableName = pathToTable[pathToTable.length - 1];// last
		String modelName = pathToTable[pathToTable.length - 2];
		if (modelName.contains(".")) {
			modelName = modelName.substring(0, modelName.indexOf("."));
		}
		String previewSQL = "select * from \"" + modelName + "\".\"" + tableName + "\"";
		return canPreviewData(expectedErrorMessage, pathToTable, previewSQL);
	}

	public void setDefaultTeiidInstance(String serverName) {
		chooseAction("Teiid", "Set the Default ");
		AbstractWait.sleep(TimePeriod.SHORT);
		if (new ShellWithTextIsActive("Server Selection").test()) {
			new DefaultCombo().setSelection(serverName);
			new PushButton("OK").click();
		}
		if (new ShellWithTextIsActive("Change of Teiid Version").test()) { // the teiid instances are different version
			new PushButton("Yes").click();
		}
		if (new ShellWithTextIsActive("Untested Teiid Version").test()) { // if test untestet teiid version
			new PushButton("Yes").click();
		}
		if (new ShellWithTextIsActive("Disconnect Current Default Instance").test()) { // if you want to disconnect old
																						// instance before switching
			new PushButton("Yes").click();
		}
		if (new ShellWithTextIsActive("Default Server Changed").test()) {
			new PushButton("OK").click();
		} else if (new ShellWithTextIsActive("Default server unchanged").test()) {
			new PushButton("OK").click();
		} else {
			throw new Error("Default server not been changed due to an error");
		}
	}

	// TODO modeling actions... - the grey context menu
	// TODO support other action sets

	/**
	 * Create new project via guides
	 * 
	 * @param actionSet
	 *            - actionSet name through that trigger new project wizard
	 * @param projectName
	 *            - project name
	 */
	public void createProjectViaGuides(String actionSet, String projectName) {
		chooseAction(actionSet, "Define Teiid ");
		new PushButton("New...").click();
		new LabeledText("Project name:").setText(projectName);
		new WizardDialog().finish();
		new DefaultShell("Define Model Project");
		new PushButton("OK").click();
	}

	/**
	 * previewData, can set actionset
	 * 
	 * @param actionSet
	 *            - actionSet name through that trigger preview
	 * @param path
	 *            - path
	 */
	public void previewDataViaActionSet(String actionSet, String... path) {// just try-catch, no boolean param
		this.previewDataViaActionSetWithParam(actionSet, null, path);
	}

	/**
	 * previewData, can set actionset, with param
	 * 
	 * @param actionSet
	 *            - actionSet name through that trigger preview
	 * @param param
	 *            - parameters to preview
	 * @param path
	 *            - path
	 */
	public void previewDataViaActionSetWithParam(String actionSet, String param, String... path) {
		AbstractWait.sleep(TimePeriod.SHORT);
		if (new ShellWithTextIsActive("Unsaved Models In Workspace").test()) { // win 10
			new PushButton("Yes").click();
		}
		new GuidesView().chooseAction(actionSet, "Preview Data");
		new DefaultShell("Preview Data");
		new PushButton("...").click();
		new DefaultShell("Table or Procedure Selection");
		new DefaultTreeItem(path).select();
		new PushButton("OK").click();
		new DefaultShell("Preview Data");
		new PushButton("OK").click();
		if (param != null) {
			new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
			new DefaultShell("Preview Data");
			// new LabeledText(nameParam).setText(param);
			new DefaultText(0).setText(param);
			new PushButton("OK").click();
		}

		// setup display property; only 1st time
		if (new ShellWithTextIsActive("Change Property").test()) {
			new PushButton("Yes").click();
		}

		if (new ShellWithTextIsAvailable("Data Sources Missing").test()) {
			new PushButton("Yes").click();
		}

		new DefaultShell("Custom Preview Data");
		new PushButton("OK").click();

		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		AbstractWait.sleep(TimePeriod.SHORT);
		new WaitWhile(new IsPreviewInProgress(), TimePeriod.VERY_LONG);
	}

	/**
	 * Define VDB via guides
	 * 
	 * @param actionSet
	 *            - name action set
	 * @param projectName
	 *            - project name
	 * @param vdbName
	 *            - VDB name
	 * @param models
	 *            - all models which we want add to VDB
	 */
	public void defineVDB(String actionSet, String projectName, String vdbName, String... models) {
		chooseAction(actionSet, "Define VDB");
		new DefaultShell("Define VDB");
		new PushButton("New...").click();
		VdbWizard.getInstance().setLocation(projectName).setName(vdbName).finish();
		new DefaultShell("Define VDB");
		new PushButton("OK").click();
		VdbEditor vdbEditor = new VdbEditor(vdbName + ".vdb");
		vdbEditor.addModelsToVDB(projectName, models);
		try {
			DefaultShell mm = new DefaultShell("Add File(s) to VDB");
			mm.close();
		} catch (Exception ex) {
			return;
		}
	}

	/*
	 * Check when VDB editor was opened via guides
	 */
	public boolean editVDB(String actionSet, String projectName, String vdbName) {
		new DefaultEditor(vdbName + ".vdb").close();
		chooseAction(actionSet, "Edit VDB");
		try {
			new DefaultEditor(vdbName + ".vdb"); // check when editor was opened
		} catch (Exception ex) {
			System.err.println(ex);
			return false;
		}
		return true;
	}

	/*
	 * @return list with the number of columns from results for each query
	 */
	public List<Integer> executeVDB(String actionSet, TeiidServerRequirement teiidServer, String vdbName,
			String... testSql) {
		chooseAction(actionSet, "Execute VDB");
		new PushButton("OK").click();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		List<Integer> result = new ArrayList<Integer>();
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdbName);
		for (String sql : testSql) {
			result.add(jdbchelper.getNumberOfResults(sql));
		}
		return result;
	}

	public void newServer(String serverName) {
		chooseAction("Teiid", "Configure new JBoss Server");
		new LabeledText("Server name:").setText(serverName);
		new PushButton("Next >").click();
		new DefaultCombo().setSelection(0);
		new PushButton("Finish").click();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
	}

	public boolean editServer(String serverName) {
		chooseAction("Teiid", "Edit JBoss");
		new DefaultShell("Server Selection");
		new DefaultCombo().setSelection(serverName);
		new PushButton("OK").click();
		try {
			new DefaultEditor(serverName);
		} catch (Exception ex) {
			System.err.println(ex);
			return false;
		}
		return true;
	}

	public void startAndRefreshServer(String serverName, String defaultServerName) {
		ServersView view = new ServersView();
		view.open();
		view.getServer(defaultServerName).stop();
		AbstractWait.sleep(TimePeriod.SHORT);
		view.getServer(serverName).start();
		AbstractWait.sleep(TimePeriod.LONG);
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
		chooseAction("Teiid", "Refresh ");
		new DefaultCombo().setSelection(serverName);
		new PushButton("OK").click();
		new DefaultShell("Notification");
		new PushButton("OK").click();
		new ShellMenu("File", "Save All").select();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
	}

	public void createDataSource(String name, String connectionProfile) {
		chooseAction("Model Teiid Data Source", "Create Data Source");
		new LabeledText("Data Source Name:").setText(name);
		new LabeledCombo("Connection Profile").setSelection(connectionProfile);
		new PushButton("OK").click();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
	}

	public void createSourceModelFromTeiid(String name, String sourceName, String tableName, String schema) {
		chooseAction("Model Teiid Data Source", "Create source model from Teiid ");
		new DefaultTable(new DefaultGroup("Data Sources"), 0).getItem(sourceName).click();
		new PushButton("Next >").click();
		DefaultTable importProperties = new DefaultTable(new DefaultGroup("Import Properties"), 0);
		if (tableName != null) {
			fillTextTable(importProperties, "Table Name Pattern", tableName);
		}
		if (schema != null) {
			fillTextTable(importProperties, "Schema Pattern", schema);
		}
		fillTextTable(importProperties, "Table Types", "TABLE");
		fillComboTable(importProperties, "Use Full Schema Name", "false");

		new PushButton("Next >").click();
		new LabeledText("Name:").setText(name);
		new PushButton("Next >").click();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new PushButton("Next >").click();
		new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
		new PushButton("Finish").click();
	}

	/**
	 * fill specified text into table.
	 */
	private void fillTextTable(DefaultTable table, String nameItem, String value) {
		TableItem item = null;
		item = table.getItem(nameItem);
		item.click(1);
		new DefaultText(new CellEditor(item), 0).setText(value);
	}

	/**
	 * choose specific selection into table.
	 */
	private void fillComboTable(DefaultTable table, String nameItem, String selection) {
		TableItem item = null;
		item = table.getItem(nameItem);
		item.click(1);
		new DefaultCCombo(new CellEditor(item), 0).setSelection(selection);
	}
}