package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 * 
 */
public abstract class ConnectionProfileWizard extends NewMenuWizard {

	public static final String DIALOG_TITLE = "New connection profile";
	public static final String LABEL_NAME = "Name:";

	private String profile;
	private String name;

	public ConnectionProfileWizard(String profile,String name) {
		super(DIALOG_TITLE, "Connection Profiles", "Connection Profile");
		this.profile = profile;
		this.name = name;
	}

	@Override
	public void open() {
		log.info("Open Connection Profile");
		try {
			super.open();
		} catch (Exception ex) {
			new DefaultTreeItem("Connection Profiles").collapse();
			new DefaultTreeItem("Connection Profiles", "Connection Profile").expand();
			new DefaultTreeItem("Connection Profiles", "Connection Profile").select();
			super.next();
		}

		new DefaultTable().select(profile);
		if(name!=null){
			new LabeledText(LABEL_NAME).setText(name);
		}
		super.next();
	}

	@Override
	public void finish() {
		new FinishButton().click();;
	};
	
	public abstract ConnectionProfileWizard testConnection();
}
