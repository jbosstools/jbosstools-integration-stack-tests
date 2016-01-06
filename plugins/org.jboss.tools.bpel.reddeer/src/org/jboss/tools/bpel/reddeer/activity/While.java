package org.jboss.tools.bpel.reddeer.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class While extends ContainerActivity {

	public While(String name) {
		super(name, WHILE);
	}

	public While setCondition(String condition) {
		select();
		openProperties().selectDetails().setCondition(condition);
		save();

		return this;
	}

}
