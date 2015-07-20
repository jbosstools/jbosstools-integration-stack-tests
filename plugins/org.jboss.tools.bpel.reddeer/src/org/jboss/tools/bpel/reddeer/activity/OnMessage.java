package org.jboss.tools.bpel.reddeer.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class OnMessage extends ContainerActivity {

	public OnMessage(Activity parent) {
		this(null, parent);
	}

	public OnMessage(String name, Activity parent) {
		super(name, "OnMessage", parent, 0);
	}

	public OnMessage pickOperation(String operation) {
		select();
		openProperties().selectDetails().pickOperation(operation);
		save();

		return this;
	}
}
