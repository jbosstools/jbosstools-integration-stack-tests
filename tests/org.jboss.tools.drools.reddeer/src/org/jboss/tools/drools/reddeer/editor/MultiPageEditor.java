package org.jboss.tools.drools.reddeer.editor;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.reddeer.workbench.exception.EditorNotFoundException;

abstract class MultiPageEditor extends DefaultEditor {

    public MultiPageEditor() {
        if (!(workbenchPart instanceof MultiPageEditorPart)){
            throw new EditorNotFoundException("Given editor is not instance of MultiPageEditorPart");
        }
    }

    abstract IWorkbenchPart getEditorByTitle(String title);

    IWorkbenchPart getEditorByIndex(final int index) {
        return Display.syncExec(new ResultRunnable<IWorkbenchPart>() {
            public IWorkbenchPart run() {
                IEditorPart[] parts = getEditorPart().findEditors(getEditorPart().getEditorInput());

                if (index >= 0 && index < parts.length) {
                    getEditorPart().setActiveEditor(parts[index]);
                    return parts[index];
                }

                return null;
            }
        });
    }

    protected MultiPageEditorPart getEditorPart() {
        if (workbenchPart == null) {
            throw new RuntimeException("workbenchPart is null");
        }
        if (!(workbenchPart instanceof MultiPageEditorPart)) {
            throw new RuntimeException("workbenchPart isn't instance of MultiPageEditorPart");
        }
        return (MultiPageEditorPart) workbenchPart;
    }
}
