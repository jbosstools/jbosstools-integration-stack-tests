package org.jboss.tools.bpmn2.reddeer.editor.matcher;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.tools.bpmn2.reddeer.GEFProcessEditor;

/**
 * Returns an edit part which is on a given location (given by X, Y) in the editor.
 */
public class ConstructOnPoint<T extends EditPart> extends BaseMatcher<EditPart> {

	private Point p;

	/**
	 * 
	 * @param p
	 */
	public ConstructOnPoint(Point p) {
		this.p = p;
	}

	@Override
	public boolean matches(Object item) {
		return new GEFProcessEditor().getBounds((GraphicalEditPart) item).contains(p);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" checking " + p + " for editPart presence.");
	}

}
