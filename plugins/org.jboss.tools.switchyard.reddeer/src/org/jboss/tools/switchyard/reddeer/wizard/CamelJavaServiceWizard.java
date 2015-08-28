package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.gef.condition.EditorHasEditParts;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class CamelJavaServiceWizard extends ServiceWizard<CamelJavaServiceWizard> {

	public static final String DIALOG_TITLE = "New Java Class";

	private GEFEditor editor;

	public CamelJavaServiceWizard() {
		this(null);
	}

	public CamelJavaServiceWizard(GEFEditor editor) {
		super(DIALOG_TITLE);
		this.editor = editor;
	}

	public CamelJavaServiceWizard setName(String name) {
		new LabeledText("Name:").setText(name);
		return this;
	}

	public CamelJavaServiceWizard open() {
		return new SwitchYardEditor().addCamelJavaImplementation();
	}

	@Override
	protected void browse() {
		new PushButton(2).click();
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
