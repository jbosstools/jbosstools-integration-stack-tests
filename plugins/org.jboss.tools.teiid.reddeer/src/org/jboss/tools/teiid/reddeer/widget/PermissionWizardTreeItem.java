package org.jboss.tools.teiid.reddeer.widget;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class PermissionWizardTreeItem implements TreeItem {

	private static final int CHECK_PIXEL_X = 4;
	private static final int CHECK_PIXEL_Y = 9;
	private static final int CHECK_PIXEL_VALUE_TRUE = 0x00;
	private static final int CHECK_PIXEL_VALUE_PARTIAL = 0x818181;
	private static final int CHECK_PIXEL_VALUE_FALSE = 0x8cbdef;
	private static final int CHECK_PIXEL_VALUE_UNAVAILABLE = 0xdedddd;

	private TreeItem delegate;

	public PermissionWizardTreeItem(String... path) {
		delegate = new DefaultTreeItem(path);
	}

	public PermissionWizardTreeItem(TreeItem delegate) {
		this.delegate = delegate;
	}

	public boolean isEnabled() {
		return delegate.isEnabled();
	}

	public void select() {
		delegate.select();
	}

	public String getText() {
		return delegate.getText();
	}

	public String getToolTipText() {
		return delegate.getToolTipText();
	}

	public String getCell(int index) {
		return delegate.getCell(index);
	}

	public String[] getPath() {
		return delegate.getPath();
	}

	public void collapse() {
		delegate.collapse();
	}

	public void doubleClick() {
		delegate.doubleClick();
	}

	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	public void expand() {
		delegate.expand();
	}

	public void expand(TimePeriod timePeriod) {
		delegate.expand(timePeriod);
	}

	public TreeItem getItem(String text) {
		return delegate.getItem(text);
	}

	public boolean isSelected() {
		return delegate.isSelected();
	}

	public boolean isDisposed() {
		return delegate.isDisposed();
	}

	public void setChecked(boolean check) {
		delegate.setChecked(check);
	}

	public boolean isChecked() {
		return delegate.isChecked();
	}

	public org.eclipse.swt.widgets.TreeItem getSWTWidget() {
		return delegate.getSWTWidget();
	}

	public List<TreeItem> getItems() {
		return delegate.getItems();
	}

	public Tree getParent() {
		return delegate.getParent();
	}

	public boolean isExpanded() {
		return delegate.isExpanded();
	}

	public void expand(int minItemsCount) {
		delegate.expand(minItemsCount);
	}

	public void expand(int minItemsCount, TimePeriod timePeriod) {
		delegate.expand(minItemsCount, timePeriod);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	public void setText(String text, int index) {
		delegate.setText(text, index);
	}

	public void setText(String text) {
		delegate.setText(text);
	}

	public boolean getChecked(final int column) {
		/*
		 * Designer does not use standard checkboxes in this dialog, but changes boolean values deep in the content
		 * provider and then sets an image on the TreeItem accordingly.
		 * 
		 * Here we get the image and check a particular pixel (which was determined by diffing the two images) and
		 * decide whether the "checkbox" is checked or not.
		 * 
		 * The alternative to this is about 200 lines of reflection code that was even more fragile.
		 */

		final org.eclipse.swt.widgets.TreeItem swtWidget = getSWTWidget();

		ImageData data = Display.syncExec(new ResultRunnable<ImageData>() {

			@Override
			public ImageData run() {
				return swtWidget.getImage(column).getImageData();
			}
		});

		int checkPixel = data.getPixel(CHECK_PIXEL_X, CHECK_PIXEL_Y);

		switch (checkPixel) {
		case CHECK_PIXEL_VALUE_PARTIAL:
		case CHECK_PIXEL_VALUE_TRUE:
			return true;
		case CHECK_PIXEL_VALUE_UNAVAILABLE:
		case CHECK_PIXEL_VALUE_FALSE:
			return false;
		default:
			throw new RuntimeException("unknown check state (designer changed checkbox images?)");
		}
	}

	public void setChecked(boolean check, final int column) {
		/*
		 * Designer does not use standard checkboxes in this dialog, we need to find the location of the cell for the
		 * particular permission type and click into it
		 */

		final org.eclipse.swt.widgets.TreeItem swtWidget = delegate.getSWTWidget();

		Rectangle bounds = Display.syncExec(new ResultRunnable<Rectangle>() {

			@Override
			public Rectangle run() {
				return swtWidget.getBounds(column);
			}

		});

		// only click if the state is different
		if (getChecked(column) != check) {
			WidgetHandler.getInstance().notifyItemMouse(SWT.MouseDown, 0, getParent().getSWTWidget(), null,
					bounds.x + 1, bounds.y + 1, 1);
		}
	}

	@Override
	public TreeItem getItem(String... path) {
		return delegate.getItem(path);
	}

}
