package org.jboss.tools.runtime.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;

/**
 * Represents <i>Download Runtimes</i> dialog that is displayed after click on <i>Download...</i>
 * button in <i>JBoss Runtime Detection</i> preference page.
 * 
 * @author tsedmik
 */
public class DownloadRuntimesWizard extends WizardDialog {

	/**
	 * Accesses names of all available runtimes.
	 * 
	 * @return List with names 
	 */
	public List<String> getAllRuntimes() {

		List<String> result = new ArrayList<String>();
		for (TableItem item : new DefaultTable().getItems()) {
			result.add(item.getText());
		}
		return result;
	}

	/**
	 * Select a given runtime
	 * 
	 * @param runtime name of the runtime
	 */
	public void selectRuntime(String runtime) {

		new DefaultTableItem(runtime).select();
	}

	/**
	 * Gets project URL (if available)
	 * 
	 * @param runtime name of the runtime
	 * @return project URL
	 */
	public String getProjectURL(String runtime) {

		selectRuntime(runtime);
		return new DefaultLink(0).getText();
	}

	/**
	 * Gets download URL (if available)
	 * 
	 * @param runtime name of the runtime
	 * @return download URL
	 */
	public String getDownloadURL(String runtime) {

		selectRuntime(runtime);
		return new DefaultLink(1).getText();
	}
}
