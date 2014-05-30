package org.jboss.tools.teiid.reddeer.editor;


import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.asyncExec;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotWorkbenchPart;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.reference.ReferencedComposite;
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
								WidgetHandler.getInstance().click(but);
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
