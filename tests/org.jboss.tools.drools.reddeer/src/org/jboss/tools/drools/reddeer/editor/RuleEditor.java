package org.jboss.tools.drools.reddeer.editor;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;

public class RuleEditor extends EnhancedTextEditor {
    private static final Logger LOGGER = Logger.getLogger(RuleEditor.class);

    private static MultiPageEditor staticEditor;

    private MultiPageEditor editor;
    private String pageTitle;

    RuleEditor(MultiPageEditor editor, String pageTitle) {
        super(setUp(editor, pageTitle));
        staticEditor = null;
        this.editor = editor;
        this.pageTitle = pageTitle;
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

    /**
     * Gets the text on line selected. The returned string does not contain line delimiter (even if it is present).
     * 
     * @return content of the line that cursor is currently on.
     */
    public String getTextOnCurrentLine() {
        return Display.syncExec(new ResultRunnable<String>() {
            public String run() {
                String result = null;
                try {
                    IDocument doc = getDocument();
                    int line = doc.getLineOfOffset(getOffset());
                    String delimiter = doc.getLineDelimiter(line);
                    int delimiterLength = delimiter != null ? delimiter.length() : 0;
                    result = doc.get(doc.getLineOffset(line), doc.getLineLength(line) - delimiterLength);
                } catch (BadLocationException ex) {
                    LOGGER.error("Wrong location returned", ex);
                }
                return result;
            }
        });
    }

    public ContentAssist createContentAssist() {
        return new ContentAssist(getTextEditorPart());
    }

    /*
     * FIXME this is a nasty hack, if it was possible to use constructor with IWorkbenchPart it would not be necessary.
     */
    private static String setUp(MultiPageEditor editor, String pageTitle) {
        staticEditor = editor;
        return pageTitle;
    }

    protected IWorkbenchPart getActiveWorkbenchPart() {
        if (editor != null) {
            return editor.getEditorByTitle(pageTitle);
        } else {
            return null;
        }
    }

    protected IWorkbenchPart getPartByTitle(String title) {
        if (editor != null) {
            return editor.getEditorByTitle(title);
        } else {
            return staticEditor.getEditorByTitle(title);
        }
    }

    public void close() {
        editor.close();
    }

    public void close(boolean save) {
        editor.close(save);
    }

    public boolean isDirty() {
        return editor.isDirty();
    }

    public void maximize() {
        editor.maximize();
    }
    
    public void minimize() {
        editor.minimize();
    }
    
    public void save() {
        editor.save();
    }

}
