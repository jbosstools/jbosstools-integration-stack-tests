package org.jboss.tools.teiid.reddeer.editor;

import java.awt.event.KeyEvent;
import java.util.List;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.dialog.InputSetEditorDialog;
import org.jboss.tools.teiid.reddeer.matcher.RecursiveButtonMatcher;

public class XmlModelEditor extends ModelEditor {
	
	public XmlModelEditor(String title) {
		super(title);
	}
	
	/**
	 * Opens editor (=Mapping Diagram) of specified document.
	 * Note: document overview (=Package Diagram) must be opened.
	 */
	public void openDocument(String document){
		selectModelItem(ModelEditor.ItemType.XML_DOCUMENT, document);
        AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenuItem("Open").select();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Deletes specified document.
	 * Note: document overview (=Package Diagram) must be opened.
	 */
	public void deleteDocument(String document){
		new WorkbenchShell();
		selectModelItem(ModelEditor.ItemType.XML_DOCUMENT, document);
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenuItem("Delete").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		try {
			new DefaultShell("Dependent Models Detected");
			new PushButton("Yes").click();
			new WaitWhile(new ShellIsActive("Dependent Models Detected"), TimePeriod.DEFAULT);
		} catch (SWTLayerException e) {	
			// shell not opened -> continue
		}
		AbstractWait.sleep(TimePeriod.SHORT);
		try {
			new DefaultShell("Confirm SQL Update");
			new PushButton("Yes").click();
			new WaitWhile(new ShellIsActive("Confirm SQL Update"), TimePeriod.DEFAULT);
		} catch (SWTLayerException e) {	
			// shell not opened -> continue
		}
		AbstractWait.sleep(TimePeriod.SHORT);
		activate();
	}
	
	/**
	 * Renames specified document to specified new name.
	 * Note: document overview (=Package Diagram) must be opened.
	 */
	public void renameDocument(String document, String newName){
		new WorkbenchShell();
		selectModelItem(ModelEditor.ItemType.XML_DOCUMENT, document);
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenuItem("Rename").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultText().setText(newName);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
        this.save();
	}
	
	/**
	 * Returns to parent diagram.
	 * Diagram -> Mapping Diagram -> Package Diagram
	 */
	public void returnToParentDiagram(){
		new DefaultToolItem("Show Parent Diagram").click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Opens editor (=Diagram) of specified mapping class.
	 * Note: mapping class overview (=Mapping Diagram) must be opened.
	 */
	public void openMappingClass(String mappingClass){
		selectModelItem(ModelEditor.ItemType.MAPPING_CLASS, mappingClass);
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenuItem("Open").select();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Creates new mapping class from specified element.
	 */
	public void createMappingClass(String... fromElementPath){
		new DefaultTreeItem(fromElementPath).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultToolItem("New Mapping Class").click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Opens editor (=Diagram) of specified staging table.
	 * Note: mapping class overview (=Mapping Diagram) must be opened.
	 */
	public void openStagingTable(String stagingTable){
		selectModelItem(ModelEditor.ItemType.STAGING_TABLE, stagingTable);
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenuItem("Open").select();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Creates new staging table from specified element.
	 */
	public void createStagingTable(String... fromElementPath){
		new DefaultTreeItem(fromElementPath).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultToolItem("New Staging Table").click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Opens input set editor's dialog.
	 * Note: mapping class editor (=Diagram) must be opened.
	 */
	public InputSetEditorDialog openInputSetEditor(){
		selectModelItem(ModelEditor.ItemType.INPUT_SET, "Input Set");
		new ContextMenuItem("Edit Input Set").select();;
		return new InputSetEditorDialog();
	}
	
	/**
	 * Opens transformation internal editor.
	 * Note: mapping class editor (=Diagram) must be opened.
	 */
	public TransformationEditor openTransformationEditor(){
		selectTransformationArrow();
		new ContextMenuItem("Edit").select();;
		AbstractWait.sleep(TimePeriod.getCustom(3));
		return new TransformationEditor();
	}
	
	/**
	 * Opens recursion internal editor.
	 * Note: mapping class editor (=Diagram) must be opened. 
	 */
	public RecursionEditor openRecursiveEditor(){
		executeMatcher(RecursiveButtonMatcher.createMatcher(""));
        return new RecursionEditor(this);
	}
	
	/**
	 * Adds attribute to specified mapping class.
	 */
	public void addAttribute(String mappingClass, String attribute){
		selectModelItem(ModelEditor.ItemType.MAPPING_CLASS, mappingClass);
		new ContextMenuItem("New Child", "Mapping Class Column").select();
		new DefaultText(0).setText(attribute);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);		
	}
	
	/**
	 * Copies attribute from one mapping class to another.
	 */
	public void copyAttribute(String fromMappingClass, String toMappingClass, String attribute){
		selectModelItemAttribute(attribute, ModelEditor.ItemType.MAPPING_CLASS, fromMappingClass);
		new ContextMenuItem("Copy").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		selectModelItem(ModelEditor.ItemType.MAPPING_CLASS, toMappingClass);
		new ContextMenuItem("Paste").select();
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
	}
	
	/**
	 * Deletes attribute from specified mapping class.
	 */
	public void deleteAttribute(String mappingClass, String attribute){
		selectModelItemAttribute(attribute, ModelEditor.ItemType.MAPPING_CLASS, mappingClass);
		new ContextMenuItem("Delete").select();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Returns attribute's names of specified mapping class.
	 */
	public List<String> listMappingClassAttributes(String mappingClass){
		return listItemAttributes(ModelEditor.ItemType.MAPPING_CLASS, mappingClass);
	}
}
