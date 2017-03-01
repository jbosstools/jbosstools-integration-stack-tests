package org.jboss.tools.teiid.reddeer.editor;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class WebServiceModelEditor extends ModelEditor {
	
	public WebServiceModelEditor(String title) {
		super(title);
		new DefaultCTabItem(0).activate();
	}
	
	/**
	 * Sets text to procedure of operation and validates it.
	 */
	public void setOperationProcedure(String iinterface, String operation, String text){
		new DefaultCTabItem("Operation Editor").activate();
		new DefaultTreeItem(iinterface, operation).select();
		new DefaultStyledText().setText(text);
		new DefaultToolItem("Save/Validate SQL").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		activate();
	}
	
	/**
	 * Returns text from procedure of operation.
	 */
	public String getOperationProcedure(String iinterface, String operation){
		new DefaultCTabItem("Operation Editor").activate();
		new DefaultTreeItem(iinterface, operation).select();
		return new DefaultStyledText().getText();
	}
	
	/**
	 * Replaces specified text in procedure of operation to specified value 
	 * and validates it.
	 */
	public void replaceTextInOperationProcedure(String iinterface, String operation, String replacedText, String replacement){
		new DefaultCTabItem("Operation Editor").activate();
		new DefaultTreeItem(iinterface, operation).select();
		new DefaultStyledText().selectText(replacedText);
		new DefaultStyledText().insertText(replacement);
		new DefaultToolItem("Save/Validate SQL").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		activate();
	}
	
	/**
	 * Replaces all specified text in procedure of operation to specified value 
	 * and validates it.
	 */
	public void replaceAllTextInOperationProcedure(String iinterface, String operation, String replacedText, String replacement){
		new DefaultCTabItem("Operation Editor").activate();
		new DefaultTreeItem(iinterface, operation).select();
		String procedureText = new DefaultStyledText().getText();
		procedureText = procedureText.replaceAll(replacedText, replacement);
		new DefaultStyledText().setText(procedureText);
		new DefaultToolItem("Save/Validate SQL").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		activate();
	}
}
