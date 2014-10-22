package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.gef.condition.EditorHasEditParts;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * 
 * @author apodhrad
 * 
 */
public class CamelXMLWizard extends ServiceWizard<CamelXMLWizard> {

	public static final String DIALOG_TITLE = "New File";
	
	public static final String FILE_NAME = "File name:";
	
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();

	private GEFEditor editor;

	public CamelXMLWizard() {
		super();
	}

	public CamelXMLWizard(GEFEditor editor) {
		super();
		this.editor = editor;
	}

	public CamelXMLWizard activate() {
		bot.shell(DIALOG_TITLE).activate();
		return this;
	}

	public CamelXMLWizard setFileName(String name) {
		new LabeledText(FILE_NAME).setText(name);
		return this;
	}
	
	public CamelXMLWizard selectResource(String... path) {
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
		activate();
		super.finish();
		new WaitUntil(new EditorHasEditParts(editor, oldCount));
		editor.save();
	}

}
