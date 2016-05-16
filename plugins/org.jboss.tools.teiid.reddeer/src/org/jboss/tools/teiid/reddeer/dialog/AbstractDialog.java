package org.jboss.tools.teiid.reddeer.dialog;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

public abstract class AbstractDialog {
	private static final Logger log = Logger.getLogger(AbstractDialog.class);
	
	protected final String title;
	
	/**
	 * Parent class for every dialog.
	 * Note: action that opens dialog should be done before
	 * i.e. this (/child) class only handles already opened dialogs. 
	 * (dialogs can be opened in different ways and requirements)
	 */
	AbstractDialog(String title){
		this.title = title;
		activate();
	}
	
	/**
	 * Sets focus to dialog window.
	 */
	public void activate(){
		log.info("Activating '" + title + "' Dialog");
		new DefaultShell(title);
	}

	/**
	 * Confirms changes and closes dialog.
	 */
	public abstract void finish();
	
	/**
	 * Rejects changes and closes dialog.
	 */
	public void cancel(){
		log.info("Canceling '" + title + "' Dialog");
		new CancelButton().click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
	}
}
