package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;

/**
 * 
 * @author apodhrad
 *
 */
public class SwitchYardEditorIsOpen implements WaitCondition {

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
