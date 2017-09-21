package org.jboss.tools.switchyard.reddeer.wizard;

/**
 * 
 * @author apodhrad
 * 
 */
public class ExistingCamelXMLServiceWizard extends ExistingServiceWizard<ExistingCamelXMLServiceWizard> {

	public static final String DIALOG_TITLE = "";

	public ExistingCamelXMLServiceWizard() {
		this(DIALOG_TITLE);
	}

	public ExistingCamelXMLServiceWizard(String title) {
		super(title);
	}

	@Override
	protected String getSelectionDialogTitle() {
		return "Select Route XML File from Project";
	}

}
