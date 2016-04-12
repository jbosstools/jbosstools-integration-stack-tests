package org.jboss.tools.fuse.reddeer.editor;

import java.util.List;

import org.hamcrest.Matcher;
import org.jboss.reddeer.gef.handler.ViewerHandler;
import org.jboss.reddeer.gef.lookup.ViewerLookup;
import org.jboss.reddeer.graphiti.impl.graphitieditpart.AbstractGraphitiEditPart;

/**
 * Abstract class for EditPart implementation in Camel Editor 
 * 
 * @author tsedmik
 */
public abstract class FuseEditPart extends AbstractGraphitiEditPart {

	protected org.eclipse.gef.GraphicalViewer viewer;
	protected org.eclipse.gef.EditPart editPart;

	/**
	 * Constructs edit part which fulfills a given matcher at the specified index.
	 * 
	 * @param editPart
	 */
	public FuseEditPart(Matcher<org.eclipse.gef.EditPart> matcher, int index) {
		super(getRightEditPart(matcher, index));
	}

	private static org.eclipse.gef.EditPart getRightEditPart(Matcher<org.eclipse.gef.EditPart> matcher, int index) {
		List<org.eclipse.gef.EditPart> editParts = ViewerHandler.getInstance().getEditParts(ViewerLookup.getInstance().findGraphicalViewer(), matcher);
		org.eclipse.gef.EditPart deepestEditPart = null;
		int depthMax = 0;
		for (org.eclipse.gef.EditPart editPart : editParts) {
			int depth = 0;
			org.eclipse.gef.EditPart parent = editPart.getParent();
			while (parent != null) {
				depth++;
				parent = parent.getParent();
			}
			if (depthMax < depth) {
				depthMax = depth;
				deepestEditPart = editPart;
			}
		}
		return deepestEditPart;
	}
}
