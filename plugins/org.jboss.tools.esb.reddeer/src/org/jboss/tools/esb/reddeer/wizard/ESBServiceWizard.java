package org.jboss.tools.esb.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.esb.reddeer.widget.LabeledTextExt;

/**
 * 
 * @author apodhrad
 *
 */
public class ESBServiceWizard extends WizardDialog {

	public static final String NAME = "Name:*";
	public static final String CATEGORY = "Category:*";
	public static final String DESCRIPTION = "Description:*";

	public ESBServiceWizard() {
		super();
		new DefaultShell("Add Service");
	}

	public void setName(String name) {
		new LabeledTextExt(NAME).setText(name);
	}

	public void setCategory(String category) {
		new LabeledTextExt(CATEGORY).setText(category);
	}

	public void setDescription(String description) {
		new LabeledTextExt(DESCRIPTION).setText(description);
	}
}
