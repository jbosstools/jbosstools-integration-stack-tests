package org.jboss.tools.teiid.reddeer.preference;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * 
 * @author apodhrad
 *
 */
public class ServerPreferencePage extends PreferencePage {

	public ServerPreferencePage() {
		super("Server", "Runtime Environments");
	}

	public void addServerRuntime(String name, String path, String... type) {
		addServerRuntime(name, path, "standalone.xml", type);
	}
	
	public void addServerRuntime(String name, String path, String configFile, String... type) {
		new PushButton("Add...").click();
		//new DefaultTreeItem(0, type).select();
		selectType(type);
		new PushButton("Next >").click();
		new LabeledText("Name").setText(name);
		new LabeledText("Home Directory").setText(path);
		
		//set configuration file
		if (type[1].matches("JBoss Enterprise Application Platform 6(.*) Runtime") 
				|| type[1].matches("JBoss AS 7(.*)")
				|| type[1].matches("WildFly (.*)")){
			new SWTWorkbenchBot().textWithLabel("Configuration file: ").setText(configFile);
		}
		//new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
		new PushButton("Finish").click();
		//new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
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
		try {
			array[array.length-1] = type[array.length-1].replaceAll(" Runtime", "+ Runtime");
			new DefaultTreeItem(0, array).select();//eclipse kepler (0), eclipse juno (1)
			return;
		} catch (Exception ex){
			
		}
	}
	//TODO preferences - teiid version
	//TODO deploy preference for teiid import - seconds to wait
}
