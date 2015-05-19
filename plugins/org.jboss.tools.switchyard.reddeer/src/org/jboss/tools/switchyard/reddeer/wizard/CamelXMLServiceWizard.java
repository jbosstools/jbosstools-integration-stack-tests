package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.gef.condition.EditorHasEditParts;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * 
 * @author apodhrad
 * 
 */
public class CamelXMLServiceWizard extends ServiceWizard<CamelXMLServiceWizard> {

	public static final String DIALOG_TITLE = "New File";

	private GEFEditor editor;

	public CamelXMLServiceWizard() {
		this(null);
	}

	public CamelXMLServiceWizard(GEFEditor editor) {
		super(DIALOG_TITLE);
		this.editor = editor;
	}

	public CamelXMLServiceWizard selectResource(String... path) {
		new DefaultTreeItem(path).select();
		return this;
	}

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}

	@Override
	public void finish() {
		int oldCount = editor.getNumberOfEditParts();
		super.finish();
		new WaitUntil(new EditorHasEditParts(editor, oldCount));
		editor.save();
	}

}
