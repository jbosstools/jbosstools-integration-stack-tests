package org.jboss.tools.switchyard.reddeer.wizard;

import java.util.List;

import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.list.DefaultList;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.switchyard.reddeer.widget.RadioButtonExt;

/**
 * 
 * @author apodhrad
 *
 */
public class ValidatorWizard extends WizardDialog {

	public ValidatorWizard() {
		activate();
	}

	public ValidatorWizard activate() {
		Shell shell = new DefaultShell("New Validator");
		setShell(shell);
		return this;
	}

	public ValidatorWizard selectValidatorType(String validatorType) {
		activate();
		new DefaultList().select(validatorType);
		return this;
	}

	public List<String> getValidatorTypes() {
		throw new UnsupportedOperationException();
	}

	public ValidatorWizard setName(String name) {
		activate();
		new LabeledCombo("Name").setText(name);
		return this;
	}

	public ValidatorWizard selectSchemaType(String schemaType) {
		activate();
		new LabeledCombo("Schema Type").setSelection(schemaType);
		return this;
	}

	public ValidatorWizard setJavaClass(String javaClass) {
		activate();
		new RadioButtonExt("Java Class").click();
		new LabeledText("Class").setText(javaClass);
		return this;
	}

	public ValidatorWizard setBeanName(String beanName) {
		activate();
		new RadioButtonExt("Bean").click();
		new LabeledText("Name").setText(beanName);
		return this;
	}
}
