package org.jboss.tools.switchyard.reddeer.shell;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.condition.TableHasRows;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;

/**
 * 
 * @author apodhrad
 *
 */
public class SelectDozerFileShell {

	public static final String TITLE = "Select Dozer File";

	public SelectDozerFileShell() {
		activate();
	}

	public void activate() {
		new DefaultShell(TITLE);
	}

	public SelectDozerFileShell setFileName(String fileName) {
		new DefaultText(0).setText(fileName);
		return this;
	}

	public SelectDozerFileShell selectFileName(String fileName) {
		getDefaultTableTBL().getItem(fileName).select();
		return this;
	}

	public SelectDozerFileShell waitForTableHasRows() {
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.LONG);
		return this;
	}

	public void ok() {
		new OkButton().click();
		new WaitWhile(new ShellIsAvailable(TITLE));
	}

	public Table getDefaultTableTBL() {
		return new DefaultTable(0);
	}
}
