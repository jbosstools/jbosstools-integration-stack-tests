package org.jboss.tools.drools.reddeer.wizard;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.swt.impl.button.CheckBox;

public class NewDroolsProjectWithExamplesWizardPage extends NewDroolsEmptyProjectWizardPage {

	private static final String RULE_SAMPLE_LABEL = "Add a sample HelloWorld rule file to this project.";
	private static final String DTABLE_SAMPLE_LABEL = "Add a sample HelloWorld decision table file to this project.";
	private static final String PROCESS_SAMPLE_LABEL = "Add a sample HelloWorld process file to this project.";

	public NewDroolsProjectWithExamplesWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void setAddSampleRule(boolean value) {
		new CheckBox(RULE_SAMPLE_LABEL).toggle(value);
	}

	public void setAddSampleDecisionTable(boolean value) {
		new CheckBox(DTABLE_SAMPLE_LABEL).toggle(value);
	}

	public void setAddSampleProcess(boolean value) {
		new CheckBox(PROCESS_SAMPLE_LABEL).toggle(value);
	}

	public void checkAll() {
		setAddSampleRule(true);
		setAddSampleDecisionTable(true);
		setAddSampleProcess(true);
	}

	public void uncheckAll() {
		setAddSampleRule(false);
		setAddSampleDecisionTable(false);
		setAddSampleProcess(false);
	}
}
