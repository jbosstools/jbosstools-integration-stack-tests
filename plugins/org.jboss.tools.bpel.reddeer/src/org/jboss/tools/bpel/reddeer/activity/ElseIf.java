package org.jboss.tools.bpel.reddeer.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class ElseIf extends ContainerActivity {

	public ElseIf(If parent) {
		this(parent, 0);
	}

	public ElseIf(If parent, int index) {
		super(null, "ElseIf", parent, index);
	}

	public ElseIf setCondition(String condition) {
		select();
		openProperties().selectDetails().setCondition(condition);
		save();

		return this;
	}

}
