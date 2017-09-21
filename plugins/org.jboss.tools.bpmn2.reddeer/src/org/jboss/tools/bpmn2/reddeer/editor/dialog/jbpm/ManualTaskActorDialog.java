package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Dialog serves for actor addition into Manual task
 * @author jomarko
 *
 */
public class ManualTaskActorDialog {

	public void addActor(String actorName) {
		new DefaultShell("Edit Source for Input Parameter \"Actor Id\"");
		new LabeledText("Script").setText(actorName);
		new PushButton("OK").click();
	}
}
