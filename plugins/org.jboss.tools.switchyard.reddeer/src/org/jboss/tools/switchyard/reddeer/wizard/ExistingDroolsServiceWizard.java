package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.swt.impl.button.RadioButton;

/**
 * 
 * @author apodhrad
 * 
 */
public class ExistingDroolsServiceWizard extends ExistingServiceWizard<ExistingDroolsServiceWizard> {

	public static final String DIALOG_TITLE = "";

	private GEFEditor editor;

	public ExistingDroolsServiceWizard() {
		this(null);
	}

	public ExistingDroolsServiceWizard(GEFEditor editor) {
		super(DIALOG_TITLE);
		this.editor = editor;
	}

	@Override
	protected String getSelectionDialogTitle() {
		return "Select Resource";
	}

	public RadioButton getRemoteREST() {
		return new RadioButton("Remote REST");
	}

	public RadioButton getRemoteJMS() {
		return new RadioButton("Remote JMS");
	}

	public RadioButton getKnowledgeContainer() {
		return new RadioButton("Knowledge Container");
	}

	public RadioButton getProjectResource() {
		return new RadioButton("Project Resource");
	}

	public ExistingDroolsServiceWizard selectRemoteREST() {
		getRemoteREST().toggle(true);
		return this;
	}

	public ExistingDroolsServiceWizard selectRemoteJMS() {
		getRemoteJMS().toggle(true);
		return this;
	}

	public ExistingDroolsServiceWizard selectKnowledgeContainer() {
		getKnowledgeContainer().toggle(true);
		return this;
	}

	public ExistingDroolsServiceWizard selectProjectResource() {
		getProjectResource().toggle(true);
		return this;
	}

}
