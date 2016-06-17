package org.jboss.tools.fuse.reddeer.editor;

import java.util.ArrayList;
import java.util.List;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Manipulates with dialog (Wizard) for adding Global Camel Endpoints
 * 
 * @author djelinek
 */
public class CamelEndpointDialog {
	
	private static final String TYPE = "JBoss Fuse";

	public void activate() {
		new WaitUntil(new ShellWithTextIsAvailable("Choose Global Camel endpoint"));
		new DefaultShell("Choose Global Camel endpoint");
	}
	
	/**
	 * Method for select an Endpoint global element in the Camel Endpoint dialog<br/>
	 * 
	 * @param component
	 *            Name of a endpoint component that will be select
	 */
	public void chooseCamelComponent(String component) {
		new DefaultTreeItem(new String[] { component }).select();
	}
	
	public PushButton getFinish() {
		return new PushButton("Finish");
	}
	
	public PushButton getCancel() {
		return new PushButton("Cancel");
	}
	
	/**
	 * Click on the Finish button in Camel endpoint dialog<br/>
	 */
	public void Finish() {
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsAvailable("Choose Global Camel endpoint"));
	}
	
	/**
	 * Click on the Cancel button in Camel endpoint dialog<br/>
	 */
	public void Cancel() {
		new PushButton("Cancel").click();
		new WaitWhile(new ShellWithTextIsAvailable("Choose Global Camel endpoint"));
	}
	
	public LabeledText getId() {
		return new LabeledText("Id *");
	}
	
	/**
	 * Return name of Camel endpoint element<br/>
	 */
	public String getIdText() {
		return new LabeledText("Id *").getText();
	}
	
	/**
	 * Set name of Camel endpoint element<br/>
	 * 
	 * @param id
	 * 	 	   String id that will be set as name of endpoint element
	 */
	public void setId(String id) {
		new LabeledText("Id *").setText(id);
	}
	
	public LabeledText getFilter() {
		return new LabeledText(new DefaultGroup("Camel component selection"),"Id *");
	}
	
	/**
	 * Return filter text in Camel endpoint dialog<br/>
	 */
	public String getFilterText() {
		return new LabeledText(new DefaultGroup("Camel component selection"),"Id *").getText();
	}
	
	/**
	 * Set text for filter in Camel endpoint dialog<br/>
	 * 
	 * @param filter
	 * 			String text that will be used for filter
	 */
	public void setFilter(String filter) {
		new LabeledText(new DefaultGroup("Camel component selection"),"Id *").setText(filter);;
	}
	
	public Button getShowOnlyPaletteComponents() {
		return new CheckBox(new DefaultGroup("Camel component selection"),"Show only palette components");
	}
	
	/**
	 * Checks button (checkbox) "Show only palette components" in Camel endpoint dialog<br/>
	 */
	public void setShowOnlyPaletteComponents() {
		new CheckBox(new DefaultGroup("Camel component selection"),"Show only palette components").click();
	}
	
	public Button getGroupedByCategories() {
		return new CheckBox(new DefaultGroup("Camel component selection"),"Grouped by categories");
	}
	
	/**
	 * Checks button (checkbox) "Grouped by categories" in Camel endpoint dialog<br/>
	 */
	public void setGroupedByCategories() {
		new CheckBox(new DefaultGroup("Camel component selection"),"Grouped by categories").click();
	}	
	
	/**
	 * Static method for gets an list of available endpoint global elements from the Camel Endpoint dialog<br/>
	 */
	public static List<String> getEndpoints() {

		CamelEditor.switchTab("Configurations");		
		new PushButton("Add").click();
		new WaitUntil(new ShellWithTextIsAvailable("Create new global element..."));
		new DefaultShell("Create new global element...");
		new DefaultTreeItem(new String[] { TYPE, "Endpoint" }).select();	
		new PushButton("OK").click();
		CamelEndpointDialog endpointDialog = new CamelEndpointDialog();
		endpointDialog.activate();			
		List<TreeItem> items = new DefaultTree().getItems();
		List<String> components = new ArrayList<String>();
		for (TreeItem treeItem : items) {
			components.add(treeItem.getText());
		}				
		endpointDialog.Cancel();
		return components;
	}
}