package org.jboss.tools.fuse.reddeer.editor;

import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;

/**
 * Represents 'Source' tab of the Camel Route Editor
 * 
 * @author tsedmik
 */
public class SourceEditor extends DefaultEditor {

	private DefaultStyledText editor = new DefaultStyledText();

	/**
	 * Sets position of the cursor to the specified position
	 * 
	 * @param position position in the editor
	 */
	public void setCursorPosition(int position) {

		editor.selectPosition(position);
	}

	/**
	 * Inserts given text on the cursor position
	 * 
	 * @param text text to be inserted
	 */
	public void insertTest(String text) {

		editor.insertText(text);
	}

	/**
	 * Returns text in the editor
	 * 
	 * @return text in the editor
	 */
	public String getText() {

		return editor.getText();
	}
}
