package org.jboss.tools.esb.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Wizard for creating project examples
 * 
 * @author tsedmik
 */
public class ProjectExamples extends NewWizardDialog {

	private final static String DIALOG_TITLE = "New Project Example";
	private final static String[] MENU_PATH = {"Examples", "JBoss Tools", "Project Examples"};

	public ProjectExamples() {
		super(MENU_PATH);
	}

	@Override
	public void open() {
		AbstractWait.sleep(TimePeriod.SHORT);
		super.open();
		new WaitUntil(new ShellWithTextIsAvailable(DIALOG_TITLE), TimePeriod.NORMAL);
		new DefaultShell(DIALOG_TITLE);
	}

	@Override
	public void finish() {
		new DefaultShell(DIALOG_TITLE);
		new FinishButton().click();
		new WaitUntil(new ShellWithTextIsAvailable(DIALOG_TITLE), TimePeriod.VERY_LONG);
		
		new DefaultShell(DIALOG_TITLE);
		if (new CheckBox(0).isEnabled()) {
			new CheckBox(0).toggle(false);
		}
		new CheckBox(1).toggle(false);
		
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsAvailable(DIALOG_TITLE));
	}

	/**
	 * Selects a given project
	 * 
	 * @param path Path to the given project
	 */
	public void selectProject(String[] path) {
		new DefaultTreeItem(0, path).select();
	}

	/**
	 * Accesses project examples related to JBoss ESB
	 * 
	 * @param version Version of used SOA Platform ('5.0')
	 * @return List of the ESB projects examples
	 */
	public List<String[]> getESBProjects(String version) {

		List<TreeItem> topItems = new ArrayList<TreeItem>();
		List<String[]> result = new ArrayList<String[]>();
		for (TreeItem item : new DefaultTree().getItems()) {
			if (item.getText().startsWith("ESB") && item.getText().endsWith(version)) {
				topItems.add(item);
			}
		}
		for (TreeItem item : topItems) {
			for (TreeItem subItem : item.getItems()) {
				String[] temp = new String[2];
				temp[0] = item.getText();
				temp[1] = subItem.getText();
				result.add(temp);
			}
		}
		return result;
	}
}
