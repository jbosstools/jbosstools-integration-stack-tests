package org.jboss.tools.teiid.reddeer.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.jboss.reddeer.core.lookup.WidgetLookup;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

public class TeiidEditorTableItem implements TableItem {

	private TableItem delegate;

	public TeiidEditorTableItem(TableItem delegate) {
		this.delegate = delegate;
	}

	public boolean isEnabled() {
		return delegate.isEnabled();
	}

	public String getText() {
		return delegate.getText();
	}

	public boolean isSelected() {
		return delegate.isSelected();
	}

	public void select() {
		delegate.select();
	}

	public void setChecked(boolean check) {
		delegate.setChecked(check);
	}

	public boolean isChecked() {
		return delegate.isChecked();
	}

	public Table getParent() {
		return delegate.getParent();
	}

	public String getText(int cellIndex) {
		return delegate.getText(cellIndex);
	}

	public Image getImage(int imageIndex) {
		return delegate.getImage(imageIndex);
	}

	public boolean isGrayed() {
		return delegate.isGrayed();
	}

	public void doubleClick() {
		delegate.doubleClick();
	}

	public void click() {
		delegate.click();
	}

	public void click(int column) {
		delegate.click(column);
	}

	public void doubleClick(int column) {
		delegate.doubleClick(column);
	}

	public org.eclipse.swt.widgets.TableItem getSWTWidget() {
		return delegate.getSWTWidget();
	}

	public void setText(int column, final String text) {
		click(column);
		final Widget w = WidgetLookup.getInstance().getFocusControl();
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				((Text) w).setText(text);
				KeyboardFactory.getKeyboard().type(SWT.CR); // this is needed, otherwise the text disappears

			}
		});
	}

	@Override
	public boolean isDisposed() {
		return delegate.isDisposed();
	}

}
