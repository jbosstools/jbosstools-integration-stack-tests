package org.jboss.tools.switchyard.reddeer.widget;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.hamcrest.Matcher;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.core.reference.ReferencedComposite;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.swt.widgets.AbstractWidget;

/**
 * 
 * @author apodhrad
 *
 */
public class DefaultCCombo extends AbstractWidget<org.eclipse.swt.custom.CCombo> {

	private static final Logger log = Logger.getLogger(DefaultCCombo.class);

	public DefaultCCombo(Matcher<?>... matchers) {
		this(null, 0, matchers);
	}

	public DefaultCCombo(int index, Matcher<?>... matchers) {
		this(null, index, matchers);
	}
	
	public DefaultCCombo(ReferencedComposite refComposite, Matcher<?>... matchers) {
		super(org.eclipse.swt.custom.CCombo.class, refComposite, 0, matchers);
	}
	
	public DefaultCCombo(ReferencedComposite refComposite, int index, Matcher<?>... matchers) {
		super(org.eclipse.swt.custom.CCombo.class, refComposite, index, matchers);
	}

	public void setSelection(final String text) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				String[] items = swtWidget.getItems();
				int index = Arrays.asList(items).indexOf(text);
				if (index == -1) {
					log.error("'" + text + "' is not " + "contained in combo items");
					log.info("Items present in combo:");
					int i = 0;
					for (String item : items) {
						log.info("    " + item + "(index " + i);
						i++;
					}
					throw new SWTLayerException("Nonexisting item in combo was requested");
				} else {
					swtWidget.select(index);
				}
			}
		});

		notifyCCombo(createEventForCCombo(SWT.Selection));
	}

	/**
	 * Creates event for CCombo with specified type
	 * 
	 * @param type
	 * @return
	 */
	private Event createEventForCCombo(int type) {
		Event event = new Event();
		event.type = type;
		event.display = Display.getDisplay();
		event.time = (int) System.currentTimeMillis();
		event.widget = swtWidget;
		return event;
	}

	/**
	 * Notifies Combo listeners about event event.type field has to be properly set
	 * 
	 * @param event
	 */
	private void notifyCCombo(final Event event) {
		Display.syncExec(new Runnable() {
			public void run() {
				swtWidget.notifyListeners(event.type, event);
			}
		});
	}
}
