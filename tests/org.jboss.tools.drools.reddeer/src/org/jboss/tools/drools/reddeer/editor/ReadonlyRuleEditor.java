package org.jboss.tools.drools.reddeer.editor;

final class ReadonlyRuleEditor extends RuleEditor {

    static ReadonlyRuleEditor newInstance(MultiPageEditor editor, String pageTitle) {
        staticEditor = editor;
        staticPageTitle = pageTitle;
        return new ReadonlyRuleEditor(editor, pageTitle);
    }

    private ReadonlyRuleEditor(MultiPageEditor editor, String pageTitle) {
        super(editor, pageTitle);
    }

    public void setText(String text) {
        throw new UnsupportedOperationException("DRL Viewer is read only!");
    }

    public ContentAssist createContentAssist() {
        throw new UnsupportedOperationException("DRL Viewer is read only!");
    }

    public void setBreakpoint(int line) {
        throw new UnsupportedOperationException("DRL Viewer is read only!");
    }

    public void writeText(String text) {
        throw new UnsupportedOperationException("DRL Viewer is read only!");
    }
}
