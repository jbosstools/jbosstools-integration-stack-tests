package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
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

	public static final String TITLE = "New Connection Profile";
	public static final String LABEL_NAME = "Name:";
	public static final String LABEL_DESCRIPTION = "Description (optional):";

	private String profile;
	private String name;
	private String description;

	public TeiidProfileWizard(String profile) {
		super("Connection Profiles", "Connection Profile");
		this.profile = profile;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
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
			next();
		}

		new DefaultTable().select(profile);
		new LabeledText(LABEL_NAME).setText(name);
		// TODO: LabeledText
		// new LabeledText(LABEL_DESCRIPTION).setText(description);
		new SWTWorkbenchBot().textWithLabel(LABEL_DESCRIPTION).setText(description);

		next();
	}

	abstract public void execute();
}
