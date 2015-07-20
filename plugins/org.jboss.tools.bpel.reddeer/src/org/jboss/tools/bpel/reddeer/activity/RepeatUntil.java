package org.jboss.tools.bpel.reddeer.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class RepeatUntil extends ContainerActivity {

	public RepeatUntil(String name) {
		super(name, "RepeatUntil");
	}

	public RepeatUntil setCondition(String condition) {
		select();
		openProperties().selectDetails().setCondition(condition);
		save();

		return this;
	}

}
