package org.jboss.tools.teiid.reddeer.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.gef.handler.ViewerHandler;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.ui.IEditorPart;
import org.hamcrest.BaseMatcher;
import org.jboss.tools.teiid.reddeer.matcher.ModelEditorItemAttributeMatcher;
import org.jboss.tools.teiid.reddeer.matcher.ModelEditorItemMatcher;
import org.jboss.tools.teiid.reddeer.matcher.TransformationArrowMatcher;

public abstract class ModelEditor extends DefaultEditor {
	
	public class ItemType{
		public static final String MAPPING_CLASS = "<<Mapping Class>>";
		public static final String TABLE = "<<Table>>";
		public static final String PROCEDURE = "<<Procedure>>";
		public static final String XML_DOCUMENT = "<<XML Document>>";
		public static final String INPUT_SET = "<<Input Set>>";
		public static final String STAGING_TABLE = "<<Staging Table>>";
		public static final String INTERFACE = "<<Interface>>";
	}
	
	ModelEditor(String name){
		super(name);
        new DefaultCTabItem(1).activate(); // legacy, some test expect package diagram as default page
		activate();
	}
	
    @Override
    public void save() {
        if (new ShellMenuItem(new WorkbenchShell(), "File", "Save").isEnabled()) {
            super.save();
        }
    }

	public void saveAndClose(){
		save();
		close();
	}
	
	/**
	 * Opens table editor's tab and returns it.
	 */
	public TableEditor openTableEditor(){
        this.save();
		return new TableEditor(this.getTitle());
	}	
	
	/**
	 * Selects model item of specified type with specified name.
	 */
	public void selectModelItem(final String type, final String name){
		activate();
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
                DefaultCTabItem tabItem = new DefaultCTabItem(1);
				tabItem.activate();
				GraphicalViewer viewer = ((IEditorPart) tabItem.getSWTWidget().getData()).getAdapter(GraphicalViewer.class);
				viewer.select(ViewerHandler.getInstance().getEditParts(viewer, new ModelEditorItemMatcher(type, name)).get(0));
			}
		});
	}
	
	/**
	 * Selects attribute with specified name of specified model item.
	 */
	public void selectModelItemAttribute(final String name, final String parentType, final String parentName){
		activate();
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
                DefaultCTabItem tabItem = new DefaultCTabItem(1);
				tabItem.activate();
				GraphicalViewer viewer = ((IEditorPart) tabItem.getSWTWidget().getData()).getAdapter(GraphicalViewer.class);
				viewer.select(ViewerHandler.getInstance().getEditParts(viewer, new ModelEditorItemAttributeMatcher(name, parentType, parentName)).get(0));
			}
		});
	}
	
	/**
	 * Returns names of all attributes of specified model item.
	 */
	public List<String> listItemAttributes(final String itemType, final String itemName){
		activate();
		final List<String> names = new ArrayList<>();
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
                DefaultCTabItem tabItem = new DefaultCTabItem(1);
				tabItem.activate();
				ModelEditorItemAttributeMatcher matcher = new ModelEditorItemAttributeMatcher("", itemType, itemName);
				
				GraphicalViewer viewer = ((IEditorPart) tabItem.getSWTWidget().getData()).getAdapter(GraphicalViewer.class);
				ViewerHandler.getInstance().getEditParts(viewer, matcher);
				names.addAll(matcher.getAllAttributeNames());
			}
		});
		return names;
	}
	
	/**
	 * Selects transformation arrow.
	 */
	public void selectTransformationArrow(){
		activate();
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
                DefaultCTabItem tabItem = new DefaultCTabItem(1);
				tabItem.activate();
				GraphicalViewer viewer = ((IEditorPart) tabItem.getSWTWidget().getData()).getAdapter(GraphicalViewer.class);
				viewer.select(ViewerHandler.getInstance().getEditParts(viewer, TransformationArrowMatcher.getInstance()).get(0));
			}
		});
	}
	
	/**
	 * Executes specified matcher. 
	 * Note: No further action is made, method just runs matcher over editor parts.
	 */
	public void executeMatcher(final BaseMatcher<EditPart> matcher){
		activate();
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
                DefaultCTabItem tabItem = new DefaultCTabItem(1);
				tabItem.activate();
				GraphicalViewer viewer = ((IEditorPart) tabItem.getSWTWidget().getData()).getAdapter(GraphicalViewer.class);
				ViewerHandler.getInstance().getEditParts(viewer, matcher);
			}
		});
	}
	
	
}
