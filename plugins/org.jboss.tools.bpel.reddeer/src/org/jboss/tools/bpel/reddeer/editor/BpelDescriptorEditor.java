package org.jboss.tools.bpel.reddeer.editor;

import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.bpel.reddeer.widget.CellEditor;
import org.jboss.tools.bpel.reddeer.widget.DefaultCCombo;

/**
 * 
 * @author apodhrad
 * 
 */
public class BpelDescriptorEditor extends DefaultEditor {

	public BpelDescriptorEditor() {
		super();
	}

	public void setAssociatedPort(String port) {
		CellEditor cellEditor = new CellEditor(new DefaultTableItem(), 1);
		cellEditor.activate();
		new DefaultCCombo(cellEditor, 0).setSelection(port);
		cellEditor.deactivate();

		save();
	}

}
