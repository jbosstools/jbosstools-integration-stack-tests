package org.jboss.tools.switchyard.reddeer.shell;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.jface.preference.PreferenceDialog;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

/**
 * 
 * @author apodhrad
 *
 */
public class ProjectCapabilitiesShell extends PreferenceDialog {

	public static final String VERSION_DETAILS = "SwitchYard Version Details";
	public static final String CONFIG_VERSION = "Configuration Version:";
	public static final String TARGET_RUNTIME = "Target Runtime:";
	public static final String LIB_VERSION = "Library Version:";

	public static final String COMPONENTS = "SwitchYard Components";

	public ProjectCapabilitiesShell(String title) {
		super(title);
	}

	public String getConfigurationVersion() {
		return new LabeledCombo(new DefaultGroup(VERSION_DETAILS), CONFIG_VERSION).getSelection();
	}

	public ProjectCapabilitiesShell setConfigurationVersion(String configurationVersion) {
		new LabeledCombo(new DefaultGroup(VERSION_DETAILS), CONFIG_VERSION).setSelection(configurationVersion);
		return this;
	}

	public String getLibraryVersion() {
		return new LabeledCombo(new DefaultGroup(VERSION_DETAILS), LIB_VERSION).getSelection();
	}

	public ProjectCapabilitiesShell setLibraryVersion(String libraryVersion) {
		new LabeledCombo(new DefaultGroup(VERSION_DETAILS), LIB_VERSION).setSelection(libraryVersion);
		return this;
	}

	public String getTargetRuntime() {
		return new LabeledCombo(new DefaultGroup(VERSION_DETAILS), TARGET_RUNTIME).getSelection();
	}

	public ProjectCapabilitiesShell setTargetRuntime(String targetRuntime) {
		new LabeledCombo(new DefaultGroup(VERSION_DETAILS), TARGET_RUNTIME).setSelection(targetRuntime);
		return this;
	}

	public boolean isComponentChecked(String group, String component) {
		return new DefaultTreeItem(new DefaultTree(new DefaultGroup(COMPONENTS)), group, component).isChecked();
	}

	public ProjectCapabilitiesShell toggleComponent(String group, String component, boolean checked) {
		new DefaultTreeItem(new DefaultTree(new DefaultGroup(COMPONENTS)), group, component).setChecked(checked);
		return this;
	}

	public void ok() {
		AbstractWait.sleep(TimePeriod.SHORT);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		super.ok();
	}
}
