package org.jboss.tools.fuse.reddeer.widget;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.jboss.reddeer.core.resolver.WidgetResolver;

/**
 * 
 * @author apodhrad
 *
 */
public class WithCLabelMatcher extends BaseMatcher<String> {

	protected Matcher<String> matcher;

	public WithCLabelMatcher(String label) {
		matcher = Is.<String> is(label);
	}

	@Override
	public boolean matches(Object obj) {
		if (!(obj instanceof Text || obj instanceof Combo)) {
			return false;
		}

		Control control = (Control) obj;
		Control parent = control.getParent();

		java.util.List<Widget> children = WidgetResolver.getInstance().getChildren(parent);
		for (Widget w : children) {
			if (w instanceof CLabel || w instanceof CLabel) {
				CLabel cLabel = (CLabel) w;
				Object layoutData = cLabel.getLayoutData();
				if (layoutData instanceof FormData) {
					FormData formData = (FormData) layoutData;
					if (control.equals(formData.left.control) || control.equals(formData.right.control)) {
						if (matcher.matches(cLabel.getText())) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("with label ").appendDescriptionOf(matcher);
	}

	@Override
	public String toString() {
		return "Matcher matching widget with label:\n" + matcher.toString();
	}

}