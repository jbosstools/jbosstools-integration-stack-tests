package org.jboss.tools.esb.reddeer.widget;

import org.jboss.reddeer.swt.impl.combo.AbstractCombo;

public class LabeledComboExt extends AbstractCombo {

	public LabeledComboExt(String label) {
		super(null, 0, new WithLabelMatcherExt(label));
	}

}
