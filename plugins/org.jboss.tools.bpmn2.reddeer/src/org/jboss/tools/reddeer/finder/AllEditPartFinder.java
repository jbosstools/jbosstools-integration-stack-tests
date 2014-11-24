package org.jboss.tools.reddeer.finder;

import java.util.List;

import org.eclipse.gef.EditPart;

public class AllEditPartFinder extends Finder<EditPart> {

	@SuppressWarnings("unchecked")
	@Override
	public List<EditPart> getChildren(EditPart child) {
		return (List<EditPart>) child.getChildren();
	}

}
