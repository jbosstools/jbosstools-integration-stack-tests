package org.jboss.tools.fuse.reddeer.editor;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.reddeer.gef.impl.editpart.AbstractEditPart;
import org.jboss.reddeer.gef.matcher.IsEditPartWithLabel;

/**
 * EditPart inside the CamelEditor implementation which is looking for a given label inside the edit part.
 * 
 * @author tsedmik
 */
public class CamelComponentEditPart extends AbstractEditPart {

	public CamelComponentEditPart(String label) {
		this(label, 0);
	}

	public CamelComponentEditPart(String label, int index) {
		super(new IsEditPartWithLabel(label), index);
	}

	public Rectangle getBounds() {
		Rectangle bounds = getFigure().getBounds();
		final Rectangle rec = bounds.getCopy();
		getFigure().translateToAbsolute(rec);
		return rec;
	}
}
