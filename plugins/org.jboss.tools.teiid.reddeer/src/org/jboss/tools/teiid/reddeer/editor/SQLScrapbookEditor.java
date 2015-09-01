package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;



public class SQLScrapbookEditor extends SWTBotEditor {

	public SQLScrapbookEditor() {
		this("SQL Scrapbook 0");
	}
	
	public SQLScrapbookEditor(String name) {
		super(new SWTWorkbenchBot().editorByTitle(name).getReference(), new SWTWorkbenchBot());
	}

	public void setDatabase(String dbName){
		new SWTWorkbenchBot().comboBoxWithLabel("Database:").setSelection(dbName);
	}
	
	public void setText(String text){
		new SWTWorkbenchBot().styledText().setText(text);
	}
	
	public void executeAll(){
		
		new DefaultStyledText();
		new ContextMenu("Execute All").select();
		try {
			new WaitWhile(new ShellWithTextIsAvailable("SQL Statement Execution"), TimePeriod.VERY_LONG);
		} catch(Exception e) {
		}
		AbstractWait.sleep(TimePeriod.NORMAL);
		new WorkbenchShell();
	}
}
