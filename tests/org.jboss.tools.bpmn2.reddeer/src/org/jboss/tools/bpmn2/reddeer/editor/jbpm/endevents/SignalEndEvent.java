package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class SignalEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public SignalEndEvent(String name) {
		super(name, ConstructType.SIGNAL_END_EVENT);
	}

}
