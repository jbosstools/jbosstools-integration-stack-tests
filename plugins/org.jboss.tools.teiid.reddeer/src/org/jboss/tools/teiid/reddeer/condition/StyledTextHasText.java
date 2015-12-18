package org.jboss.tools.teiid.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.api.StyledText;

public class StyledTextHasText extends AbstractWaitCondition {

	private final StyledText widget;

	public StyledTextHasText(StyledText widget) {
		this.widget = widget;
	}

	@Override
	public boolean test() {
		return (widget.getText() != null && !widget.getText().isEmpty());
	}

	@Override
	public String description() {
		return "StyledText has text"; 
	}

}
