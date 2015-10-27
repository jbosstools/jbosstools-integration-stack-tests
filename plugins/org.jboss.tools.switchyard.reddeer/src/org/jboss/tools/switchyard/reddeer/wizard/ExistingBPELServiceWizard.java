package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.gef.editor.GEFEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class ExistingBPELServiceWizard extends ExistingServiceWizard<ExistingBPELServiceWizard> {

	public static final String DIALOG_TITLE = "";

	public ExistingBPELServiceWizard() {
		this(null);
	}

	public ExistingBPELServiceWizard(GEFEditor editor) {
		super(DIALOG_TITLE);
	}

	@Override
	protected String getSelectionDialogTitle() {
		return "Select Resource";
	}

}
