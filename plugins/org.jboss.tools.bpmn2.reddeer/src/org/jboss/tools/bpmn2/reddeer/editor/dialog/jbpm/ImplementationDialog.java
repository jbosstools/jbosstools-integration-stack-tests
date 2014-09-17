package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * 
 */
public class ImplementationDialog {

	/**
	 * 
	 * @param implementationUri
	 */
	public void add(String implementationUri) {
		new SWTBot().shell("Create New Service Implementation").activate();
		new DefaultText(0).setText(implementationUri);
		new PushButton("OK").click();
	}
	
}
