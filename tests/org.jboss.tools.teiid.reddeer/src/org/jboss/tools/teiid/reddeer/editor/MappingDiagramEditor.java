package org.jboss.tools.teiid.reddeer.editor;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.asyncExec;

import java.util.List;

import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.IEditorReference;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.matcher.AttributeMatcher;
import org.jboss.tools.teiid.reddeer.matcher.IsTransformation;
import org.jboss.tools.teiid.reddeer.matcher.MappingClassMatcher;
import org.jboss.tools.teiid.reddeer.matcher.RecursiveButtonMatcher;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;

public class MappingDiagramEditor extends SWTBotEditor{

	public MappingDiagramEditor(IEditorReference editorReference, SWTWorkbenchBot bot)
			throws WidgetNotFoundException {
		super(editorReference, bot);
		new SWTWorkbenchBot().sleep(5 * 1000);
	}

	private Clickable button;
	private String prefixMappingClassRecursive;
	private SWTBotGefViewer viewer;
	public static final String MAPPING_DIAGRAM = "Mapping Diagram";
	private static final String DIAGRAM = "Diagram";
	private ModelEditor me;

	public MappingDiagramEditor(String title) {
		super(new SWTWorkbenchBot().editorByTitle(title).getReference(), new SWTWorkbenchBot());
		//new SWTWorkbenchBot().sleep(5 * 1000);
		this.me = new ModelEditor(title);
		viewer = me.getGraphicalViewer(MAPPING_DIAGRAM);
	}

	public RecursionEditor clickOnRecursiveButton(String mappingClass){
		this.prefixMappingClassRecursive = mappingClass;
		RecursiveButtonMatcher matcher = RecursiveButtonMatcher.createRecursiveButtonMatcher(mappingClass);
		viewer.editParts(matcher);
		return new RecursionEditor();
	}
	
	/**
	 * 
	 * @param prefix
	 * @return all attributes (type Label) with name starting with prefix
	 */
	public List<SWTBotGefEditPart> getAttributes(String prefix){
		//viewer = new ModelEditor(super.getTitle()).getGraphicalViewer(MAPPING_DIAGRAM);
		AttributeMatcher matcher = AttributeMatcher.createAttributeMatcher();
		matcher.setPrefix(prefix);
		return viewer.editParts(matcher); 
	}
	
	
	/**
	 * 
	 * @param prefix
	 * @return all mapping classes (type Label) with name starting with prefix
	 */
	public List<SWTBotGefEditPart> getMappingClasses(String prefix){
		//viewer = new ModelEditor(super.getTitle()).getGraphicalViewer(MAPPING_DIAGRAM);
		MappingClassMatcher matcher = MappingClassMatcher.createMappingClassMatcher();
		matcher.setPrefix(prefix);
		return viewer.editParts(matcher);
	}
	
	/**
	 * 
	 * @param prefix
	 * @return list of attributes starting with prefix
	 */
	public List<String> namesOfAttributes(String prefix){
			//viewer = new ModelEditor(super.getTitle()).getGraphicalViewer(MAPPING_DIAGRAM);
			AttributeMatcher matcher = AttributeMatcher.createAttributeMatcher();
			matcher.setPrefix(prefix);
			List<SWTBotGefEditPart> attributes = viewer.editParts(matcher);//generate list of texts
			return matcher.getTexts();
	}
	
	/**
	 * 
	 * @param mappingClass
	 * @param column in form "columnName : type"
	 */
	public void addMappingClassColumns(String mappingClass, String... columns){
		for (String column : columns){
			//new ModelExplorerView().open();
			new SWTWorkbenchBot().sleep(5000);
			me.selectParts(getMappingClasses(mappingClass));
			//new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
			new ContextMenu("New Child", "Mapping Class Column").select();
			new org.jboss.reddeer.swt.impl.text.DefaultText(0).setText(column);
			new SWTWorkbenchBot().activeShell().pressShortcut(Keystrokes.TAB);
			me.save();
			//new SWTWorkbenchBot().sleep(5000);
			//click somewhere else
			//new org.jboss.reddeer.swt.impl.tree.DefaultTreeItem(0).select();//chooses the xml schema
			//new ModelExplorerView().open();
		}
	}
	
	public void showTransformation() {
		viewer = me.getGraphicalViewer(DIAGRAM);
		viewer.editParts(IsTransformation.isTransformation()).get(0).select();
		viewer.clickContextMenu("Edit");
		new SWTWorkbenchBot().sleep(5 * 1000);
	}
	
	public void copyAttribute(String fromClass, String toClass, String attr){
		me.selectParts(this.getMappingClasses(fromClass));
		me.selectParts(this.getAttributes(attr));
		new ContextMenu("Copy").select();
		me.selectParts(this.getMappingClasses(toClass));
		new ContextMenu("Paste").select();
		new SWTWorkbenchBot().activeShell().pressShortcut(Keystrokes.TAB);
		me.save();
	}
}
