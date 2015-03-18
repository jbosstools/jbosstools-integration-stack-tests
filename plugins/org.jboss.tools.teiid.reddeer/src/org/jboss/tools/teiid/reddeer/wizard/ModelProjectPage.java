package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 * 
 */
public class ModelProjectPage extends WizardPage {

	public static final String LABEL_PROJECT_NAME = "Project name:";

	public void setProjectName(String projectName) {
		new LabeledText(LABEL_PROJECT_NAME).setText(projectName);
	}

}
