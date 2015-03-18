package org.jboss.tools.switchyard.reddeer.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.text.AbstractText;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithLabelMatcher;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;

/**
 * 
 * Extended LabeledText by focusOut() method. This method is needed in binding pages so that
 * SWTValueUpdater is called (without this some changed values are not stored).
 * 
 * @author apodhrad
 *
 */
public class LabeledText extends AbstractText implements Text {

	/**
	 * Default text with a label
	 * 
	 * @param label
	 */
	public LabeledText(String label) {
		this(new RegexMatcher(label + "\\**"));
	}

	public LabeledText(Matcher<String> matcher) {
		super(null, 0, new WithLabelMatcher(matcher));
	}

	public void setFocusOut() {
		Display.asyncExec(new Runnable() {

			@Override
			public void run() {
				Listener[] listeners = swtWidget.getListeners(SWT.FocusOut);
				for (Listener listener : listeners) {
					listener.handleEvent(createEvent(swtWidget, SWT.FocusOut));
					listener.handleEvent(createEvent(swtWidget, SWT.FocusOut));
				}
			}
		});
		Display.syncExec(new Runnable() {

			@Override
			public void run() {

			}
		});
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	private Event createEvent(Widget widget, int eventType) {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = widget;
		event.display = Display.getDisplay();
		event.type = eventType;
		return event;
	}
}
