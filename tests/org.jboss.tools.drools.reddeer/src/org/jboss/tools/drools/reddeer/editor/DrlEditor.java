package org.jboss.tools.drools.reddeer.editor;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.reddeer.swt.util.Display;

public class DrlEditor extends MultiPageEditor {
    private static final String TEXT_EDITOR = "Text Editor";
    private static final String RETE_TREE = "Rete Tree";

    public RuleEditor showRuleEditor() {
        return new RuleEditor(this, TEXT_EDITOR);
    }

    public void showReteTree() {
        Display.syncExec(new Runnable() {
            public void run() {
                for (IEditorPart editor : getEditorPart().findEditors(getEditorPart().getEditorInput())) {
                    if (!(editor instanceof ITextEditor)) {
                        getEditorPart().setActiveEditor(editor);
                    }
                }
            }
        });
    }

    IWorkbenchPart getEditorByTitle(String title) {
        if (TEXT_EDITOR.equals(title)) {
            return getEditorByIndex(0);
        }
        if (RETE_TREE.equals(title)) {
            return getEditorByIndex(1);
        }

        return null;
    }

}
