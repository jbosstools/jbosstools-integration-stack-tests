package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;

/**
 * 
 */
public class ErrorDialog {

	/**
	 * 
	 * @param name
	 * @param code
	 * @param dataType
	 */
	public void addErrorRef(String name, String code, String dataType) {
		add(new ErrorRef(name, code, dataType));
	}

	/**
	 * 
	 * @param errorRef
	 */
	public void add(ErrorRef errorRef) {
		errorRef.setUpViaDialog();
	}

}
