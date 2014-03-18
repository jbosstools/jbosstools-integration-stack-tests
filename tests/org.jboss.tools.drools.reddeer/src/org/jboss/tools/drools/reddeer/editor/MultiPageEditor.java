package org.jboss.tools.drools.reddeer.editor;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.workbench.exception.WorkbenchPartNotFound;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;

abstract class MultiPageEditor extends DefaultEditor {

    public MultiPageEditor() {
        if (!(editorPart instanceof MultiPageEditorPart)){
            throw new WorkbenchPartNotFound("Given editor is not instance of MultiPageEditorPart");
        }
    }

    abstract IWorkbenchPart getEditorByTitle(String title);

    IWorkbenchPart getEditorByIndex(final int index) {
        final IEditorPart editor = Display.syncExec(new ResultRunnable<IEditorPart>() {
            public IEditorPart run() {
                IEditorPart[] parts = getEditorPart().findEditors(getEditorPart().getEditorInput());

                if (index >= 0 && index < parts.length) {
                    return parts[index];
                }

                return null;
            }
        });

        if (index != getSelectedPage()) {
            Display.asyncExec(new Runnable() {
                public void run() {
                    getEditorPart().setActiveEditor(editor);
                }
            });
        }

        return editor;
    }

    protected MultiPageEditorPart getEditorPart() {
        if (editorPart == null) {
            throw new RuntimeException("workbenchPart is null");
        }
        if (!(editorPart instanceof MultiPageEditorPart)) {
            throw new RuntimeException("workbenchPart isn't instance of MultiPageEditorPart " + editorPart.getClass());
        }
        return (MultiPageEditorPart) editorPart;
    }

    protected int getSelectedPage() {
        return Display.syncExec(new ResultRunnable<Integer>() {
            public Integer run() {
                return getEditorPart().getActivePage();
            }
        });
    }
}
