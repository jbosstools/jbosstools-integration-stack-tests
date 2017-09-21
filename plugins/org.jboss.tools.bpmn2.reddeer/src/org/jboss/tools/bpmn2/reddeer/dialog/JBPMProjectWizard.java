package org.jboss.tools.bpmn2.reddeer.dialog;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 */
public class JBPMProjectWizard extends NewMenuWizard {
	
	public enum ProjectType {

		EMPTY(0), HELLO_WORLD(1);

		private int buttonIndex;

		ProjectType(int buttonIndex) {
			this.buttonIndex = buttonIndex;
		}

		public int getButtonIndex() {
			return buttonIndex;
		}
	}

	/**
	 * 
	 */
	public JBPMProjectWizard() {
		super("", "jBPM", "jBPM project");
	}

	/**
	 * 
	 * @param projectName
	 * @param projectType
	 */
	public void execute(String projectName, ProjectType projectType, boolean withRuntime) {
		open();
		new PushButton(projectType.getButtonIndex()).click();
		next();	
		new LabeledText("Project name:").setText(projectName);
		if (!withRuntime) {
			new RadioButton("Maven").click();
			new LabeledText("Artifact ID:").setText("artifact-" + projectName);
		} else {
			new RadioButton("Java and jBPM Runtime classes").click();
		}
		finish();
		new PackageExplorerPart().open();
		assertTrue("Project '" + projectName + "' was not created", new PackageExplorerPart().containsProject(projectName));
	}
	
	public void executeForHumanResourcesExample() {
		open();
		new PushButton(2).click();
		next();	
		new DefaultCombo().setSelection("jBPM Playground at github (droolsjbpm-6.3)");
		new DefaultTreeItem("Human Resources").setChecked(true);
		finish();
	}
	
	public void execute(String projectName, boolean includeTests) {
		open();
		new PushButton(ProjectType.HELLO_WORLD.getButtonIndex()).click();
		next();
		new LabeledText("Project name:").setText(projectName);
		if(includeTests) {
			new CheckBox("Also include a sample JUnit test for the process").click();
		}
		new RadioButton("Maven").click();
		new LabeledText("Artifact ID:").setText(projectName);
		finish();
		new PackageExplorerPart().open();
		assertTrue("Project '" + projectName + "' was not created", new PackageExplorerPart().containsProject(projectName));
	}

}
