package org.jboss.tools.switchyard.reddeer.widget;

import org.eclipse.swt.widgets.Text;
import org.eclipse.reddeer.swt.impl.text.AbstractText;
import org.eclipse.reddeer.core.matcher.ClassMatcher;
import org.eclipse.reddeer.core.reference.ReferencedComposite;

/**
 * 
 * @author apodhrad
 *
 */
public class DefaultTextExt extends AbstractText {
	public DefaultTextExt(ReferencedComposite ref) {
		this(ref, 0);
	}

	public DefaultTextExt(ReferencedComposite ref, int index) {
		super(new ControlReferencedComposite(ref, new ClassMatcher(Text.class), index), 0);
	}
}
