package org.jboss.tools.switchyard.reddeer.widget;

import org.jboss.reddeer.swt.impl.text.AbstractText;

public class LabeledTextExt extends AbstractText {

	public LabeledTextExt(String label) {
		super(null, 0, new WithLabelMatcherExt(label));
	}

}
