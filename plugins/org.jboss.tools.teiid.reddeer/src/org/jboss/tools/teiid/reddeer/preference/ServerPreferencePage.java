package org.jboss.tools.teiid.reddeer.preference;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.preference.PreferencePage;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

/**
 * 
 * @author apodhrad
 *
 */
public class ServerPreferencePage extends PreferencePage {

	public ServerPreferencePage(ReferencedComposite ref) {
		super(ref, "Server", "Runtime Environments");
	}

	public void addServerRuntime(String name, String path, String... type) {
		addServerRuntime(name, path, "standalone.xml", type);
	}

	public void addServerRuntime(String name, String path, String configFile, String... type) {
		new PushButton("Add...").click();
		new DefaultShell("New Server Runtime Environment");
		selectType(type);
		new PushButton("Next >").click();
		new DefaultShell("New Server Runtime Environment");
		new LabeledText("Name").setText(name);
		new LabeledText("Home Directory").setText(path);

		// set configuration file
		if (type[1].matches("JBoss Enterprise Application Platform 6(.*) Runtime") || type[1].matches("JBoss 7(.*)")
				|| type[1].matches("WildFly (.*)")) {
			new LabeledText("Configuration file: ").setText(configFile);
		}
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	private void selectType(String[] type) {
		String[] array = new String[type.length];
		System.arraycopy(type, 0, array, 0, array.length);

		try {
			new DefaultTreeItem(new DefaultTree(0), array).select();// eclipse kepler (0), eclipse juno (1)
			return;
		} catch (Exception ex) {
			System.out.println(type + " not found, trying other variants...");
		}
		try {
			array[array.length - 1] = type[array.length - 1].replaceAll(" Runtime", "+ Runtime");
			new DefaultTreeItem(new DefaultTree(0), array).select();// eclipse kepler (0), eclipse juno (1)
			return;
		} catch (Exception ex) {

		}
	}
	// TODO preferences - teiid version
	// TODO deploy preference for teiid import - seconds to wait
}
