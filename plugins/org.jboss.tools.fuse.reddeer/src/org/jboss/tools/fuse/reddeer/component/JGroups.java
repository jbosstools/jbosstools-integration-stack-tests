package org.jboss.tools.fuse.reddeer.component;

public class JGroups implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "JGroups";
	}

	@Override
	public String getLabel() {
		return "jgroups:clusterName";
	}

	@Override
	public String getTooltip() {
		return "Component providing support for messages multicasted from- or to JGroups channels (org.jgroups.Channel).";
	}

}
