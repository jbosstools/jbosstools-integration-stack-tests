package org.jboss.tools.teiid.reddeer.editor;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;

public class WebServiceModelEditor extends AbstractModelEditor {
	public static final String PACKAGE_DIAGRAM = "Package Diagram";
	public static final String OPERATION_EDITOR = "Operation Editor";
	
	public WebServiceModelEditor(String title) {
		super(title);
		AbstractWait.sleep(TimePeriod.SHORT);
		this.getEditorViewer(PACKAGE_DIAGRAM);
	}
	
	/**
	 * Opens editor (=Operation Tab) of specified interface.
	 * Note: interface overview (=Package Diagram) must be opened.
	 */
	public void openOperationEditor(){
		new DefaultCTabItem(OPERATION_EDITOR).activate();
	}
	
	/**
	 * Selects specified operation in specified interface.
	 */
	public void selectOperation(String iinterface, String operation){
		new DefaultTreeItem(iinterface, operation).select();
	}
	
	/**
	 * Sets text to procedure of operation and validates it.
	 */
	public void setOperationProcedure(String text){
		new DefaultStyledText().setText(text);
		new DefaultToolItem("Save/Validate SQL").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
	}
	
	/**
	 * Returns text from procedure of operation.
	 */
	public String getOperationProcedure(){
		return new DefaultStyledText().getText();
	}
	
	/**
	 * Replaces specified text in procedure of operation to specified value 
	 * and validates it.
	 */
	public void replaceTextInOperationProcedure(String target, String replacement){
		new DefaultStyledText().selectText(target);
		new DefaultStyledText().insertText(replacement);
		new DefaultToolItem("Save/Validate SQL").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
	}
}
