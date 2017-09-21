package org.jboss.tools.switchyard.reddeer.wizard;

/**
 * 
 * @author apodhrad
 * 
 */
public class ExistingBPELServiceWizard extends ExistingServiceWizard<ExistingBPELServiceWizard> {

	public static final String DIALOG_TITLE = "";

	public ExistingBPELServiceWizard() {
		this(DIALOG_TITLE);
	}

	public ExistingBPELServiceWizard(String title) {
		super(title);
	}

	@Override
	protected String getSelectionDialogTitle() {
		return "Select Resource";
	}

}
