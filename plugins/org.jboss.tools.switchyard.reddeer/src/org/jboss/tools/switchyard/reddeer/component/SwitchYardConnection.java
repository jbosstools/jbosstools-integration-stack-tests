package org.jboss.tools.switchyard.reddeer.component;

import org.eclipse.reddeer.gef.impl.connection.DefaultConnection;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;

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
		new ContextMenuItem("Create Required Transformers").select();
	}
}
