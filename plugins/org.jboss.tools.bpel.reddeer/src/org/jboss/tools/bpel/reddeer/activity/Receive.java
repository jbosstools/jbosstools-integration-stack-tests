package org.jboss.tools.bpel.reddeer.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class Receive extends Activity {

	public Receive(String name) {
		super(name, "Receive");
	}

	public Receive pickOperation(String operation) {
		select();
		openProperties().selectDetails().pickOperation(operation);
		save();

		return this;
	}

	public Receive checkCreateInstance() {
		select();
		openProperties().selectDetails().toggleCreateInstance(true);
		save();

		return this;
	}
}
