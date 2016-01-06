package org.jboss.tools.teiid.reddeer.wizard;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionPage;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.preference.DriverDefinitionPreferencePageExt;

/**
 * 
 * @author apodhrad
 * 
 */
public class HSQLDBDriverWizard {

	public static final String DEFAULT_NAME = "HSQLDB Driver";
	public static final String DEFAULT_DRIVER = "org.hsqldb.jdbc.JDBCDriver";

	private String name;
	private String library;
	private String driver;

	public HSQLDBDriverWizard(String library) {
		super();
		this.library = library;
		this.driver = DEFAULT_DRIVER;
		this.name = DEFAULT_NAME;
	}

	public HSQLDBDriverWizard setDriver(String driver) {
		this.driver = driver;
		return this;
	}

	public HSQLDBDriverWizard setName(String name) {
		this.name = name;
		return this;
	}

	public void create() {
		new DriverDefinitionPreferencePageExt().open();
		// new SWTWorkbenchBot().sleep(10000);
		new PushButton("Add...").click();
		// new SWTWorkbenchBot().sleep(10000);
		DriverDefinitionPageExt page = new DriverDefinitionPageExt();

		// System.out.println(new SWTWorkbenchBot().activeShell().getText());
		// new SWTWorkbenchBot().sleep(10000);
		page.selectDriverTemplate(true, "Generic JDBC Driver", "1.0");

		// System.out.println(new SWTWorkbenchBot().activeShell().getText());
		// System.out.println("Should set name to " + name);
		// new SWTWorkbenchBot().sleep(10000);
		page.setName(name);

		System.out.println(new SWTWorkbenchBot().activeShell().getText());
		System.out.println("should set library to " + library);
		// new SWTWorkbenchBot().sleep(10000);
		page.addDriverLibrary(library);

		// System.out.println(new SWTWorkbenchBot().activeShell().getText());
		System.out.println("should set driver to " + driver);
		// new SWTWorkbenchBot().sleep(10000);
		page.setDriverClass(driver);
		// new SWTWorkbenchBot().sleep(10000);
		new PushButton("OK").click();
		// new SWTWorkbenchBot().sleep(10000);
		new PushButton("OK").click();
	}

	private class DriverDefinitionPageExt extends DriverDefinitionPage {

		@Override
		public void setDriverClass(String driverClass) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(new DefaultTree(0), "General", "Driver Class").doubleClick();// kepler 0, juno 1
			new PushButton("...").click();
			new DefaultText().setText(driverClass);
			new PushButton("OK").click();
		}

		@Override
		public void addDriverLibrary(String driverLocation) {
			selectTab(TAB_JAR_LIST);
			addItem(driverLocation);
			addItem(driverLocation);
			removeDriverLibrary(driverLocation);
		}

		public void selectDriverTemplate(boolean b, String type, String version) {
			System.out.println("SELECT DRIVER TEMPLATE");
			selectTab(TAB_NAME_TYPE);
			Tree tree = new DefaultTree();
			// Database
			TreeItem root = tree.getItems().get(0);
			for (TreeItem item : root.getItems()) {
				if (type.equals(item.getCell(0))) {
					System.out.println("TYPE FOUND");
					/*
					 * try { System.out.println(item.getCell(2));
					 * 
					 * if (item.getCell(2) == null){ System.out.println("---is null"); }
					 * 
					 * if (item.getCell(2).equals(null)) { System.out.println("---null"); } if
					 * (item.getCell(2).isEmpty()){ System.out.println("---is empty"); }
					 * 
					 * 
					 * }catch (Exception ex){ System.out.println("EXC "+ ex.getMessage());
					 * 
					 * if (item.getCell(2).isEmpty()){ System.out.println("is empty"); } }
					 */

					if (item.getCell(2).isEmpty() || version.equals(item.getCell(2))) {
						item.select();
						break;
					}
				}
			}
		}

		private void addItem(final String item) {
			syncExec(new VoidResult() {

				@Override
				public void run() {
					new SWTWorkbenchBot().list().widget.add(item);
				}
			});
		}

	}
}
