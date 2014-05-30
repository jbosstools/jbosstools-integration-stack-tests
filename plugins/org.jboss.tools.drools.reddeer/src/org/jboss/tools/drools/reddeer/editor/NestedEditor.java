package org.jboss.tools.drools.reddeer.editor;

import org.eclipse.ui.IEditorPart;
import org.jboss.reddeer.workbench.api.Editor;

public class NestedEditor implements Editor {
    private IEditorPart editor;
    private Editor parent;

    public NestedEditor(MultiPageEditor parent, String title) {
        this.parent = parent;
        editor = parent.getEditorByTitle(title);
    }

    protected IEditorPart getEditorPart() {
        return editor;
    }

    public String getTitle() {
        return parent.getTitle();
    }

    public String getTitleToolTip() {
        return parent.getTitleToolTip();
    }

    public boolean isDirty() {
        return parent.isDirty();
    }

    public void save() {
        parent.save();
    }

    public void close(boolean save) {
        parent.close(save);
    }

    public boolean isActive() {
        return parent.isActive();
    }

    public void close() {
        parent.close();
    }

    public void maximize() {
        parent.maximize();
    }

    public void minimize() {
        parent.minimize();
    }

}
