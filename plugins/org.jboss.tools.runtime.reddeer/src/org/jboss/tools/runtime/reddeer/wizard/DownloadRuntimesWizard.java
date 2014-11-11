package org.jboss.tools.runtime.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

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

	public void acceptTerms() {

		// for those runtimes that need credentials to jboss.org
		try {
			new DefaultText(0).setText("1234sgf");
			new DefaultText(1).setText("1234sgf");
			next();
		} catch (Exception e) {
		}
		new RadioButton(0).toggle(true);
	}

	public void setInstallFolder(String path) {

		new LabeledText("Install folder:").setText(path);
	}

	public void setDownloadFolder(String path) {

		new LabeledText("Download folder:").setText(path);
	}

	public void finish(String runtime) {

		super.finish();
		new WaitWhile(new ShellWithTextIsAvailable("Download '" + runtime), TimePeriod.getCustom(900));
		new DefaultShell();
	}
}
