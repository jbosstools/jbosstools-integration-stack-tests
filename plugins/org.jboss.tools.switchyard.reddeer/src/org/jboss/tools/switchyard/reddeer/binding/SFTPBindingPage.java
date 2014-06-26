package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * SFTP binding page
 * 
 * @author apodhrad
 * 
 */
public class SFTPBindingPage extends OperationOptionsPage<SFTPBindingPage> {

	public static final String DIRECTORY = "Directory*";

	public SFTPBindingPage setDirectory(String directory) {
		new LabeledText(DIRECTORY).setFocus();
		new LabeledText(DIRECTORY).setText(directory);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getDirectory() {
		return new LabeledText(DIRECTORY).getText();
	}

}
