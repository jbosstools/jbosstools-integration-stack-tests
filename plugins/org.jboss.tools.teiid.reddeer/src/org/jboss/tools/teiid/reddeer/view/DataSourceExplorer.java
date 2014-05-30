package org.jboss.tools.teiid.reddeer.view;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.jboss.reddeer.eclipse.jdt.ui.AbstractExplorer;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class DataSourceExplorer extends AbstractExplorer {

	public DataSourceExplorer() {

		super("Data Source Explorer");
	}

	public void openSQLScrapbook(String datasource) {

		open();
		selectVDB(datasource);
		new ContextMenu("Open SQL Scrapbook").select();
	}

	public void setVDBDriver(String properties, String vdb) {

		Properties props = new Properties();
		try {
			props.load(new FileReader(properties));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		selectVDB(vdb);
		new ContextMenu("Properties").select();
		new DefaultTreeItem("Driver Properties").select();
		new DefaultCombo("Drivers:").setSelection(props.getProperty("driver"));
		new PushButton("OK").click();

		try {
			new PushButton("Yes").click();
		} catch (Exception e) {
			// Confirm not appeared
		}
	}

	private void selectVDB(String vdb) {

		for (TreeItem item : new DefaultTree(0).getItems()) {
			if (item.getText().equals("Database Connections")) {
				for (TreeItem dbConnections : item.getItems()) {
					if (dbConnections.getText().startsWith(vdb)) {
						dbConnections.select();
						break;
					}
				}
				break;
			}
		}
	}
}
