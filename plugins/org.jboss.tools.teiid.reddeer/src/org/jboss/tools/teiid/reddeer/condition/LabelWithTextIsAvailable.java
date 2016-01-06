package org.jboss.tools.teiid.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;

public class LabelWithTextIsAvailable extends AbstractWaitCondition {

	private String text;

	public LabelWithTextIsAvailable(String text) {
		this.text = text;
	}

	@Override
	public boolean test() {
		try {
			new DefaultLabel(text);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

}
