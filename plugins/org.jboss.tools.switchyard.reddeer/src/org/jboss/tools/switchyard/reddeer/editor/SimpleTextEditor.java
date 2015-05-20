package org.jboss.tools.switchyard.reddeer.editor;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class SimpleTextEditor extends TextEditor {

	public SimpleTextEditor(String fileName) {
		super(fileName);
	}

	public SimpleTextEditor type(String text) {
		AbstractWait.sleep(TimePeriod.SHORT);
		insertText(getCursorOffset(), text);
		save();
		return this;
	}

	public SimpleTextEditor typeAfter(String word, String text) {
		setCursorPosition(getLineNum(word) + 1, 0);
		return type(text);
	}

	public SimpleTextEditor typeBefore(String word, String text) {
		setCursorPosition(getLineNum(word) - 1, 0);
		return type(text);
	}

	public SimpleTextEditor deleteLine(int lineNum) {
		selectLine(lineNum);
		new ShellMenu("Edit", "Delete").select();
		save();
		return this;
	}

	public SimpleTextEditor deleteLineWith(String word) {
		return deleteLine(getLineNum(word));
	}

	public SimpleTextEditor newLine() {
		return type("\n");
	}

	public void saveAndClose() {
		formatText();
		save();
		close();
	}

	public SimpleTextEditor formatText() {
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("Source", "Format").select();
		new ShellMenu("Source", "Organize Imports").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new PushButton("Select All").click();
		new PushButton("OK").click();
		save();

		return this;
	}

	public int getLineNum(String word) {
		int numberOfLines = getNumberOfLines();
		for (int i = 0; i < numberOfLines; i++) {
			String line = getTextAtLine(i);
			if (line.contains(word)) {
				return i;
			}
		}
		throw new RuntimeException("Cannot find line with '" + word + "'");
	}

	/**
	 * Generate getters and setters to all attributes
	 * 
	 * @param firstAttribute
	 *            name of first attribute
	 */
	public void generateGettersSetters(String firstAttribute) {
		setCursorPosition(getLineNum(firstAttribute), 0);
		new ShellMenu("Source", "Generate Getters and Setters...").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new PushButton("Select All").click();
		new PushButton("OK").click();
		save();
	}
}
