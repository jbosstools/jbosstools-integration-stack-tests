package org.jboss.tools.bpmn2.reddeer.dialog;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.RadioButton;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPMProjectWizard extends NewWizardDialog {

	public enum ProcessType {
		
		SIMPLE(0),
		ADVANCED(1),
		NONE(2);
		
		private int buttonIndex;
		
		ProcessType(int buttonIndex) {
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
	
	@Override
	public WizardPage getFirstPage() {
		return null;
	}	
	
	/**
	 * 
	 * @param projectName
	 */
	public void execute(String projectName) {
		execute(projectName, ProcessType.NONE, false);
	}
	
	/**
	 * 
	 * @param projectName
	 * @param processType
	 * @param includeTestClass
	 */
	public void execute(String projectName, ProcessType processType, boolean includeTestClass) {
		open();
		new LabeledText("Project name:").setText(projectName);
		next();
		new RadioButton(processType.getButtonIndex()).click();
		if (!includeTestClass) new CheckBox().click();
		finish();
		assertTrue("Project '" + projectName + "' was not created", new PackageExplorer().containsProject(projectName));
	}
	
}
