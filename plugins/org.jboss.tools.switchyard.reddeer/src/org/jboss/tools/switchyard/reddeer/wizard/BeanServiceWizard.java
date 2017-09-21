package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating a bean service.
 * 
 * @author apodhrad
 * 
 */
public class BeanServiceWizard extends ServiceWizard<BeanServiceWizard> {

	public static final String DIALOG_TITLE = "New Bean Service";

	public BeanServiceWizard() {
		super(DIALOG_TITLE);
	}

	public BeanServiceWizard setName(String name) {
		activate();
		new LabeledText("Name:").setText(name);
		return activate();
	}

	@Override
	protected void browse() {
		new PushButton(2, new WithMnemonicTextMatcher("Browse...")).click();
	}

}
