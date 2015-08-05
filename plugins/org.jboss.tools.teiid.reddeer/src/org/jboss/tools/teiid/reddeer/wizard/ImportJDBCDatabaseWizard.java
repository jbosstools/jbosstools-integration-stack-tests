package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.view.GuidesView;

/**
 * Imports JDBC Database to Teiid project.
 * 
 * @author Lucia Jelinkova
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
		new WaitWhile(new ShellWithTextIsAvailable("Progress Information"), TimePeriod.LONG);
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
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new WaitWhile(new ShellWithTextIsAvailable(TITLE), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	private void fillFirstPage() {

		new SWTWorkbenchBot().comboBoxInGroup("Connection Profile").setSelection(connectionProfile);
	}

	private void fillSecondPage() {

		new PushButton("Deselect All").click();
		new SWTWorkbenchBot().tableInGroup("Table Types").select("TABLE");
	}

	private void fillThirdPage() {

		if ((itemList != null) && (! itemList.isEmpty())) {
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

		new DefaultText(new DefaultGroup(""), 0).setText(modelName);
		new CheckBox("Update (if existing model selected)").toggle(false);
		new PushButton(1, new WithMnemonicTextMatcher("...")).click();

		new SelectTargetFolder().select(projectName);
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
