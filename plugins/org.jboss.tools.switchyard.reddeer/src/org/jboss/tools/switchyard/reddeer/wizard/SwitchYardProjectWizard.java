package org.jboss.tools.switchyard.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Wizard for creating a SwitchYard project.
 * 
 * @author apodhrad
 * 
 */
public class SwitchYardProjectWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "New SwitchYard Project";

	private String name;
	private String groupId;
	private String packageName;
	private String version;
	private List<String[]> components;

	public SwitchYardProjectWizard(String name, String version) {
		super("SwitchYard", "SwitchYard Project");
		this.name = name;
		this.version = version;
		components = new ArrayList<String[]>();
	}

	public SwitchYardProjectWizard groupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public SwitchYardProjectWizard packageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public SwitchYardProjectWizard impl(String... component) {
		for (int i = 0; i < component.length; i++) {
			components.add(new String[] { "Implementation Support", component[i] });
		}
		return this;
	}

	public SwitchYardProjectWizard binding(String... component) {
		for (int i = 0; i < component.length; i++) {
			components.add(new String[] { "Gateway Bindings", component[i] });
		}
		return this;
	}
	
	public SwitchYardProjectWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public void create() {
		open();
		activate();
		setText("Project name:", name);
		next();
		activate();
		setText("Group Id:", groupId);
		setText("Package Name:", packageName);
		setVersion(version);
		selectComponents(components);
		finish();
		update();
	}

	@Override
	public void finish() {
		log.info("Finish wizard");

		new PushButton("Finish").click();

		try {
			new WaitWhile(new JobIsRunning());
			new DefaultShell("Cannot Resolve SwitchYard Dependencies");
			new PushButton("OK").click();
		} catch (Exception e) {
			// this shell pop ups only sometimes
		}
		
		TimePeriod timeout = TimePeriod.getCustom(20 * 60 * 1000);
		new WaitWhile(new ShellWithTextIsActive(DIALOG_TITLE), timeout);
		new WaitWhile(new JobIsRunning(), timeout);
	}

	public void update() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.getProjects().get(0).select();
		new ContextMenu("Maven", "Update Project...").select();
		new DefaultShell("Update Maven Project");
		new CheckBox("Force Update of Snapshots/Releases").toggle(true);
		new PushButton("OK").click();

		AbstractWait.sleep(TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	private void setText(String label, String value) {
		if (value != null) {
			new LabeledText(label).setText(value);
		}
	}

	private void setVersion(String version) {
		Combo combo = new LabeledCombo("Library Version:");
		if (version != null && combo.isEnabled()) {
			combo.setSelection(version);
		}
	}

	private void selectComponents(List<String[]> components) {
		for (String[] component : components) {
			new DefaultTreeItem(component).setChecked(true);
		}
	}
}
