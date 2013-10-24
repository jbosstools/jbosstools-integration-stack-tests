package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class SignalStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public SignalStartEvent(String name) {
		super(name, ConstructType.SIGNAL_START_EVENT);
	}

}