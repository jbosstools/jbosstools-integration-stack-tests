package org.jboss.tools.teiid.reddeer.matcher;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Decides whether item of model editor is Transformation Arrow.
 */
public class TransformationArrowMatcher extends BaseMatcher<EditPart> {
	
	public static TransformationArrowMatcher getInstance(){
		return new TransformationArrowMatcher();
	}

	@Override
	public boolean matches(Object item) {
		if (item instanceof GraphicalEditPart) {
			IFigure figure = ((GraphicalEditPart) item).getFigure();
			Rectangle rectangle = figure.getBounds().getCopy();
			return rectangle.width == 40 && rectangle.height == 60;
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is a transformation edit part");
	}
}
