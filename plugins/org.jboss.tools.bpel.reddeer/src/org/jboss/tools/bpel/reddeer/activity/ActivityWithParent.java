package org.jboss.tools.bpel.reddeer.activity;

import org.eclipse.gef.EditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * 
 * @author Andrej Podhradsky (apodhrad@redhat.com)
 *
 */
public class ActivityWithParent extends BaseMatcher<EditPart> {

	private Activity parentActivity;

	public ActivityWithParent(Activity parentActivity) {
		this.parentActivity = parentActivity;
	}

	@Override
	public boolean matches(Object item) {
		if (item instanceof EditPart) {
			EditPart editPart = (EditPart) item;
			EditPart parent = editPart.getParent();
			while (parent != null) {
				if (parentActivity.getEditPart().equals(parent)) {
					return true;
				}
				parent = parent.getParent();
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("activity with parent '" + parentActivity + "'");
	}

}