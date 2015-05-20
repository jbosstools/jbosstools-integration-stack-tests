package org.jboss.tools.switchyard.reddeer.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;

/**
 * 
 * @author apodhrad
 * 
 */
public class ControlUtils {

	private List<Control> allWidgets;

	public static Rectangle getBounds(final Control control) {
		return Display.syncExec(new ResultRunnable<Rectangle>() {

			@Override
			public Rectangle run() {
				return control.getBounds();
			}
		});
	}

	public static Rectangle getAbsoluteBounds(final Control control) {
		Point point = getAbsolutePoint(control);
		Rectangle rec = getBounds(control);
		return new Rectangle(rec.x + point.x, rec.y + point.y, rec.width, rec.height);
	}

	public static Point getAbsolutePoint(final Control control) {
		return Display.syncExec(new ResultRunnable<Point>() {

			@Override
			public Point run() {
				return control.toDisplay(0, 0);
			}
		});
	}

	public static Point getCentralPoint(final Control control) {
		return getCentralPoint(getBounds(control));
	}

	public static Point getAbsoluteCentralPoint(final Control control) {
		return getCentralPoint(getAbsoluteBounds(control));
	}

	private static Point getCentralPoint(Rectangle rec) {
		return new Point(rec.x + rec.width / 2, rec.y + rec.height / 2);
	}

	public List<Control> findAllWidgets(final Control parent) {
		allWidgets = new ArrayList<Control>();
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				findWidgets(parent);
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
