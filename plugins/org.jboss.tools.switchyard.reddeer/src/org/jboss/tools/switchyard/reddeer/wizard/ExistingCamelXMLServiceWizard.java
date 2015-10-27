package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.gef.editor.GEFEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class ExistingCamelXMLServiceWizard extends ExistingServiceWizard<ExistingCamelXMLServiceWizard> {

	public static final String DIALOG_TITLE = "";

	public ExistingCamelXMLServiceWizard() {
		this(null);
	}

	public ExistingCamelXMLServiceWizard(GEFEditor editor) {
		super(DIALOG_TITLE);
	}

	@Override
	protected String getSelectionDialogTitle() {
		return "Select Route XML File from Project";
	}

}
