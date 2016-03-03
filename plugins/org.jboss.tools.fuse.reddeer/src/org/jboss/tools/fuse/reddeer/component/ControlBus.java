package org.jboss.tools.fuse.reddeer.component;

public class ControlBus implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "Control Bus";
	}

	@Override
	public String getLabel() {
		return "controlbus:comman...";
	}

	@Override
	public String getTooltip() {
		return "The Control Bus component allows sending messages to a control-bus endpoint to control the lifecycle of routes";
	}

}
