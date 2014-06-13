package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.view.GuidesView;

/**
 * Imports JDBC Database to Teiid project.
 * 
 * @author Lucia Jelinkova
 * 
 */
public class ImportJDBCDatabaseWizard extends ImportWizardDialog {

	public static final String TITLE = "Import Database via JDBC";
	
	private String connectionProfile;
	private String projectName;
	private String modelName;
	private List<String> itemList;

	public ImportJDBCDatabaseWizard() {
		super("Teiid Designer", "JDBC Database >> Source Model");
		itemList = new ArrayList<String>();
	}
	
	public void openUsingGuideView() {
		new GuidesView().chooseAction("Model JDBC Source", "Create source model for JDBC data source");
		setFocus();
	}

	public void execute() {
		open();
		fill();
		finish();
	}
	
	/**
	 * Create source model for JDBC data source
	 * @param viaGuides true if should be executed via guides
	 */
	public void execute(boolean viaGuide) {
		if (viaGuide) {
			openUsingGuideView();
		} else {
			open();	
		}
		fill();
		finish();
	}
	
	public void fill() {
		setFocus();
		fillFirstPage();
		next();
		setFocus();
		fillSecondPage();
		next();
		setFocus();
		fillThirdPage();
		next();
		setFocus();
		fillFourthPage();
	}
	
	public void setFocus() {
		new DefaultShell(TITLE);
	}

	@Override
	public void finish() {
		super.finish();
		new WaitWhile(new ShellWithTextIsAvailable(TITLE), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
	}

	private void fillFirstPage() {
		new SWTWorkbenchBot().comboBoxInGroup("Connection Profile").setSelection(connectionProfile);
	}

	private void fillSecondPage() {
		new PushButton("Deselect All").click();
		new SWTWorkbenchBot().tableInGroup("Table Types").select("TABLE");
	}

	private void fillThirdPage() {
		if ((itemList != null) && (! itemList.isEmpty())){
			new PushButton("Deselect All").click();
		}
		for (String item : itemList) {
			String[] itemArray = item.split("/");
			SWTBotTreeItem treeItem = new SWTWorkbenchBot().tree().getTreeItem(itemArray[0]);
			for (int i = 1; i < itemArray.length; i++) {
				treeItem.expand();
				treeItem = treeItem.getNode(itemArray[i]);
			}
			treeItem.check();
		}
	}

	private void fillFourthPage() {
		new SWTWorkbenchBot().checkBoxInGroup("Model Object Names (Tables, Procedures, Columns, etc...)", 0).deselect();
		new SWTWorkbenchBot().textWithLabel("Model Name:").setText(modelName);
		new SWTWorkbenchBot().checkBox("Update (if existing model selected)").deselect();
		new SWTWorkbenchBot().button(1).click();

		new DefaultShell("Select a Folder");
		new SWTWorkbenchBot().tree(0).select(projectName);
		new PushButton("OK").click();
		new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
	}

	public void setConnectionProfile(String connectionProfile) {
		this.connectionProfile = connectionProfile;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void addItem(String item) {
		itemList.add(item);
	}
	
}
