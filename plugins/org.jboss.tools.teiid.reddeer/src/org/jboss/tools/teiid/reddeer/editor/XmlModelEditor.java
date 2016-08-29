package org.jboss.tools.teiid.reddeer.editor;

import java.awt.event.KeyEvent;
import java.util.List;

import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.tools.teiid.reddeer.dialog.InputSetEditorDialog;
import org.jboss.tools.teiid.reddeer.matcher.AttributeMatcher;
import org.jboss.tools.teiid.reddeer.matcher.IsTransformation;
import org.jboss.tools.teiid.reddeer.matcher.ModelEditorItemMatcher;
import org.jboss.tools.teiid.reddeer.matcher.RecursiveButtonMatcher;

public class XmlModelEditor extends AbstractModelEditor {
	public static final String PACKAGE_DIAGRAM = "Package Diagram";
	public static final String MAPPING_DIAGRAM = "Mapping Diagram";
	public static final String DIAGRAM = "Diagram";
	
	public XmlModelEditor(String title) {
		super(title);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Opens editor (=Mapping Diagram) of specified document.
	 * Note: document overview (=Package Diagram) must be opened.
	 */
	public void openDocument(String document){
		SWTBotGefViewer viewer = this.getEditorViewer(PACKAGE_DIAGRAM);
		viewer.select(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.XML_DOCUMENT, document)));
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenu("Open").select();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Deletes specified document.
	 * Note: document overview (=Package Diagram) must be opened.
	 */
	public void deleteDocument(String document){
		SWTBotGefViewer viewer = this.getEditorViewer(PACKAGE_DIAGRAM);
		viewer.select(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.XML_DOCUMENT, document)));
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenu("Delete").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		try {
			new DefaultShell("Dependent Models Detected");
			new PushButton("Yes").click();
			new WaitWhile(new ShellWithTextIsActive("Dependent Models Detected"), TimePeriod.NORMAL);
		} catch (SWTLayerException e) {	
			// shell not opened -> continue
		}
		AbstractWait.sleep(TimePeriod.SHORT);
		try {
			new DefaultShell("Confirm SQL Update");
			new PushButton("Yes").click();
			new WaitWhile(new ShellWithTextIsActive("Confirm SQL Update"), TimePeriod.NORMAL);
		} catch (SWTLayerException e) {	
			// shell not opened -> continue
		}
		AbstractWait.sleep(TimePeriod.SHORT);
		this.show();
	}
	
	/**
	 * Renames specified document to specified new name.
	 * Note: document overview (=Package Diagram) must be opened.
	 */
	public void renameDocument(String document, String newName){
		SWTBotGefViewer viewer = this.getEditorViewer(PACKAGE_DIAGRAM);
		viewer.select(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.XML_DOCUMENT, document)));
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenu("Rename").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultText().setText(newName);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Shows document overview (=Package Diagram).
	 * Note: mapping class overview (=Mapping Diagram) must be opened.
	 */
	public void returnToDocumentOverview(){
		new DefaultToolItem("Show Parent Diagram").click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Opens editor (=Diagram) of specified mapping class.
	 * Note: mapping class overview (=Mapping Diagram) must be opened.
	 */
	public void openMappingClass(String mappingClass){
		SWTBotGefViewer viewer = this.getEditorViewer(MAPPING_DIAGRAM);
		viewer.select(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.MAPPING_CLASS, mappingClass)));
		AbstractWait.sleep(TimePeriod.SHORT);
		viewer.select(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.MAPPING_CLASS, mappingClass)));
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenu("Open").select();
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
	 * Shows mapping class overview (=Mapping Diagram).
	 * Note: mapping class (/staging table) editor (=Diagram) must be opened.
	 */
	public void returnToMappingClassOverview(){
		new DefaultToolItem("Show Parent Diagram").click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Opens editor (=Diagram) of specified staging table.
	 * Note: mapping class overview (=Mapping Diagram) must be opened.
	 */
	public void openStagingTable(String stagingTable){
		SWTBotGefViewer viewer = this.getEditorViewer(MAPPING_DIAGRAM);
		viewer.select(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.STAGING_TABLE, stagingTable)));
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenu("Open").select();
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
		SWTBotGefViewer viewer = this.getEditorViewer(DIAGRAM);
		viewer.select(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.INPUT_SET, "Input Set")));
		new ContextMenu("Edit Input Set").select();;
		return new InputSetEditorDialog();
	}
	
	/**
	 * Opens transformation internal editor.
	 * Note: mapping class editor (=Diagram) must be opened.
	 */
	public TransformationEditor openTransformationEditor(){
		SWTBotGefViewer viewer = this.getEditorViewer(DIAGRAM);
		viewer.select(viewer.editParts(IsTransformation.isTransformation()));
		new ContextMenu("Edit").select();;
		AbstractWait.sleep(TimePeriod.getCustom(3));
		return new TransformationEditor();
	}
	
	/**
	 * Opens recursion internal editor.
	 * Note: mapping class editor (=Diagram) must be opened. 
	 */
	public RecursionEditor openRecursiveEditor(){
		RecursiveButtonMatcher matcher = RecursiveButtonMatcher.createRecursiveButtonMatcher("");
		this.getEditorViewer(DIAGRAM).editParts(matcher);
		return new RecursionEditor();
	}
	
	/**
	 * Selects mapping class with specified prefix
	 */
	public void selectModelItem(String namePrefix, String type){
		SWTBotGefViewer viewer = this.getEditorViewer(MAPPING_DIAGRAM);
		viewer.select(viewer.editParts(new ModelEditorItemMatcher(type, namePrefix)));
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Selects attribute with specified prefix of specified mapping class
	 */
	public void selectMappingClassAttribute(String mappingClassPrefix, String attributePrefix){
		SWTBotGefViewer viewer = this.getEditorViewer(MAPPING_DIAGRAM);
		viewer.select(viewer.editParts(new AttributeMatcher(attributePrefix, ModelEditorItemMatcher.MAPPING_CLASS, mappingClassPrefix)));
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Adds attribute to specified mapping class.
	 */
	public void addAttribute(String mappingClass, String attribute){
		selectModelItem(mappingClass, ModelEditorItemMatcher.MAPPING_CLASS);
		new ContextMenu("New Child", "Mapping Class Column").select();
		new DefaultText(0).setText(attribute);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);		
	}
	
	/**
	 * Copies attribute from one mapping class to another.
	 */
	public void copyAttribute(String fromMappingClass, String toMappingClass, String attribute){
		selectMappingClassAttribute(fromMappingClass, attribute);
		new ContextMenu("Copy").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		selectModelItem(toMappingClass, ModelEditorItemMatcher.MAPPING_CLASS);
		new ContextMenu("Paste").select();
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
	}
	
	/**
	 * Deletes attribute from specified mapping class.
	 */
	public void deleteAttribute(String mappingClass, String attribute){
		selectMappingClassAttribute(mappingClass, attribute);
		new ContextMenu("Delete").select();
	}
	
	/**
	 * Returns attribute's names of specified mapping class.
	 * @return
	 */
	public List<String> listAttributesNames(String mappingClass){
		AttributeMatcher matcher = new AttributeMatcher("", ModelEditorItemMatcher.MAPPING_CLASS, mappingClass);
		getEditorViewer(MAPPING_DIAGRAM).editParts(matcher);
		return matcher.getTexts();
	}
}
