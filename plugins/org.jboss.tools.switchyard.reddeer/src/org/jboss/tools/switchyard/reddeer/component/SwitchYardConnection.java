package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.reddeer.gef.impl.connection.DefaultConnection;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;

/**
 * 
 * @author apodhrad
 *
 */
public class SwitchYardConnection extends DefaultConnection {

	/**
	 * Constructs a connection at index 0.
	 */
	public SwitchYardConnection() {
		this(0);
	}

	/**
	 * Constructs a connection at a given index.
	 * 
	 * @param index
	 *            Index
	 */
	public SwitchYardConnection(int index) {
		super(index);
	}

	public void createRequiredTransformers() {
		select();
		new ContextMenu("Create Required Transformers").select();
	}
}
