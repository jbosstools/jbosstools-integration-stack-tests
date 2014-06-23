package org.jboss.tools.switchyard.reddeer.condition;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class EditPartAppeared implements WaitCondition {

	private Matcher<EditPart> matcher;

	public EditPartAppeared(Matcher<EditPart> matcher) {
		this.matcher = matcher;
	}

	@Override
	public boolean test() {
		List<SWTBotGefEditPart> list = new SwitchYardEditor().editParts(matcher);
		return !list.isEmpty();
	}

	@Override
	public String description() {
		return "Edit part with matcher '" + matcher + "' didn't appear!";
	}
}
