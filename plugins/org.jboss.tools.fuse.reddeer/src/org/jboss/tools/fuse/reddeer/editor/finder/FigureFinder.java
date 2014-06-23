package org.jboss.tools.fuse.reddeer.editor.finder;

import java.util.List;

import org.eclipse.draw2d.IFigure;

/**
 * 
 * @author apodhrad
 * 
 */
public class FigureFinder extends Finder<IFigure> {

	@SuppressWarnings("unchecked")
	@Override
	public List<IFigure> getChildren(IFigure child) {
		return child.getChildren();
	}

}
