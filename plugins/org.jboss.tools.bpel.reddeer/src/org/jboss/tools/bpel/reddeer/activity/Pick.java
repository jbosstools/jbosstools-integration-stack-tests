package org.jboss.tools.bpel.reddeer.activity;


/**
 * 
 * @author apodhrad
 * 
 */
public class Pick extends Activity {

	public Pick(String name) {
		super(name, PICK);
	}

	public Pick checkCreateInstance() {
		select();
		openProperties().selectDetails().toggleCreateInstance(true);
		save();

		return this;
	}

	public OnMessage addOnMessage() {
		menu("Add OnMessage");
		return new OnMessage(this);
	}

	public OnAlarm addOnAlarm() {
		menu("Add OnAlarm");
		return new OnAlarm(this);
	}

}
