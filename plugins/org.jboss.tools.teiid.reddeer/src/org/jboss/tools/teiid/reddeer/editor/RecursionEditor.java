package org.jboss.tools.teiid.reddeer.editor;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.spinner.LabeledSpinner;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.teiid.reddeer.matcher.ToolBarButtonWithLabel;

public class RecursionEditor {
	public static class LimitAction{
		public static final String THROW = "THROW";
		public static final String RECORD = "RECORD";
		public static final String DISCARD = "DISCARD";
	}
	
	
	public void toggleRecursion(boolean flag) {
		new DefaultToolItem(new ToolBarButtonWithLabel("Enable Recursion", flag));
	}

	public void limitRecursion(int limit) {
		new LabeledSpinner("Count Limit:").setValue(limit);
	}

	public void close() {
		new DefaultToolItem("Close").click();
	}
	
	/**
	 * @param action - RecursionEditor.LimitAction.THROW|...
	 */
	public void setActionWhenLimitExceeded(String action){
		new LabeledCombo("Action When Count Limit Exceeded:").setSelection(action);
	}

}
