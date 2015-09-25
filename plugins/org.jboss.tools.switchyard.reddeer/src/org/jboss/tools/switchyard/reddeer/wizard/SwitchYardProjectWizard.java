package org.jboss.tools.switchyard.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.tools.switchyard.reddeer.condition.SwitchYardEditorIsOpen;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;

/**
 * Wizard for creating a SwitchYard project.
 * 
 * @author apodhrad
 * 
 */
public class SwitchYardProjectWizard extends NewWizardDialog {

	public static final String PROJECT_NAME = "Project name:";
	public static final String ARTIFACT_ID = "Artifact Id:";
	public static final String GROUP_ID = "Group Id:";
	public static final String TARGET_NAMESPACE = "Target Namespace:";
	public static final String PACKAGE_NAME = "Package Name:";
	public static final String OSGI_BUNDLE = "OSGI Bundle";
	public static final String BOM_DEPENDENCY = "Use SwitchYard BOM for Dependency Management";

	public static final String DEFAULT_CONFIGURATION_VERSION = "2.0";
	public static final String DEFAULT_LIBRARY_VERSION = "2.0.0.Final";

	public static final String SWITCHYARD_COMPONENTS = "SwitchYard Components";

	public static final String DIALOG_TITLE = "New SwitchYard Project";

	private String name;
	private String groupId;
	private String packageName;
	private String configurationVersion;
	private String targetRuntime;
	private String libraryVersion;
	private List<String[]> components;

	public SwitchYardProjectWizard(String name) {
		this(name, DEFAULT_CONFIGURATION_VERSION, DEFAULT_LIBRARY_VERSION);
	}

	public SwitchYardProjectWizard(String name, String configurationVersion, String libraryVersion) {
		this(name, configurationVersion, libraryVersion, null);
	}

	public SwitchYardProjectWizard(String name, String configurationVersion, String libraryVersion, String targetRuntime) {
		super("SwitchYard", "SwitchYard Project");
		this.name = name;
		this.configurationVersion = configurationVersion;
		this.targetRuntime = targetRuntime;
		this.libraryVersion = libraryVersion;
		components = new ArrayList<String[]>();
	}

	public SwitchYardProjectWizard name(String name) {
		this.name = name;
		return this;
	}

	public SwitchYardProjectWizard setName(String name) {
		new LabeledText(PROJECT_NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(PROJECT_NAME).getText();
	}

	public String getArtifactId() {
		return new LabeledText(ARTIFACT_ID).getText();
	}

	public SwitchYardProjectWizard groupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public SwitchYardProjectWizard setGroupId(String groupId) {
		new LabeledText(GROUP_ID).setText(groupId);
		return this;
	}

	public String getGroupId() {
		return new LabeledText(GROUP_ID).getText();
	}

	public SwitchYardProjectWizard setTargetNamespace(String targetNamespace) {
		new LabeledText(TARGET_NAMESPACE).setText(targetNamespace);
		return this;
	}

	public String gettargetNamespace() {
		return new LabeledText(TARGET_NAMESPACE).getText();
	}

	public SwitchYardProjectWizard packageName(String packageName) {
		this.packageName = packageName;
		return this;
	}

	public SwitchYardProjectWizard setPackageName(String packageName) {
		new LabeledText(PACKAGE_NAME).setText(packageName);
		return this;
	}

	public String getPackageName() {
		return new LabeledText(PACKAGE_NAME).getText();
	}

	public SwitchYardProjectWizard setOSGiBundle(boolean checked) {
		new CheckBox(OSGI_BUNDLE).toggle(checked);
		return this;
	}

	public boolean isOSGiBundle() {
		return new CheckBox(OSGI_BUNDLE).isChecked();
	}

	public SwitchYardProjectWizard setBOMDependency(boolean checked) {
		new CheckBox(BOM_DEPENDENCY).toggle(checked);
		return this;
	}

	public boolean isBOMDependencyChecked() {
		return new CheckBox(BOM_DEPENDENCY).isChecked();
	}

	public boolean isBOMDependencyEnabled() {
		return new CheckBox(BOM_DEPENDENCY).isEnabled();
	}

	public SwitchYardProjectWizard setComponent(String group, String component, boolean checked) {
		new DefaultTreeItem(new DefaultTree(new DefaultGroup(SWITCHYARD_COMPONENTS)), group, component).setChecked(checked);
		return this;
	}

	public boolean isComponent(String group, String component) {
		return new DefaultTreeItem(new DefaultTree(new DefaultGroup(SWITCHYARD_COMPONENTS)), group, component).isChecked();
	}

	public SwitchYardProjectWizard impl(String... component) {
		for (int i = 0; i < component.length; i++) {
			components.add(new String[] { "Implementation Support", component[i] });
		}
		return this;
	}

	public SwitchYardProjectWizard implAll() {
		components.add(new String[] { "Implementation Support" });
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

	private void setText(String label, String value) {
		if (value != null) {
			new LabeledText(label).setText(value);
		}
	}

	public void setConfigurationVersion(String configurationVersion) {
		Combo combo = new LabeledCombo("Configuration Version:");
		if (configurationVersion != null && combo.isEnabled()) {
			combo.setSelection(configurationVersion);
		}
	}

	public String getLibraryVersion() {
		Combo combo = new LabeledCombo("Library Version:");
		return combo.getText();
	}

	public void setLibraryVersion(String libraryVersion) {
		LabeledComboExt combo = new LabeledComboExt("Library Version:");
		if (libraryVersion != null && combo.isEnabled()) {
			try {
				log.info("Select library version to '" + libraryVersion + "'");
				combo.setSelection(libraryVersion);
			} catch (Exception e) {
				log.info("Type library version to '" + libraryVersion + "'");
				combo.typeText(libraryVersion);	
			}
		}
	}

	public String getTargetRuntime() {
		Combo combo = new LabeledCombo("Target Runtime:");
		return combo.getSelection();
	}

	public void setTargetRuntime(String targetRuntime) {
		Combo combo = new LabeledCombo("Target Runtime:");
		combo.setSelection(targetRuntime);
	}

	private void selectComponents(List<String[]> components) {
		for (String[] component : components) {
			new DefaultTreeItem(component).setChecked(true);
		}
	}

	@Override
	public void open() {
		super.open();
		activate();
		setName(name);
		next();
		activate();
	}

	public void create() {
		open();
		setText("Group Id:", groupId);
		setText("Package Name:", packageName);
		if (configurationVersion != null) {
			if (configurationVersion.equals("1.0") || configurationVersion.equals("1.1")) {
				setBOMDependency(false);
			}
			setConfigurationVersion(configurationVersion);
		}
		if (targetRuntime != null) {
			setTargetRuntime(targetRuntime);
		} else if (libraryVersion != null) {
			setLibraryVersion(libraryVersion);
			String actualLibraryVersion = getLibraryVersion();
			if (!actualLibraryVersion.equals(libraryVersion)) {
				throw new RuntimeException("The library version should be '" + libraryVersion + "' but is ' "
						+ actualLibraryVersion + "'");
			}
		}
		selectComponents(components);
		finish();
		new WaitUntil(new SwitchYardEditorIsOpen(), TimePeriod.LONG);
		new SwitchYardProject(name).update();
		if (targetRuntime != null && targetRuntime.contains("Karaf Extension")) {
			new SwitchYardProject(name).enableFuseCamelNature();
		}
		System.out.println();
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

	private class LabeledComboExt extends LabeledCombo {

		private final Logger log = Logger.getLogger(LabeledComboExt.class);

		public LabeledComboExt(String label) {
			super(label);
		}

		public void setFocus() {
			log.debug("Set focus to Combo Text");
			WidgetHandler.getInstance().setFocus(swtWidget);
		}

		public void typeText(String text) {
			setText("");
			setFocus();
			KeyboardFactory.getKeyboard().type(text);
			setText(text);
		}

	}
}
