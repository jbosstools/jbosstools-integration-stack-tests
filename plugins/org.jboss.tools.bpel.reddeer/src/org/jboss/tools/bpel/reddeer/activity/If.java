package org.jboss.tools.bpel.reddeer.activity;

/**
 * 
 * @author apodhrad
 * 
 */
public class If extends ContainerActivity {

	public If(String name) {
		super(name, IF);
	}

	public If setCondition(String condition) {
		select();
		openProperties().selectDetails().setCondition(condition);
		save();

		return this;
	}

	public ElseIf addElseIf() {
		menu("Add ElseIf");
		return new ElseIf(this);
	}

	public Else addElse() {
		menu("Add Else");
		return new Else(this);
	}
}
