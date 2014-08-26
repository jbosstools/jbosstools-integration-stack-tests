package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.reddeer.DefaultSection;

public class Signal {
	private String name;
	private String dataType;
	
	
	public Signal(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
	}
	
	
	public String getName() {
		return name;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	/**
	 * Perform user actions which are required to set up this object
	 * in the UI.
	 */
	public void setUp() {
		new LabeledText(new DefaultSection("Signal Details"), "Name").setText(name);
		Combo dataTypeCombo = new LabeledCombo("Data Type");
		if (!dataTypeCombo.getItems().contains(dataType)) {
			throw new IllegalArgumentException("Unsuported data type: " + dataType + ". There was no such data type in combo box." );
		}
		dataTypeCombo.setSelection(dataType);
	}
	

}
