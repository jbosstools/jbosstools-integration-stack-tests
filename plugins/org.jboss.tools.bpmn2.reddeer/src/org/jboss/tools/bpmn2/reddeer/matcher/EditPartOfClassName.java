package org.jboss.tools.bpmn2.reddeer.matcher;

import org.eclipse.gef.EditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class EditPartOfClassName extends BaseMatcher<EditPart> {

	private String className;

	public EditPartOfClassName(String className) {
		this.className = className;
	}

	@Override
	public boolean matches(Object item) {
		return item.getClass().getSimpleName().equals(className);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("of class '").appendText(className).appendText("'");

	}

}
