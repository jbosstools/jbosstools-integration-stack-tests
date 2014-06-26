package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * File binding page
 * 
 * @author apodhrad
 * 
 */
public class FileBindingPage extends OperationOptionsPage<FileBindingPage> {

	public static final String DIR_AUTO_CREATION = "Auto Create Missing Directories in File Path";
	public static final String DIRECTORY = "Directory*";

	public FileBindingPage setDirectory(String directory) {
		new LabeledText(DIRECTORY).setFocus();
		new LabeledText(DIRECTORY).setText(directory);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getDirectory() {
		return new LabeledText(DIRECTORY).getText();
	}

	public FileBindingPage setDirAutoCreation(boolean dirAutoCreation) {
		new CheckBox(DIR_AUTO_CREATION).toggle(dirAutoCreation);
		return this;
	}

	public boolean isDirAutoCreation() {
		return new CheckBox(DIR_AUTO_CREATION).isChecked();
	}

	public FileBindingPage setMoveDirectory(String moveDirectory) {
		new LabeledText("Move (Default .camel)").setFocus();
		new LabeledText("Move (Default .camel)").setText(moveDirectory);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

}
