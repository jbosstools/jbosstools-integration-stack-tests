package org.jboss.tools.esb.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * 
 * @author apodhrad
 *
 */
public class ESBServiceWizard extends WizardDialog {

	public static final String NAME = "Name:*";
	public static final String CATEGORY = "Category:*";
	public static final String DESCRIPTION = "Description:*";

	private SWTWorkbenchBot bot = new SWTWorkbenchBot();
	
	public ESBServiceWizard() {
		super();
		new DefaultShell("Add Service");
	}

	public void setName(String name) {
		bot.textWithLabel(NAME).setText(name);
	}

	public void setCategory(String category) {
		bot.textWithLabel(CATEGORY).setText(category);
	}

	public void setDescription(String description) {
		bot.textWithLabel(DESCRIPTION).setText(description);
	}
}
