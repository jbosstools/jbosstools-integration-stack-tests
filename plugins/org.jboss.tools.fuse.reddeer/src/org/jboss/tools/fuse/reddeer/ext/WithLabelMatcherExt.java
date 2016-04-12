package org.jboss.tools.fuse.reddeer.ext;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.Is;

public class WithLabelMatcherExt extends BaseMatcher<String> {

	protected Matcher<String> matcher;

	public WithLabelMatcherExt(String label) {
		matcher = Is.<String> is(label);
	}

	@Override
	public boolean matches(Object item) {
		if ((item instanceof List) || (item instanceof Text) || (item instanceof Button) || (item instanceof Combo)
				|| (item instanceof CCombo) || (item instanceof Spinner)) {
			String widgetLabel = WidgetHandlerExt.getInstance().getLabel((Widget) item);
			String widgetLabelExt = widgetLabel.replace("*", "").trim();
			if (widgetLabel != null && (matcher.matches(widgetLabel) || matcher.matches(widgetLabelExt))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description desc) {
	}

}
