package org.jboss.tools.teiid.reddeer.editor;


import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.teiid.reddeer.matcher.ToolBarButtonWithLabel;

public class RecursionEditor {
	private static final String ENABLE_RECURSION = "Enable Recursion";
	
	public void enableRecursion(){
		new SWTBot().widgets(new ToolBarButtonWithLabel(ENABLE_RECURSION));
	}
	
	public void limitRecursion(int countLimit){
		//new SWTWorkbenchBot().spinnerWithLabel("Count Limit: ").setSelection(countLimit);
		throw new UnsupportedOperationException();
	}
	
	public void close(){
		new SWTBot().toolbarButtonWithTooltip("Close").click();
	}
	
	
}
