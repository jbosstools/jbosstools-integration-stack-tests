package org.jboss.tools.teiid.reddeer.view;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.reddeer.eclipse.ui.navigator.resources.AbstractExplorer;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

public class DataSourceExplorer extends AbstractExplorer {

	public DataSourceExplorer() {

		super("Data Source Explorer");
	}

	public void openSQLScrapbook(String datasource) {

		open();
		selectVDB(datasource);
		new ContextMenuItem("Open SQL Scrapbook").select();
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
		new ContextMenuItem("Properties").select();
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
