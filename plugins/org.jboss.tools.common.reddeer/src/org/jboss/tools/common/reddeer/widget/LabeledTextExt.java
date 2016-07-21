package org.jboss.tools.common.reddeer.widget;

import org.jboss.reddeer.swt.impl.text.AbstractText;

public class LabeledTextExt extends AbstractText {

	public LabeledTextExt(String label) {
		this(label, true);
	}
	
	public LabeledTextExt(String label, boolean ignoreAsterisk) {
		super(null, 0, new WithLabelMatcherExt(label, ignoreAsterisk));
	}

}
