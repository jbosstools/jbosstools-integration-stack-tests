package org.jboss.tools.bpel.reddeer.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class Reply extends Activity {

	public Reply(String name) {
		super(name, REPLY);
	}

	public Reply pickOperation(String operation) {
		select();
		openProperties().selectDetails().pickOperation(operation);
		save();

		return this;
	}
}
