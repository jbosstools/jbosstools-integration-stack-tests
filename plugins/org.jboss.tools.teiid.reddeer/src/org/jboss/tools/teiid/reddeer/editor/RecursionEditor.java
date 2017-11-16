package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.spinner.LabeledSpinner;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.teiid.reddeer.matcher.ToolBarButtonWithLabel;

public class RecursionEditor {

    private static final Logger log = Logger.getLogger(RecursionEditor.class);

    private XmlModelEditor parentEditor = null; // for activate function

	public static class LimitAction{
		public static final String THROW = "THROW";
		public static final String RECORD = "RECORD";
		public static final String DISCARD = "DISCARD";
	}

    /**
     * @param parentEditor
     *            - which editor contains recursion editor
     */
    public RecursionEditor(XmlModelEditor parentEditor) {
        this.parentEditor = parentEditor;
    }
	
    public void activate() {
        parentEditor.activate();
    }

    public void toggleRecursion(final boolean toggle) {
        log.info(((toggle) ? "checking" : "unchecking") + "Enable recursion");
        activate();
        Display.syncExec(new Runnable() {
            @Override
            public void run() {
                new DefaultToolItem(new ToolBarButtonWithLabel("Enable Recursion", toggle));
            }
        });

	}

	public void limitRecursion(int limit) {
        activate();
		new LabeledSpinner("Count Limit:").setValue(limit);
	}

	public void close() {
        activate();
		new DefaultToolItem("Close").click();
	}
	
	/**
	 * @param action - RecursionEditor.LimitAction.THROW|...
	 */
	public void setActionWhenLimitExceeded(String action){
        activate();
		new LabeledCombo("Action When Count Limit Exceeded:").setSelection(action);
	}

}
