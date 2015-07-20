package org.jboss.tools.bpel.reddeer.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.lookup.WidgetLookup;
import org.jboss.reddeer.swt.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.swt.util.Display;

/**
 * 
 * @author apodhrad
 *
 */
public class WithLabelMatcherExt extends BaseMatcher<String> {

	private java.util.List<Control> allWidgets;

	protected Matcher<String> matcher;

	public WithLabelMatcherExt(String label) {
		matcher = new WithMnemonicTextMatcher(label);
	}

	@Override
	public boolean matches(Object obj) {
		if (!(obj instanceof org.eclipse.swt.widgets.Text || obj instanceof org.eclipse.swt.custom.StyledText
				|| obj instanceof org.eclipse.swt.widgets.Combo || obj instanceof org.eclipse.swt.widgets.List
				|| obj instanceof org.eclipse.swt.widgets.Table || obj instanceof org.eclipse.swt.widgets.Tree)) {
			return false;
		}

		findAllWidgets();

		int widgetIndex = allWidgets.indexOf(obj);
		ListIterator<? extends Widget> listIterator = allWidgets.listIterator(widgetIndex);
		while (listIterator.hasPrevious()) {
			Widget previousWidget = listIterator.previous();
			if (isLabel(previousWidget)) {
				String label = WidgetHandler.getInstance().getText(previousWidget);
				if (matcher.matches(label)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description desc) {
	}

	private boolean isLabel(Widget widget) {
		return widget instanceof Label || widget instanceof CLabel;
	}

	private List<Control> findAllWidgets() {
		final Control activeControl = WidgetLookup.getInstance().getActiveWidgetParentControl();
		allWidgets = new ArrayList<Control>();
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				findWidgets(activeControl);
			}
		});
		return allWidgets;
	}

	private void findWidgets(Control control) {
		allWidgets.add(control);
		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			for (Control child : composite.getChildren()) {
				findWidgets(child);
			}
		}
	}
}
