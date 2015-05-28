package org.jboss.tools.fuse.reddeer.widget;

import org.jboss.reddeer.swt.impl.text.AbstractText;

/**
 * 
 * @author apodhrad
 *
 */
public class CLabeledText extends AbstractText {

	public CLabeledText(String label) {
		super(null, 0, new WithCLabelMatcher(label));
	}


}
