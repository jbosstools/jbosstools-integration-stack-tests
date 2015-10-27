package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.core.reference.ReferencedComposite;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;

/**
 * 
 * @author apodhrad
 * 
 */
public class ExistingBPMNServiceWizard extends ExistingServiceWizard<ExistingBPMNServiceWizard> {

	public static final String DIALOG_TITLE = "";

	public ExistingBPMNServiceWizard() {
		this(null);
	}

	public ExistingBPMNServiceWizard(GEFEditor editor) {
		super(DIALOG_TITLE);
	}

	@Override
	protected String getSelectionDialogTitle() {
		return "Select Resource";
	}

	public RadioButton getRemoteREST() {
		activate();
		return new RadioButton("Remote REST");
	}

	public RadioButton getRemoteJMS() {
		activate();
		return new RadioButton("Remote JMS");
	}

	public RadioButton getKnowledgeContainer() {
		activate();
		return new RadioButton("Knowledge Container");
	}

	public RadioButton getProjectResource() {
		activate();
		return new RadioButton("Project Resource");
	}

	public ExistingBPMNServiceWizard selectRemoteREST() {
		getRemoteREST().toggle(true);
		if (!getRemoteREST().isSelected()) {
			throw new RuntimeException("Cannot select Remote REST");
		}
		return this;
	}

	public ExistingBPMNServiceWizard selectRemoteJMS() {
		getRemoteJMS().toggle(true);
		if (!getRemoteJMS().isSelected()) {
			throw new RuntimeException("Cannot select Remote JMS");
		}
		return this;
	}

	public ExistingBPMNServiceWizard selectKnowledgeContainer() {
		getKnowledgeContainer().toggle(true);
		if (!getKnowledgeContainer().isSelected()) {
			throw new RuntimeException("Cannot select Knowledge Container");
		}
		return this;
	}

	public ExistingBPMNServiceWizard selectProjectResource() {
		getProjectResource().toggle(true);
		return this;
	}

	/* Remote JMS */
	public Text getTruststoreLocation() {
		activate();
		return new LabeledText(new DefaultGroup("SSL Details"), "Truststore Location");
	}

	public Text getTruststorePassword() {
		activate();
		return new LabeledText(new DefaultGroup("SSL Details"), "Truststore Password");
	}

	public Text getKeystoreLocation() {
		activate();
		return new LabeledText(new DefaultGroup("SSL Details"), "Keystore Location");
	}

	public Text getKeystorePassword() {
		activate();
		return new LabeledText(new DefaultGroup("SSL Details"), "Keystore Password");
	}

	public Combo getUseSSL() {
		activate();
		return new LabeledCombo(new DefaultGroup("SSL Details"), "Use SSL");
	}

	public Text getMessagingPort() {
		activate();
		return new LabeledText(new DefaultGroup("JMS Details"), "Messaging Port");
	}

	public Text getRemotingPort() {
		activate();
		return new LabeledText(new DefaultGroup("JMS Details"), "Remoting Port");
	}

	public Text getHostName() {
		activate();
		return new LabeledText(new DefaultGroup("JMS Details"), "Host Name");
	}

	public Text getDeploymentID() {
		activate();
		return new LabeledText("Deployment ID");
	}

	public Text getScanInterval() {
		activate();
		return new LabeledText("Scan Interval:");
	}

	public CheckBox getScanForUpdates() {
		activate();
		return new CheckBox("Scan for updates");
	}

	public Text getBaseName() {
		activate();
		return new LabeledText("Base Name:");
	}

	public Text getVersion() {
		activate();
		return new LabeledText(new DefaultGroup("Release ID"), "Version:");
	}

	public Text getArtifactID() {
		activate();
		return new LabeledText(new DefaultGroup("Release ID"), "Artifact ID:");
	}

	public Text getGroupID() {
		activate();
		return new LabeledText(new DefaultGroup("Release ID"), "Group ID:");
	}

	public Text getSessionName() {
		activate();
		return new LabeledText("Session Name:");
	}

	/* Remote REST */
	public Button getRemove() {
		activate();
		return new PushButton(new DefaultSection("Extra JAXB Classes"), "Remove");
	}

	public Button getAdd() {
		activate();
		return new PushButton(new DefaultSection("Extra JAXB Classes"), "Add");
	}

	public Combo getUseFormBasedAuthentication() {
		activate();
		return new LabeledCombo(new DefaultGroup("REST Details"), "Use Form Based Authentication");
	}

	public Text getTimeout() {
		activate();
		ReferencedComposite ref = null;
		if (getRemoteJMS().isSelected()) {
			ref = new DefaultGroup("JMS Details");
		} else if (getRemoteREST().isSelected()) {
			ref = new DefaultGroup("REST Details");
		} else {
			throw new IllegalStateException("This field is available only in Remote JMS/REST");
		}
		return new LabeledText(ref, "Timeout");
	}

	public Text getPassword() {
		activate();
		ReferencedComposite ref = null;
		if (getRemoteJMS().isSelected()) {
			ref = new DefaultGroup("JMS Details");
		} else if (getRemoteREST().isSelected()) {
			ref = new DefaultGroup("REST Details");
		} else {
			throw new IllegalStateException("This field is available only in Remote JMS/REST");
		}
		return new LabeledText(ref, "Password");
	}

	public Text getUserName() {
		activate();
		ReferencedComposite ref = null;
		if (getRemoteJMS().isSelected()) {
			ref = new DefaultGroup("JMS Details");
		} else if (getRemoteREST().isSelected()) {
			ref = new DefaultGroup("REST Details");
		} else {
			throw new IllegalStateException("This field is available only in Remote JMS/REST");
		}
		return new LabeledText(ref, "User Name");
	}

	public Text getRESTURL() {
		activate();
		return new LabeledText("REST URL");
	}

}
