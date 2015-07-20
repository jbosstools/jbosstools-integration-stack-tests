package org.jboss.tools.bpel.reddeer.activity;


/**
 * 
 * @author apodhrad
 * 
 */
public class Invoke extends Activity {

	public Invoke(String name) {
		super(name, INVOKE);
	}

	public Invoke pickOperation(String operation) {
		select();
		openProperties().selectDetails().pickOperation(operation);
		save();

		return this;
	}

}
