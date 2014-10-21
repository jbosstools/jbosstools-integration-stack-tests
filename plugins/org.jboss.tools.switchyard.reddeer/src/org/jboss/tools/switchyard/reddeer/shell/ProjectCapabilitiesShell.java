package org.jboss.tools.switchyard.reddeer.shell;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * 
 * @author apodhrad
 *
 */
public class ProjectCapabilitiesShell extends PreferencePage {

	public static final String VERSION_DETAILS = "SwitchYard Version Details";
	public static final String CONFIG_VERSION = "Configuration Version:";
	public static final String TARGET_RUNTIME = "Target Runtime:";
	public static final String LIB_VERSION = "Library Version:";

	public static final String COMPONENTS = "SwitchYard Components";

	protected String title;

	public ProjectCapabilitiesShell(String title) {
		this.title = title;
		new DefaultShell(title);
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
		return new DefaultTreeItem(new DefaultGroup(COMPONENTS), group, component).isChecked();
	}

	public ProjectCapabilitiesShell toggleComponent(String group, String component, boolean checked) {
		new DefaultTreeItem(new DefaultGroup(COMPONENTS), group, component).setChecked(checked);
		return this;
	}

	@Override
	public void ok() {
		AbstractWait.sleep(TimePeriod.SHORT);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		super.ok();
		new WaitWhile(new ShellWithTextIsAvailable(title));
		AbstractWait.sleep(TimePeriod.SHORT);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}
