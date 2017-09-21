package org.jboss.tools.bpel.reddeer.editor;

import org.eclipse.reddeer.jface.viewers.CellEditor;
import org.eclipse.reddeer.swt.impl.ccombo.DefaultCCombo;
import org.eclipse.reddeer.swt.impl.table.DefaultTableItem;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;

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
