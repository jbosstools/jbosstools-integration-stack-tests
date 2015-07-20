package org.jboss.tools.bpel.reddeer.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class Wait extends Activity {

	public Wait(String name) {
		super(name, WAIT);
	}

	public Wait setCondition(String condition, String conditionType) {
		select();
		openProperties().selectDetails().setCondition(condition, conditionType);
		save();

		return this;
	}

}
