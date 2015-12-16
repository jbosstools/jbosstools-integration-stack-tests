package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;

/**
 * 
 * @author apodhrad
 *
 */
public class SwitchYardEditorIsOpen extends AbstractWaitCondition {

	@Override
	public boolean test() {
		try {
			new SwitchYardEditor();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public String description() {
		return "SwitchYard editor is still unavailable.";
	}

}
