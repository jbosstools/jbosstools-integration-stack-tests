package org.jboss.tools.teiid.reddeer.editor;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;

public class TableEditor extends AbstractModelEditor {
	private static final String TABLE_EDITOR = "Table Editor";
	
	private final String fromEditor;
	
	public TableEditor(String title, String fromEditor){
		super(title);
		this.fromEditor = fromEditor;
		new DefaultCTabItem(TABLE_EDITOR).activate();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	@Override
	public void close() {
		this.getEditorViewer(fromEditor);
	}
	
	// TODO impl. edit methods
	
}
