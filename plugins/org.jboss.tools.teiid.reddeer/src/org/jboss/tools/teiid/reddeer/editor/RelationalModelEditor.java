package org.jboss.tools.teiid.reddeer.editor;

import java.awt.event.KeyEvent;
import java.util.List;

import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.spinner.LabeledSpinner;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.matcher.AttributeMatcher;
import org.jboss.tools.teiid.reddeer.matcher.ModelEditorItemMatcher;

public class RelationalModelEditor extends AbstractModelEditor{
	public static final String PACKAGE_DIAGRAM = "Package Diagram";
	public static final String TRANSFORMATION_DIAGRAM = "Transformation Diagram";
	
	public RelationalModelEditor(String title) {
		super(title);
		AbstractWait.sleep(TimePeriod.SHORT);
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
		new WorkbenchShell();
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
	
	/**
	 * Returns attribute's names of specified model item.
	 */
	public List<String> listTableAttributesNames(String tableName){
		AttributeMatcher matcher = new AttributeMatcher("", ModelEditorItemMatcher.TABLE, tableName);
		getEditorViewer(new DefaultCTabItem(0).getText()).editParts(matcher);
		return matcher.getTexts();
	}
	
	/**
	 * Selects model item(s) which name starts with specified prefix.
	 * @param type ModelEditorItemMatcher.PROCEDURE|TABLE|...
	 */
	public void selectModelItem(String namePrefix, String type){
		SWTBotGefViewer viewer = this.getEditorViewer(new DefaultCTabItem(0).getText());
		viewer.select(viewer.editParts(new ModelEditorItemMatcher(type, namePrefix)));
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Selects attribute which name starts with specified prefix of the item which name starts with specified prefix.
	 * @param itemType ModelEditorItemMatcher.PROCEDURE|TABLE|...
	 */
	public void selectAttribute(String itemPrefix, String itemType, String attributePrefix){
		SWTBotGefViewer viewer = this.getEditorViewer(new DefaultCTabItem(0).getText());
		viewer.select(viewer.editParts(new AttributeMatcher(attributePrefix, itemType, itemPrefix)));
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Deletes specified attribute from the specified model item.
	 * @param itemType ModelEditorItemMatcher.PROCEDURE|TABLE|...
	 * @param removeFromTransformation - whether remove column from transformation too
	 */
	public void deleteAttribute(String itemName, String itemType, String attrName, boolean removeFromTransformation){
		selectAttribute(itemName, itemType, attrName);
		new ContextMenu("Delete").select();	
		AbstractWait.sleep(TimePeriod.SHORT);
		PushButton button = (removeFromTransformation) ? new PushButton("Yes") : new PushButton("No");
		button.click();
	}
	
	/**
	 * Renames specified attribute in the specified model item.
	 * @param itemType ModelEditorItemMatcher.PROCEDURE|TABLE|...
	 */
	public void renameAttribute(String itemName, String itemType, String attrName, String newName){
		selectAttribute(itemName, itemType, attrName);
		new ContextMenu("Rename").select();
		new DefaultText(0).setText(newName);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		
	}
	
	/**
	 * Sets specified data type to the specified attribute in the specified model item.
	 */
	public void setAttributeDataType(String itemName, String itemType, String attrName, String dataType, Integer length){
		selectAttribute(itemName, itemType, attrName);
		new ContextMenu("Modeling","Set Datatype").select();
		new DefaultShell("Select a Datatype");
		new DefaultText(0).setText(dataType);
		new DefaultTable().getItem(0).click();
		if (length != null){
			new LabeledSpinner("'string' length value").setValue(length);
		}		
		new PushButton("OK").click();
	}
}
