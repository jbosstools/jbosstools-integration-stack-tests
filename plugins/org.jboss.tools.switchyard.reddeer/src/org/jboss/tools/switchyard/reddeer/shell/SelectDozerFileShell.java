package org.jboss.tools.switchyard.reddeer.shell;

import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

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
		new WaitWhile(new ShellWithTextIsAvailable(TITLE));
	}

	public Table getDefaultTableTBL() {
		return new DefaultTable(0);
	}
}
