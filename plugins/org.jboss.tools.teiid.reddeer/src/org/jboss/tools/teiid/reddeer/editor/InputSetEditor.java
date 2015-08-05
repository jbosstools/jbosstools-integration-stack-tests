package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author lfabriko
 *
 */
public class InputSetEditor {

	public InputSetEditor(){
		new DefaultShell("Edit Input Set");
	}
	
	public InputSetEditor(String title){
		new ModelEditor(title).openInputSetEditor();
		new DefaultShell("Edit Input Set");
	}
	
	public void createNewInputParam(String... pathToParam){
		new DefaultTree();
		new DefaultTreeItem(pathToParam).select();
		new PushButton("< New").click();
	}
	
	public void close(){
		new PushButton("OK").click();
	}
}
