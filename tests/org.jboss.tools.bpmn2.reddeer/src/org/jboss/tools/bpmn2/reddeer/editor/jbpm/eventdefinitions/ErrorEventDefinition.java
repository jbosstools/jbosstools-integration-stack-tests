package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ErrorDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.AbstractEventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class ErrorEventDefinition extends AbstractEventDefinition {

	private ErrorRef errorRef;

	/**
	 * 
	 * @param errorRef
	 */
	public ErrorEventDefinition(ErrorRef errorRef) {
		this.errorRef = errorRef;
	}
	
	@Override
	public void setUp() {
		Combo errorCombo = new LabeledCombo("Error");
		if (!errorCombo.getItems().contains(errorRef.getName())) {
			new PushButton(0).click();
			new ErrorDialog().add(errorRef);
		}
		errorCombo.setSelection(errorRef.getName());

		new DefaultSection("Error Event Definition Details").getToolbarButton("Close").click();
	}
	
}
