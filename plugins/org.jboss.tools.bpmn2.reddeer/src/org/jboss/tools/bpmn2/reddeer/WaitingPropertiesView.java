package org.jboss.tools.bpmn2.reddeer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.reddeer.eclipse.ui.views.properties.TabbedPropertyList;

public class WaitingPropertiesView extends PropertySheet {

	@Override
	public void selectTab(String label) {
		activate();
		List<String> old = new ArrayList<String>();

		try {
			old = new TabbedPropertyList().getTabs();
		} catch (Exception ex) {
			// probably not rendered yet
		}
		new WaitUntil(new AnotherTabsRendered(old), TimePeriod.DEFAULT, false);
		super.selectTab(label);
	}

	private class AnotherTabsRendered extends AbstractWaitCondition {

		private List<String> old;

		public AnotherTabsRendered(List<String> old) {
			this.old = old;
		}

		@Override
		public boolean test() {
			List<String> actual = new ArrayList<String>();

			try {
				actual = new TabbedPropertyList().getTabs();
			} catch (Exception ex) {
				// probably not rendered yet
			}

			return !actual.equals(old);
		}

		@Override
		public String description() {
			return "Wait for tabs to be rendered";
		}

	}
}
