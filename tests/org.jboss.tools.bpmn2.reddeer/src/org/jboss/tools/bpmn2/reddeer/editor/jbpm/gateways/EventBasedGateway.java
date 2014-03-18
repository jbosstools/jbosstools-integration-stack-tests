package org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.bpmn2.reddeer.editor.AbstractGateway;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;


/**
 * ISSUES: 
 * 	1) Description is mixed with the pictogram.
 *  2) When adding a new condition 
 *  	+ select flow
 *      + click pencil
 *      + click add condition
 *      - condition expression is not visible
 *  
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class EventBasedGateway extends AbstractGateway {
	
	public enum Type {
		EXCLUSIVE, PARALLEL
	}
	
	/**
	 * 
	 * @param name
	 */
	public EventBasedGateway(String name) {
		super(name, ConstructType.EVENT_BASED_GATEWAY);
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setType(Type type) {
		String visibleText = type.name().charAt(0) + 
				type.name().substring(1).toLowerCase();
		
		properties.selectTab("Gateway");
		new LabeledCombo("Event Gateway Type").setSelection(visibleText);
	}

	/**
	 * 
	 */
	public void setInstantiate(boolean b) {
		properties.selectTab("Gateway");
		properties.selectCheckBox(bot.checkBox(), b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.AbstractGateway#setDirection(org.jboss.tools.bpmn2.reddeer.editor.AbstractGateway.Direction)
	 */
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}
	
	/**
	 * @see org.jboss.tools.bpmn2.reddeer.editor.constructs.AbstractGateway#setCondition(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setCondition(String branch, String lang, String condition) {
		super.setCondition(branch, lang, condition);
	}
	
}
