package org.jboss.tools.bpmn2.reddeer.dialog;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.reddeer.dialog.ProjectPath;

/**
 * 
 */
public class BPMN2GenericModelWizard extends NewWizardDialog {

	/**
	 * 
	 */
	public enum ModelType {
		
		PROCESS(0),
		COLLABORATION(1),
		CHOREOGRAPHY(2);
		
		private int buttonIndex;
		
		ModelType(int buttonIndex) {
			this.buttonIndex = buttonIndex;
		}
		
		public int getButtonIndex() {
			return buttonIndex;
		}
	}
	
	/**
	 * Creates a new instance of BPMN2GenericModelWizard. 
	 */
	public BPMN2GenericModelWizard() {
		super("BPMN2", "Generic BPMN 2.0 Diagram");
	}
	
	@Override
	public WizardPage getFirstPage() {
		return null;
	}

	/**
	 * 
	 * @param location
	 * @param fileName
	 * @param targetNamespace
	 * @param type
	 */
	public void execute(String[] location, String fileName, String targetNamespace, ModelType type) {
		open();
		new PushButton(type.getButtonIndex()).click();
		next();
		new LabeledText("Location:").setText(ProjectPath.valueOf(location));
		new LabeledText("File name:").setText(fileName);
		new LabeledText("Target Namespace:").setText(targetNamespace);
		finish();
	}
}
