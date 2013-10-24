package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpmn2.reddeer.editor.AbstractTask;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.IParameterMapping;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ServiceTask extends AbstractTask {

	/**
	 * 
	 * @param name
	 */
	public ServiceTask(String name) {
		super(name, ConstructType.SERVICE_TASK);
	}
	
	public void setOperation(String name) {
		properties.selectTab("Service Task");
		
		new PushButton(0).click();
		SWTBot viewBot = bot.shell("Create New Operation").bot();
		viewBot.textWithLabel("Name").setText(name);
		viewBot.button("OK").click();
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.reddeer.editor.IParameterMapping)
	 */
	@Override
	public void addParameterMapping(IParameterMapping parameter) {
		super.addParameterMapping(parameter);
	}

}
