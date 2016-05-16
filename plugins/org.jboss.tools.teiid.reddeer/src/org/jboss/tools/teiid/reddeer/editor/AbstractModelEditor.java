package org.jboss.tools.teiid.reddeer.editor;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;

public abstract class AbstractModelEditor extends SWTBotEditor {

	AbstractModelEditor(String title){
		super(new SWTWorkbenchBot().editorByTitle(title).getReference(), new SWTWorkbenchBot());
	}
	
	protected SWTBotGefViewer getEditorViewer(String tabLabel) {
		final DefaultCTabItem tabItem = new DefaultCTabItem(tabLabel);
		tabItem.activate();
		GraphicalViewer graphicalViewer = syncExec(new Result<GraphicalViewer>() {
			@Override
			public GraphicalViewer run() {
				Object obj = tabItem.getSWTWidget().getData();
				if (obj instanceof GraphicalEditor) {
					GraphicalEditor ge = (GraphicalEditor) obj;
					Object obj2 = ge.getAdapter(GraphicalViewer.class);
					if (obj2 instanceof GraphicalViewer) {
						return (GraphicalViewer) obj2;
					}
				} 
				return null;
			}
		});
		return new SWTBotGefViewer(graphicalViewer);
	}

}
