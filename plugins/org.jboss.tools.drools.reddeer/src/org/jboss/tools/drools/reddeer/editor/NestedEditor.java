package org.jboss.tools.drools.reddeer.editor;

import java.util.List;

import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.swt.api.Menu;
import org.eclipse.reddeer.workbench.api.Editor;
import org.eclipse.reddeer.workbench.api.EditorFile;
import org.eclipse.reddeer.workbench.impl.editor.AbstractEditor.ContentAssistantEnum;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;

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

	@Override
	public void activate() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void restore() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ContentAssistant openContentAssistant() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ContentAssistant openQuickFixContentAssistant() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ContentAssistant openOpenOnAssistant() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Marker> getMarkers() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ContentAssistant getAutoContentAssistant(Runnable execute) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Image getTitleImage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Control getControl() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ContentAssistant openContentAssistant(ContentAssistantEnum assistantType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ContentAssistant openContentAssistant(String assistantLabel) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ContentAssistantEnum> getAvailableContentAssistants() {
		throw new UnsupportedOperationException();
	}

	@Override
	public EditorFile getAssociatedFile() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Menu getContextMenu() {
		throw new UnsupportedOperationException();
	}
}
