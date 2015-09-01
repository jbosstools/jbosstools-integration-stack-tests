package org.jboss.tools.teiid.reddeer.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Condition that specifies if a progress window is still present
 * 
 * @author apodhrad
 * 
 */
public class DefaultTreeItemAvailable implements WaitCondition {

	private String[] PATH;

	public DefaultTreeItemAvailable(String... path){
		this.PATH = path;
	}
	
	@Override
	public boolean test() {
		try {
			new DefaultTreeItem(PATH);
			return true;
		} catch (Exception e) {
			// ok, not enabled
			return false;
		}
	}

	@Override
	public String description() {
		return "Default tree item with path " + PATH + " not enabled yet";
	}

}
