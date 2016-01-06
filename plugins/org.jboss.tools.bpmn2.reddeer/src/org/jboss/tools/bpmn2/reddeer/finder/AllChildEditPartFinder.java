package org.jboss.tools.bpmn2.reddeer.finder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.gef.EditPart;
import org.hamcrest.Matcher;

/**
 * Serves for finding EditPart's by given Matcher Method find doesn't include argument parent in output, even if parent
 * matches given Matcher
 * 
 * @author jomarko
 *
 */
public class AllChildEditPartFinder extends Finder<EditPart> {

	@Override
	public List<EditPart> find(EditPart parent, Matcher<?> matcher) {
		List<EditPart> list = new ArrayList<EditPart>();
		Stack<EditPart> stack = new Stack<EditPart>();
		// Initial push
		stack.push(parent);
		// Depth first search
		while (!stack.isEmpty()) {
			// Pop figure
			EditPart child = stack.pop();
			// If null then continue
			if (child == null) {
				continue;
			}
			// Does it matches?
			if (parent != child && matcher.matches(child)) {
				list.add(child);
			}
			// Push another children
			for (EditPart t : getChildren(child)) {
				stack.push(t);
			}
		}
		return list;
	}

	@Override
	public List<EditPart> getChildren(EditPart child) {
		return (List<EditPart>) child.getChildren();
	}

}
