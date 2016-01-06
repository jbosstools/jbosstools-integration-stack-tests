package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.jboss.reddeer.swt.api.Combo;
import org.eclipse.swt.widgets.ToolBar;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.core.lookup.ToolBarLookup;
import org.jboss.reddeer.core.lookup.ToolItemLookup;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ErrorDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.EventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

/**
 * 
 */
public class ErrorEventDefinition extends EventDefinition {

	private ErrorRef errorRef;
	private String variableForMaping;
	private String comboLabel;

	/**
	 * 
	 * @param errorRef
	 */
	public ErrorEventDefinition(ErrorRef errorRef, String variableForMaping, String comboLabel) {
		this.errorRef = errorRef;
		this.variableForMaping = variableForMaping;
		this.comboLabel = comboLabel;
	}

	@Override
	public void setUp() {

		Combo mappingCombo = new LabeledCombo(comboLabel);
		if (!mappingCombo.getItems().contains(variableForMaping)) {
			throw new IllegalArgumentException(variableForMaping + " isn't in list of defined errors");
		}
		mappingCombo.setSelection(variableForMaping);

		Combo errorCombo = new LabeledCombo("Error");
		String comboItem = errorRef.getName() + "(" + errorRef.getDataType() + ")";
		if (!errorCombo.getItems().contains(comboItem)) {
			new PushButton(0).click();
			new ErrorDialog().add(errorRef);
		} else {
			errorCombo.setSelection(comboItem);
		}

		// new SectionToolItem("Error Event Definition Details", "Close").click();
	}

}
