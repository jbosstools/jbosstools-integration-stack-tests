package org.jboss.tools.teiid.reddeer.model;

import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;

public class ModelProcedure {
	private String name;
	private String nameInSource;
	private String function;
	private String restMethod;
	private String restUri;

	public ModelProcedure() {
	}

	public ModelProcedure(TableItem it) {
		Table parent = it.getParent();
		name = it.getText(parent.getHeaderIndex("Name"));
		nameInSource = it.getText(parent.getHeaderIndex("Name In Source"));
		function = it.getText(parent.getHeaderIndex("Function"));
		restMethod = it.getText(parent.getHeaderIndex("REST:Rest Method"));
		restUri = it.getText(parent.getHeaderIndex("REST:URI"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameInSource() {
		return nameInSource;
	}

	public void setNameInSource(String nameInSource) {
		this.nameInSource = nameInSource;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getRestMethod() {
		return restMethod;
	}

	public void setRestMethod(String restMethod) {
		this.restMethod = restMethod;
	}

	public String getRestUri() {
		return restUri;
	}

	public void setRestUri(String restUri) {
		this.restUri = restUri;
	}

}
