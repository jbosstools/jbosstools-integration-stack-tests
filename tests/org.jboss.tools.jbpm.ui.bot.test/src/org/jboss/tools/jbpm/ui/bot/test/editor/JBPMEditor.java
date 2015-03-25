package org.jboss.tools.jbpm.ui.bot.test.editor;

import org.jboss.reddeer.gef.editor.GEFEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class JBPMEditor extends GEFEditor {

	public JBPMEditor() {
		super();
	}

	public JBPMEditor(String title) {
		super(title);
	}

	public void insertEntity(String title, int x, int y) {
		addToolFromPalette(title, x, y);
	}
}
