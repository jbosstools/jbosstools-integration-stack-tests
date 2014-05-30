package org.jboss.tools.drools.reddeer.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;

public class RuleEditor extends NestedTextEditor {
    RuleEditor(MultiPageEditor editor, String pageTitle) {
        super(editor, pageTitle);
    }

    public void setBreakpoint(int line) {
        try {
            IDocument doc = getDocument();
            int lineOffset = doc.getLineOffset(line);

            IToggleBreakpointsTarget breakpoint = (IToggleBreakpointsTarget)getEditorPart().getAdapter(IToggleBreakpointsTarget.class);
            breakpoint.toggleLineBreakpoints(getEditorPart(), new TextSelection(doc, lineOffset, 0));
        } catch (BadLocationException ex) {
            throw new IllegalArgumentException("Wrong line number", ex);
        } catch (CoreException ex) {
            throw new RuntimeException("Unable to set breakpoint", ex);
        }
    }

    public ContentAssist createContentAssist() {
        return new ContentAssist(getTextEditorPart());
    }
}
