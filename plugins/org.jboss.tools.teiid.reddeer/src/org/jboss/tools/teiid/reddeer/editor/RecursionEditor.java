package org.jboss.tools.teiid.reddeer.editor;


import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.swt.handler.ButtonHandler;
import org.jboss.reddeer.swt.util.Display;

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
	
	
private class ToolBarButtonWithLabel extends BaseMatcher {
		
	private Button but;
	private String label;
	
	public ToolBarButtonWithLabel(){
		this.label = "";
	}
	
	public ToolBarButtonWithLabel(String label){
		this.label = label;
		
	}
	
		@Override
		public boolean matches(Object o) {//ToolItem
			if (o instanceof ToolItem){
				ToolItem ti = (ToolItem)o;
				if (ti.getControl() instanceof Button){
					but = (Button)ti.getControl();
					if (but.getText().equals(label)){
						System.out.println(but.getText());
						Display.syncExec(new Runnable() {
							@Override
							public void run() {
								ButtonHandler.getInstance().click(but);
							}
						});	
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public void describeTo(Description arg0) {
			
	}

		
}
}
