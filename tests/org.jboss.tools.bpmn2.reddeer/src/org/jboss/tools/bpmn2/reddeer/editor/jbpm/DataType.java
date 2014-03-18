package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class DataType implements org.jboss.tools.bpmn2.reddeer.editor.DataType {

	private String name;
	
	/**
	 * 
	 * @param name
	 */
	public DataType(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 */
	public void add() {
		if (!selectType(name)) {
			new PushButton(0).click();
			new SWTBot().shell("Create New Data Type").activate();
			new LabeledText("Structure").setText(name);
			new PushButton("OK").click();
		}
		selectType(name);
	}
	
	/**
	 * 
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	private boolean selectType(String name) {
		// BZ-1008637
		try {
			new LabeledCombo("Data Type").setSelection(name);
		} catch (SWTLayerException e1) {
			try {
				new LabeledCombo("Item").setSelection(name);
			} catch (SWTLayerException e2) {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
}
