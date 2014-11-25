package org.jboss.tools.bpmn2.reddeer;

import org.hamcrest.Matcher;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.gef.impl.editpart.AbstractEditPart;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;

/**
 * Represents EditPart-s with absolute coordinates to top-left corner
 * 
 * @author jomarko
 *
 */
public class AbsoluteEditPart extends AbstractEditPart {	
	
	public AbsoluteEditPart(EditPart editPart) {
		super(editPart);
	}
	
	public AbsoluteEditPart(Matcher<org.eclipse.gef.EditPart> matcher) {
		this(matcher, 0);
	}
	
	public AbsoluteEditPart(Matcher<org.eclipse.gef.EditPart> matcher, int index) {
		super(matcher, index);
	}
	
	@Override
	public void click() {
		Rectangle bounds = getFigure().getBounds();
		final Rectangle rec = bounds.getCopy();
		getFigure().translateToAbsolute(rec);
		int x = rec.x + rec.width / 2;
		int y = rec.y + rec.height / 2;
		new GEFEditor().click(x, y);
	}
	
	public Rectangle getBounds() {
		IFigure figure = super.getFigure();
		final Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds);
		return bounds;
	}
	
	public EditPart getEditPart() {
		return editPart;
	}

}
