package org.jboss.tools.teiid.reddeer.editor;

import java.awt.event.KeyEvent;
import java.util.List;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.spinner.LabeledSpinner;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
public class RelationalModelEditor extends ModelEditor{
	
	public RelationalModelEditor(String title) {
		super(title);
        new DefaultCTabItem(1).activate(); // legacy, some test expect package diagram as default page
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Opens transformation diagram and returns transformation editor for specified item.
	 * Note: Package Diagram must be opened.
	 * @param type ModelEditor.ItemType.PROCEDURE|TABLE|...
	 */
	public TransformationEditor openTransformationDiagram(String type, String name){
		selectModelItem(type, name);
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenuItem("Edit").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
		return new TransformationEditor();
	}
	
	/**
	 * Returns to parent diagram.
	 * Transformation Diagram -> Package Diagram
	 */
	public void returnToParentDiagram(){
		new DefaultToolItem("Show Parent Diagram").click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Returns attribute's names of specified model item.
	 */
	public List<String> listTableAttributes(String tableName){
		return listItemAttributes(ModelEditor.ItemType.TABLE, tableName);
	}
	
	/**
	 * Deletes specified attribute from the specified model item.
	 * @param itemType ModelEditor.ItemType.PROCEDURE|TABLE|...
	 * @param removeFromTransformation - whether remove column from transformation too
	 */
	public void deleteAttribute(String itemName, String itemType, String attrName, boolean removeFromTransformation){
		selectModelItemAttribute(attrName, itemType, itemName);
		new ContextMenuItem("Delete").select();	
		AbstractWait.sleep(TimePeriod.SHORT);
		PushButton button = (removeFromTransformation) ? new PushButton("Yes") : new PushButton("No");
		button.click();
		new WorkbenchShell();
		activate();
	}
	
	/**
	 * Renames specified attribute in the specified model item.
	 * @param itemType ModelEditorItemMatcher.PROCEDURE|TABLE|...
	 */
	public void renameAttribute(String itemName, String itemType, String attrName, String newName){
		selectModelItemAttribute(attrName, itemType, itemName);
		new ContextMenuItem("Rename").select();
		new DefaultText(0).setText(newName);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		
	}
	
	/**
	 * Sets specified data type to the specified attribute in the specified model item.
	 */
	public void setAttributeDataType(String itemName, String itemType, String attrName, String dataType, Integer length){
		selectModelItemAttribute(attrName, itemType, itemName);
		new ContextMenuItem("Modeling","Set Datatype").select();
		new DefaultShell("Select a Datatype");
		new DefaultText(0).setText(dataType);
        AbstractWait.sleep(TimePeriod.SHORT); // wait if show only dataType in the table
		new DefaultTable().getItem(0).click();
		if (length != null){
			new LabeledSpinner("'string' length value").setValue(length);
		}		
		new PushButton("OK").click();
	}

    @Override
    public void save() {
        AbstractWait.sleep(TimePeriod.SHORT); // on windows save button shows after a second
        super.save();
    }
}
