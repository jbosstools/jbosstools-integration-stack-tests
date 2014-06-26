package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * FTP binding page
 * 
 * @author apodhrad
 * 
 */
public class FTPBindingPage extends OperationOptionsPage<FTPBindingPage> {

	public static final String DIRECTORY = "Directory*";

	public FTPBindingPage setDirectory(String directory) {
		new LabeledText(DIRECTORY).setFocus();
		new LabeledText(DIRECTORY).setText(directory);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getDirectory() {
		return new LabeledText(DIRECTORY).getText();
	}

}
