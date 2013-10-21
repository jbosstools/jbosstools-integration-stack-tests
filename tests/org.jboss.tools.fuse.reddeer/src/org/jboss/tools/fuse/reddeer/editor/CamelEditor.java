package org.jboss.tools.fuse.reddeer.editor;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.lookup.WidgetLookup;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.tools.fuse.reddeer.component.CamelComponent;
import org.jboss.tools.fuse.reddeer.editor.matcher.All;

/**
 * 
 * @author apodhrad
 * 
 */
public class CamelEditor extends GefEditor {

	public static final int MAX = 10;

	protected GraphicalViewer viewer;

	public CamelEditor(String title) {
		super(title);
	}

	public void addCamelComponent(CamelComponent component) {
		addCamelComponent(component, 1);
	}

	protected void addCamelComponent(CamelComponent component, int count) {
		if (count > MAX) {
			throw new RuntimeException("Cannot add component '" + component.getPaletteEntry() + "'");
		}
		List<EditPart> before = getEditParts(new All());
		getPalette().activateTool(component.getPaletteEntry());
		AbstractWait.sleep(2000);
		WidgetHandler.getInstance().setFocus(getFigureCanvas());
		click(0, 0);
		WidgetLookup.getInstance().sendClickNotifications(getFigureCanvas());
		AbstractWait.sleep(2000);
		List<EditPart> after = getEditParts(new All());
		if (after.size() <= before.size()) {
			addCamelComponent(component, count + 1);
		}
	}

	public void deleteCamelComponent(CamelComponent component) {
		deleteEditPartWithLabel(component.getLabel());
	}
}
