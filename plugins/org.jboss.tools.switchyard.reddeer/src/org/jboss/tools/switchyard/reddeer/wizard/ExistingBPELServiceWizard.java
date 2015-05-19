package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.gef.editor.GEFEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class ExistingBPELServiceWizard extends ExistingServiceWizard<ExistingBPELServiceWizard> {

	public static final String DIALOG_TITLE = "";

	private GEFEditor editor;

	public ExistingBPELServiceWizard() {
		this(null);
	}

	public ExistingBPELServiceWizard(GEFEditor editor) {
		super(DIALOG_TITLE);
		this.editor = editor;
	}

	@Override
	protected String getSelectionDialogTitle() {
		return "Select Resource";
	}

}
