package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;

/**
 * 
 */
public class Escalation {

	private static final String SHELL_LABEL = "Create New Escalation";
    private String name;
	private String code;

	/**
	 * 
	 * @param name
	 * @param code
	 */
    public Escalation(String name, String code) {
    	this.name = name;
    	this.code = code;
    	
    	// default value
    	if (this.name == null || this.name.isEmpty()) {
    		this.name = "Escalation Code: " + code;
    	}
    }

    /**
     * 
     * @return
     */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Perform user actions which are required to set up this object
	 * in the UI.
	 */
	public void setUpViaProcessDefinitions() {
		new LabeledText(new DefaultSection("Escalation Details"), "Name").setText(name);
		new LabeledText("Escalation Code").setText(code);
	}
	
	public void setUpViaDialog() {
		new DefaultShell(SHELL_LABEL);
		new DefaultTabItem("General").activate();
		new LabeledText("Name").setText(name);
		new DefaultTabItem("Escalation").activate();
		new LabeledText("Escalation Code").setText(code);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(SHELL_LABEL));
	}
}
