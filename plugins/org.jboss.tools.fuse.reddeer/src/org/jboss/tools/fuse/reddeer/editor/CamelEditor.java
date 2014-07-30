package org.jboss.tools.fuse.reddeer.editor;

import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.gef.impl.editpart.LabeledEditPart;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.fuse.reddeer.component.CamelComponent;

/**
 * Manipulates with Camel Editor
 * 
 * @author tsedmik
 */
public class CamelEditor extends GEFEditor {

	public CamelEditor(String title) {
		super(title);
	}

	public void addCamelComponent(CamelComponent component) {

		addToolFromPalette(component.getPaletteEntry(), 0, 0);
	}

	public void deleteCamelComponent(CamelComponent component) {

		new LabeledEditPart(component.getLabel()).select();
		new ContextMenu("Remove").select();
	}
}
