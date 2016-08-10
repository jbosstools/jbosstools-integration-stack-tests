package org.jboss.tools.common.reddeer.ext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesViewProperty;
import org.jboss.reddeer.eclipse.ui.views.properties.TabbedPropertyList;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class PropertiesViewExt extends AbstractViewExt {

	public PropertiesViewExt() {
		super("Properties");
	}

	/**
	 * Returns list of Properties.
	 *
	 * @return list of Properties
	 */
	public List<PropertiesViewProperty> getProperties(){
		activate();
		LinkedList<PropertiesViewProperty> properties = new LinkedList<PropertiesViewProperty>();
		for (TreeItem treeItem : new DefaultTree().getAllItems()){
			properties.add(new PropertiesViewProperty(treeItem));
		}
		return properties;
	}
	
	/**
	 * Returns ViewProperty with propertyName.
	 *
	 * @param propertyNamePath the property name path
	 * @return ViewProperty with propertyName
	 */
	public PropertiesViewProperty getProperty(String... propertyNamePath){
		activate();
		return new PropertiesViewProperty(new DefaultTreeItem(propertyNamePath));
	}
	
	/**
	 * Sets whether to show categories.
	 * 
	 * @param toggle Indicates whether to show categories
	 */
	public void toggleShowCategories(boolean toggle){
		activate();
		new DefaultToolItem("Show Categories")
			.toggle(toggle);
	}


	/**
	 * Sets whether to show advanced properties.
	 * 
	 * @param toggle Indicates whether to show advanced properties
	 */
	public void toggleShowAdvancedProperties(boolean toggle){
		activate();
		new DefaultToolItem("Show Advanced Properties")
			.toggle(toggle);
	}
	
	/**
	 * Selects tab with a given label.
	 * 
	 * @param label Label
	 */
	public void selectTab(String label) {
		activate();
		List<String> old = new ArrayList<String>();
		
		try{
			old = new TabbedPropertyList().getTabs();
		}catch(Exception ex) {
			//probably not rendered yet
		}
		new WaitUntil(new AnotherTabsRendered(old), TimePeriod.NORMAL, false);
		
		new TabbedPropertyList().selectTab(label);
	}
	
	private class AnotherTabsRendered extends AbstractWaitCondition {

		private List<String> old;
		
		public AnotherTabsRendered(List<String> old) {
			this.old = old;
		}
		
		@Override
		public boolean test() {
			List<String> actual = new ArrayList<String>();
			
			try{
				actual = new TabbedPropertyList().getTabs();
			}catch(Exception ex) {
				//probably not rendered yet
			}
			
			return !actual.equals(old);
		}

		@Override
		public String description() {
			return "Wait for tabs of focused element to be rendered";
		}
		
	}
}
