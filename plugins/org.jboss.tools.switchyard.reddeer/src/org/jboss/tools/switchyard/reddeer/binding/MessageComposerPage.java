package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.condition.TableHasRows;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.reference.ReferencedComposite;

/**
 * 
 * @author apodhrad
 *
 */
public class MessageComposerPage extends WizardPage {

	public MessageComposerPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public MessageComposerPage setMessageComposer(String composer) {
		new PushButton("Browse...").click();
		new DefaultText().setText(composer);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		return this;
	}

}
