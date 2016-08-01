package org.jboss.tools.common.reddeer.widget;

import org.jboss.reddeer.swt.impl.combo.AbstractCombo;

/**
 * 
 * @author apodhrad
 *
 */
public class LabeledComboExt extends AbstractCombo {

	public LabeledComboExt(String label) {
		this(label, true);
	}
	
	public LabeledComboExt(String label, boolean ignoreAsterisk) {
		super(null, 0, new WithLabelMatcherExt(label, ignoreAsterisk));
	}

}
