package org.jboss.tools.drools.reddeer.properties;

import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

public class DroolsProjectProperties {
	private final String projectName;

	public DroolsProjectProperties(String projectName) {
		this.projectName = projectName;
	}

	public void open() {
		PackageExplorerPart explorer = new PackageExplorerPart();
		explorer.open();
		explorer.getProject(projectName).select();
		new ContextMenuItem("Properties").select();
		new DefaultTreeItem("Drools").select();
	}

	public void setEnableSpecificSettings(boolean value) {
		new CheckBox("Enable project specific settings").toggle(value);
	}

	public void setDefaultDroolsRuntime(String runtimeName) {
		setEnableSpecificSettings(true);
		new LabeledCombo("Runtime:").setSelection(runtimeName);
	}

	public void ok() {
		new PushButton("OK").click();
	}

	public void cancel() {
		new PushButton("Cancel").click();
	}
}
