package org.jboss.tools.fuse.reddeer.component;

import org.jboss.tools.fuse.reddeer.utils.CamelComponentUtils;

/**
 * 
 * @author apodhrad
 *
 */
public abstract class AbstractURICamelComponent implements CamelComponent {

	@Override
	public String getLabel() {
		return CamelComponentUtils.getLabel(getUri());
	}

	@Override
	public String getTooltip() {
		return null;
	}

	public abstract String getUri();

}
