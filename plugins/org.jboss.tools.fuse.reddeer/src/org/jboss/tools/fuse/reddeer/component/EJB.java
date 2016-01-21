package org.jboss.tools.fuse.reddeer.component;

public class EJB implements CamelComponent {

	@Override
	public String getPaletteEntry() {
		return "EJB";
	}

	@Override
	public String getLabel() {
		return "ejb:beanName";
	}

	@Override
	public String getTooltip() {
		return "EJB component to invoke EJBs like the org.apache.camel.component.bean.BeanComponent.";
	}

}
