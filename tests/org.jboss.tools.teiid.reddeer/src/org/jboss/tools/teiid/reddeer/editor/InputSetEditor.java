package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author lfabriko
 *
 */
public class InputSetEditor {

	public InputSetEditor(){
		
	}
	
	public InputSetEditor(String title){
		new ModelEditor(title).openInputSetEditor();
	}
	
	public void createNewInputParam(String... pathToParam){
		new DefaultTreeItem(1, pathToParam).select();
		new PushButton("< New").click();
	}
	
	public void close(){
		new SWTWorkbenchBot().toolbarButton(1).click();//close the inputset editor
	}
}
