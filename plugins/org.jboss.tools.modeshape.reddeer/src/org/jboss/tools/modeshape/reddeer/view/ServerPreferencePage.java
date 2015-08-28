package org.jboss.tools.modeshape.reddeer.view;

import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

/**
 * Preference Page for Server Runtimes.
 * 
 * @author apodhrad
 *
 */
public class ServerPreferencePage extends WorkbenchPreferencePage {

	public ServerPreferencePage() {
		super("Server", "Runtime Environments");
	}

	public void addServerRuntime(String name, String path, String... type) {
		new PushButton("Add...").click();
		//new DefaultTreeItem(type).select();
		selectType(type);
		new PushButton("Next >").click();
		new LabeledText("Name").setText(name);
		new LabeledText("Home Directory").setText(path);
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private void selectType(String[] type){
		String[] array = new String[type.length];
		System.arraycopy(type, 0, array, 0, array.length);
		
		try {
			new DefaultTreeItem(0, array).select();//eclipse kepler (0), eclipse juno (1)
			return;
		} catch (Exception ex){
			System.out.println(type + " not found, trying other variants...");
		}
		array[array.length-1] = type[array.length-1].replaceAll(" Runtime", "+ Runtime");
		new DefaultTreeItem(0, array).select();//eclipse kepler (0), eclipse juno (1)
		return;
	}
}
