package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.gef.editor.GEFEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class ExistingCamelXMLServiceWizard extends ExistingServiceWizard<ExistingCamelXMLServiceWizard> {

	public static final String DIALOG_TITLE = "";

	private GEFEditor editor;

	public ExistingCamelXMLServiceWizard() {
		this(null);
	}

	public ExistingCamelXMLServiceWizard(GEFEditor editor) {
		super(DIALOG_TITLE);
		this.editor = editor;
	}

	@Override
	protected String getSelectionDialogTitle() {
		return "Select Route XML File from Project";
	}

}
