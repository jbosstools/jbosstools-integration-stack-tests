package org.jboss.tools.teiid.reddeer.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.eclipse.reddeer.swt.impl.clabel.DefaultCLabel;

public class StatusLineMatcher extends BaseMatcher<DefaultCLabel> {

	@Override
	public boolean matches(Object widget) {
		return "org.eclipse.jface.action.StatusLine".equals(widget.getClass().getName());
	}

	@Override
	public void describeTo(Description desc) {
		desc.appendText(" is Status Line");
	}
}
