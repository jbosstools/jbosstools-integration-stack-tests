package org.jboss.tools.bpmn2.reddeer.finder;

import java.util.List;

import org.eclipse.gef.EditPart;

/**
 * Serves for finding EditPart's by given Matcher Method find includes argument parent in output, if parent matches
 * given Matcher
 * 
 * @author jomarko
 *
 */
public class AllEditPartFinder extends Finder<EditPart> {

	@SuppressWarnings("unchecked")
	@Override
	public List<EditPart> getChildren(EditPart child) {
		List<EditPart> result = (List<EditPart>) child.getChildren();
		return result;
	}

}
