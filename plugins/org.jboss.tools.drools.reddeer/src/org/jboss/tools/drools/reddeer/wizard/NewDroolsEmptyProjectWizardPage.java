package org.jboss.tools.drools.reddeer.wizard;

import java.util.List;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class NewDroolsEmptyProjectWizardPage extends WizardPage {

	private static final String PROJECT_NAME_LABEL = "Project name:";
	private static final String USE_DEFAULT_LOCATION_LABEL = "Use default location";
	private static final String LOCATION_LABEL = "Location:";
	private static final String JAVA_AND_DROOLS_LABEL = "Java and Drools Runtime classes";
	private static final String MAVEN_LABEL = "Maven";
	private static final String GROUP_ID_LABEL = "Group ID:";
	private static final String ARTIFACT_ID_LABEL = "Artifact ID:";
	private static final String VERSION_LABEL = "Version:";
	private static final String MANAGE_RUNTIMES_LABEL = "Manage Runtime definitions...";

	public void setProjectName(String projectName) {
		new LabeledText(PROJECT_NAME_LABEL).setText(projectName);
	}

	public void setUseDefaultLocation(boolean value) {
		new CheckBox(USE_DEFAULT_LOCATION_LABEL).toggle(value);
	}
	
	public void setLocation(String location) {
		new LabeledText(LOCATION_LABEL).setText(location);
	}
	
	public void useRuntime(String runtimeName) {
		useRuntime();
		new DefaultCombo(0).setSelection(runtimeName);
	}
	
	public void useRuntime() {
		new RadioButton(JAVA_AND_DROOLS_LABEL).click();
	}
	
	public void useMaven(String groupId, String artifactId, String version) {
		useMaven();
		new LabeledText(GROUP_ID_LABEL).setText(groupId);
		new LabeledText(ARTIFACT_ID_LABEL).setText(artifactId);
		new LabeledText(VERSION_LABEL).setText(version);
	}
	
	public void useMaven() {
		new RadioButton(MAVEN_LABEL).click();
	}
	
	public void manageRuntimeDefinition() {
		new DefaultLink(MANAGE_RUNTIMES_LABEL).click();
	}
	
	public List<String> getInstalledRuntimes() {
		return new DefaultCombo(0).getItems();
	}
}
