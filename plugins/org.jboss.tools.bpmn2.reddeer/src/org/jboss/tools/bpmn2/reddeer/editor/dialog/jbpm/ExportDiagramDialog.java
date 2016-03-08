package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.bpmn2.reddeer.GEFProcessEditor;

/**
 * 
 */
public class ExportDiagramDialog {

	public enum ImageFormat {
		BMP, GIF, JPG, PNG, RLE;
	}

	GEFProcessEditor editor;

	/**
	 * 
	 */
	public ExportDiagramDialog() {
		this(new GEFProcessEditor());
	}

	/**
	 * 
	 * @param editor
	 */
	public ExportDiagramDialog(GEFProcessEditor editor) {
		this.editor = editor;
	}

	/**
	 * 
	 * @param format
	 */
	public void export(ImageFormat format) {
		new DefaultShell("Export Diagram");
		new RadioButton("All").click();
		new DefaultCombo().setSelection(format.name());
		new PushButton("OK").click();
		// TBD: handle the native file dialog
	}

}