package org.jboss.tools.reddeer.finder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.hamcrest.Matcher;

/**
 * 
 */
public abstract class Finder<T> {

	/**
	 * 
	 * @param parent
	 * @param matcher
	 * @return
	 */
	public List<T> find(T parent, Matcher<?> matcher) {
		List<T> list = new ArrayList<T>();
		Stack<T> stack = new Stack<T>();
		// Initial push
		stack.push(parent);
		// Depth first search
		while (!stack.isEmpty()) {
			// Pop figure
			T child = stack.pop();
			// If null then continue
			if (child == null) {
				continue;
			}
			// Does it matches?
			if (matcher.matches(child)) {
				list.add(child);
			}
			// Push another children
			for (T t : getChildren(child)) {
				stack.push(t);
			}
		}
		return list;
	}

	/**
	 * 
	 * @param child
	 * @return
	 */
	public abstract List<T> getChildren(T child);
}
