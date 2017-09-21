package org.jboss.tools.bpel.reddeer.wizard;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class NewProjectWizard extends NewMenuWizard {

	private String name;

	public NewProjectWizard(String name) {
		super("New BPEL Project", "BPEL 2.0", "BPEL Project");
		this.name = name;
	}

	public void execute() {
		open();

		new LabeledText("Project name:").setText(name);

		finish();

		ProjectExplorer packageExplorer = new ProjectExplorer();
		packageExplorer.open();
		assertTrue("Project '" + name + "' wasn't created", packageExplorer.containsProject(name));
	}
}
