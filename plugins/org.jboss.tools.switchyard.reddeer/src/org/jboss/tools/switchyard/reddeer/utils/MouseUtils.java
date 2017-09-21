package org.jboss.tools.switchyard.reddeer.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.reddeer.common.util.Display;

/**
 * Class which provides various mouse operations.
 * 
 * @author apodhrad
 * 
 */
public class MouseUtils {

	public static void click(final int x, final int y) {
		mouseMove(x, y);
		mouseDown(x, y, 1);
		mouseUp(x, y, 1);
	}

	public static void doubleClick(final int x, final int y) {
		click(x, y);
		click(x, y);
	}

	public static void mouseMove(final int x, final int y) {
		Display.asyncExec(new Runnable() {
			public void run() {
				Event event = new Event();
				event.x = x;
				event.y = y;
				event.type = SWT.MouseMove;
				Display.getDisplay().post(event);
			}
		});
	}

	public static void mouseDown(final int x, final int y, final int button) {
		Display.asyncExec(new Runnable() {
			public void run() {
				Event event = new Event();
				event.x = x;
				event.y = y;
				event.button = button;
				event.type = SWT.MouseDown;
				Display.getDisplay().post(event);
			}
		});
	}

	public static void mouseUp(final int x, final int y, final int button) {
		Display.asyncExec(new Runnable() {
			public void run() {
				Event event = new Event();
				event.x = x;
				event.y = y;
				event.button = button;
				event.type = SWT.MouseUp;
				Display.getDisplay().post(event);
			}
		});
	}

}
