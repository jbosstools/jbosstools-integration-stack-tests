package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.bpmn2.reddeer.ProcessEditorView;

/**
 * 
 */
public class ExportDiagramDialog {

	public enum ImageFormat {
		BMP, GIF, JPG, PNG, RLE;
	}
	
	
	ProcessEditorView editor;
	
	/**
	 * 
	 */
	public ExportDiagramDialog() {
		this(new ProcessEditorView());
	}
	
	/**
	 * 
	 * @param editor
	 */
	public ExportDiagramDialog(ProcessEditorView editor) {
		this.editor = editor;
	}
	
	/**
	 * 
	 * @param format
	 */
	public void export(ImageFormat format) {
		new DefaultShell("Export Diagram").setFocus();
		new RadioButton("All").click();
		new DefaultCombo().setSelection(format.name());
		new PushButton("OK").click();
		// TBD: handle the native file dialog
	}
	
}