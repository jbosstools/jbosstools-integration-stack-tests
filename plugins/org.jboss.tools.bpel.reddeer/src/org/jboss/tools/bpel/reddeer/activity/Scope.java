package org.jboss.tools.bpel.reddeer.activity;

import org.jboss.reddeer.swt.impl.button.CheckBox;

/**
 * 
 * @author apodhrad
 * 
 */
public class Scope extends ContainerActivity {

	public Scope(String name) {
		this(name, null);
	}

	public Scope(String name, Activity parent) {
		super(name, SCOPE, parent, 0);
	}

	public Scope checkIsIsolated() {
		select();
		openProperties().selectDetails();
		new CheckBox("Isolated").toggle(true);
		save();

		return this;
	}

	public FaultHandler addFaultHandler() {
		menu("Add Fault Handler");
		return new FaultHandler();
	}

	public CompensationHandler addCompensationHandler() {
		menu("Add Compensation Handler");
		return new CompensationHandler(this);
	}

	public void addTerminationHandler() {
		menu("Add Termination Handler");
	}

	public void addEventHandler() {
		menu("Add Event Handler");
	}

}
