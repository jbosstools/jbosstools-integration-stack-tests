package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 * 
 */
public abstract class TeiidProfileWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "New connection profile";
	public static final String LABEL_NAME = "Name:";

	private String profile;
	private String name;

	public TeiidProfileWizard(String profile,String name) {
		super("Connection Profiles", "Connection Profile");
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

	/**
	 * use nextPage()
	 */
	@Deprecated
	@Override
	public void next(){
		super.next();
	}
	
	public abstract TeiidProfileWizard testConnection();
}
