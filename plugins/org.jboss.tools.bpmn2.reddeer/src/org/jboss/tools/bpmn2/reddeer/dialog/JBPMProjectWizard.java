package org.jboss.tools.bpmn2.reddeer.dialog;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * 
 */
public class JBPMProjectWizard extends NewWizardDialog {
	
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
		super("jBPM", "jBPM project");
	}

	/**
	 * 
	 * @param projectName
	 * @param projectType
	 */
	public void execute(String projectName, ProjectType projectType) {
		open();
		new PushButton(projectType.getButtonIndex()).click();
		next();	
		new LabeledText("Project name:").setText(projectName);
		finish();
		assertTrue("Project '" + projectName + "' was not created", new PackageExplorer().containsProject(projectName));
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
		if(!includeTests) {
			new CheckBox("Also include a sample JUnit test for the process").click();
		}
		finish();
		assertTrue("Project '" + projectName + "' was not created", new PackageExplorer().containsProject(projectName));
	}

}
