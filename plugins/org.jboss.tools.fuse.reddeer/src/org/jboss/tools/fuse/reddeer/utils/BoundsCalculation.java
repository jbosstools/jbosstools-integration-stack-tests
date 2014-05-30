package org.jboss.tools.fuse.reddeer.utils;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;

public class BoundsCalculation {

	public static Rectangle getBounds(final IFigure figure) {
		return Display.syncExec(new ResultRunnable<Rectangle>() {
			@Override
			public Rectangle run() {
				org.eclipse.draw2d.geometry.Rectangle rec = figure.getBounds().getCopy();
				figure.translateToAbsolute(rec);
				return new Rectangle(rec.x, rec.y, rec.width, rec.height);
			}
		});
	}

	public static Rectangle getAbsoluteBounds(final Control control, final IFigure figure) {
		Rectangle cRec = getAbsoluteBounds(control);
		Rectangle fRec = getBounds(figure);
		return new Rectangle(cRec.x + fRec.x, cRec.y + fRec.y, fRec.width, fRec.height);
	}

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

	public static Point getCentralPoint(Rectangle rec) {
		return new Point(rec.x + rec.width / 2, rec.y + rec.height / 2);
	}
}
