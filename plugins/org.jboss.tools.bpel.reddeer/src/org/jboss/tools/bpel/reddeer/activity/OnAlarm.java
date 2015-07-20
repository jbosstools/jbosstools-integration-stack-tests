package org.jboss.tools.bpel.reddeer.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class OnAlarm extends ContainerActivity {

	public OnAlarm(Activity parent) {
		super(null, "OnAlarm", parent, 0);
	}

	public OnAlarm setCondition(String condition, String conditionType) {
		select();
		openProperties().selectDetails().setCondition(condition, conditionType);
		save();

		return this;
	}

}
