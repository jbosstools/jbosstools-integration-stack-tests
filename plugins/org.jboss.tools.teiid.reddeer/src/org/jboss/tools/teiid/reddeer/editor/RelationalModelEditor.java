package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.teiid.reddeer.matcher.ModelEditorItemMatcher;

public class RelationalModelEditor extends AbstractModelEditor{
	public static final String PACKAGE_DIAGRAM = "Package Diagram";
	public static final String TRANSFORMATION_DIAGRAM = "Transformation Diagram";
	
	public RelationalModelEditor(String title) {
		super(title);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Opens table editor's tab.
	 * @param fromDiagram - RelationalModelEditor.PACKAGE_DIAGRAM|...
	 */
	public TableEditor openTableEditor(String fromDiagram){
		return new TableEditor(this.getTitle(), fromDiagram);
	}
	
	/**
	 * Opens transformation diagram and returns transformation editor for specified item.
	 * Note: Package Diagram must be opened.
	 * @param type ModelEditorItemMatcher.PROCEDURE|TABLE|...
	 */
	public TransformationEditor openTransformationDiagram(String type, String name){
		SWTBotGefViewer viewer = this.getEditorViewer(PACKAGE_DIAGRAM);
		viewer.select(viewer.editParts(new ModelEditorItemMatcher(type, name)));
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenu("Edit").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		return new TransformationEditor();
	}
	
	/**
	 * Shows Package Diagram.
	 * Note: Transformation Diagram must be opened.
	 */
	public void returnToPackageDiagram(){
		new DefaultToolItem("Show Parent Diagram").click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

}
