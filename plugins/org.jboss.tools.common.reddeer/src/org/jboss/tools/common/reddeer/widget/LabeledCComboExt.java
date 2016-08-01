package org.jboss.tools.common.reddeer.widget;

import org.jboss.reddeer.swt.impl.ccombo.AbstractCCombo;

/**
 * 
 * @author apodhrad
 *
 */
public class LabeledCComboExt extends AbstractCCombo {

	public LabeledCComboExt(String label) {
		this(label, true);
	}

	public LabeledCComboExt(String label, boolean ignoreAsterisk) {
		super(null, 0, new WithLabelMatcherExt(label, ignoreAsterisk));
	}

}
