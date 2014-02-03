package org.jboss.tools.bpmn2.reddeer.view;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ExportDiagramView {

	public enum ImageFormat {
		BMP, GIF, JPG, PNG, RLE;
	}
	
	
	BPMN2Editor editor;
	
	/**
	 * 
	 */
	public ExportDiagramView() {
		this(new BPMN2Editor());
	}
	
	/**
	 * 
	 * @param editor
	 */
	public ExportDiagramView(BPMN2Editor editor) {
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