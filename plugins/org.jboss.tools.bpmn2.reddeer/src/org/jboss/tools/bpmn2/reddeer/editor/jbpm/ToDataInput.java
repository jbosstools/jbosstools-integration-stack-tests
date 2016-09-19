package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.tools.bpmn2.reddeer.editor.MappingSide;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.EditNameDialog;

/**
 * Represents the target side of parameter mapping.
 */
public class ToDataInput implements MappingSide {

	private String name;
	private String dataType;
	private boolean isNameReserved;

	/**
	 * @deprecated
	 * @param name
	 */
	public ToDataInput(String name) {
		this(name, "");
	}

	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public ToDataInput(String name, String dataType) {
		this(name, dataType, false);
	}
	
	/**
	 * 
	 * @param name - name of input
	 * @param dataType - data type of input
	 * @param isNameReserved - is the 'name' reserved
	 */
	public ToDataInput(String name, String dataType, boolean isNameReserved) {
		this.name = name;
		this.dataType = dataType;
		this.isNameReserved = isNameReserved;
	}

	@Override
	public void setUp() {
		new PushButton(new DefaultGroup("To"), 0).click();
		new EditNameDialog(isNameReserved).setName(name);

		if(!isNameReserved) {
			Combo dataTypeCombo = new LabeledCombo(new DefaultGroup("To"), "Data Type");
			if (!dataTypeCombo.getItems().contains(dataType)) {
				new PushButton(0).click();
				new DataTypeDialog().add(dataType);
			}
			dataTypeCombo.setSelection(dataType);
		}
	}

	@Override
	public String getName() {
		return name;
	}

}
