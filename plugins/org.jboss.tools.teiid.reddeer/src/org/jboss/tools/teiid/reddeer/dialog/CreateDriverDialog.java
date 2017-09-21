package org.jboss.tools.teiid.reddeer.dialog;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.list.DefaultList;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

public class CreateDriverDialog extends AbstractDialog {
	
	private static final Logger log = Logger.getLogger(CreateDriverDialog.class);
	
	public CreateDriverDialog() {
		super("New Driver Definition");
		log.info("New Driver Definition dialog is opened");

	}

	public CreateDriverDialog selectTemplate(String type, String version){
		new DefaultTabItem("Name/Type").activate();
		TreeItem root = new DefaultTree().getItems().get(0);
		for (TreeItem item : root.getItems()) {
			if (type.equals(item.getCell(0)) && version.equals(item.getCell(2))) {
				item.select();
				break;
			}
		}
		return this;
	}
	
	/**
	 * First must be run selectTemplate
	 */
	public CreateDriverDialog setName(String name){
		log.info("Set driver name to: '"+ name +"'");
		activate();
		new LabeledText("Driver name:").setText(name);
		return this;
	}
	
	public CreateDriverDialog addDriver(String path){
		log.info("Set driver path to: '"+ path +"'");
		new DefaultTabItem("JAR List").activate();
		clearAllDriverLibraries();
		addItem(path);
		addItem(path);
		removeDriverLibrary(path);
		return this;
	}

	public CreateDriverDialog setDriverClassGeneric(String driverClass) {
		log.info("Set driver class to: '"+ driverClass +"'");
		new DefaultTabItem("Properties").activate();
		new DefaultTreeItem(new DefaultTree(0), "General", "Driver Class").doubleClick();
		new PushButton("...").click();
		new DefaultShell("Available Classes from Jar List");
		new DefaultText().setText(driverClass);
		new PushButton("OK").click();
		activate();
		return this;
	}
	
	public CreateDriverDialog setDriverClass(String driverClass){
		log.info("Set driver class to: '"+ driverClass +"'");
		new DefaultTabItem("Properties").activate();
		new DefaultTreeItem(new DefaultTree(0), "General", "Driver Class").doubleClick();
		new DefaultText().setText(driverClass);
		activate();
		return this;
	}
	
	public CreateDriverDialog setConnectionUrl(String connectionUrl){
		log.info("Set driver connection url to: '"+ connectionUrl +"'");
		new DefaultTabItem("Properties").activate();
		new DefaultTreeItem(new DefaultTree(0), "General", "Connection URL").doubleClick();
		new DefaultText().setText(connectionUrl);
		activate();
		return this;
	}
	
	public CreateDriverDialog setDatabaseName(String databaseName) {
		log.info("Set driver database name to: '"+ databaseName +"'");
		new DefaultTabItem("Properties").activate();
		new DefaultTreeItem(new DefaultTree(0), "General", "Database Name").doubleClick();
		new DefaultText().setText(databaseName);
		activate();
		return this;
	}
	
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
	}
	
	private void clearAllDriverLibraries() {
		if (new DefaultList().getListItems().length > 0) {			
			new PushButton("Clear All").click();
		} else {
			log.info("No drivers to clean, skipped");
		}			
	}
	
	public void removeDriverLibrary(String driverLocation) {
		new DefaultList().select(driverLocation);
		new PushButton("Remove JAR/Zip").click();
	}
	
	private void addItem(final String item) {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				new DefaultList().getSWTWidget().add(item);
			}
		});
	}

}
