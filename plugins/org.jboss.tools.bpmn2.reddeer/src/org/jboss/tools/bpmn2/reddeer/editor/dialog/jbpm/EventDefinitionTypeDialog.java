package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

/**
 * 
 */
public class EventDefinitionTypeDialog {

	/**
	 * 
	 * @param eventDefinitionName
	 */
	public void add(String eventDefinitionName) {
		new SWTBot().shell("Select a type of Event Definition").activate();
		new DefaultTable().select(eventDefinitionName);
		new PushButton("OK").click();
	}

}
