package org.jboss.tools.drools.reddeer.editor;

import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.ui.IEditorPart;

public class DslrEditor extends MultiPageEditor {
	private static final String TEXT_EDITOR = "Text Editor";
	private static final String DRL_VIEWER = "DRL Viewer";

	public RuleEditor showRuleEditor() {
		return new RuleEditor(this, TEXT_EDITOR);
	}

	public RuleEditor showDrlViewer() {
		return new ReadonlyRuleEditor(this, DRL_VIEWER);
	}

	IEditorPart getEditorByTitle(String title) {
		if (new ShellMenuItem(new WorkbenchShell(), "File", "Save").isEnabled()) {
			save();
		}
		if (TEXT_EDITOR.equals(title)) {
			return getEditorByIndex(0);
		}
		if (DRL_VIEWER.equals(title)) {
			return getEditorByIndex(1);
		}

		return null;
	}

}
