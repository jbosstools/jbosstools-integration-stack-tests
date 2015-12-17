package org.jboss.tools.teiid.reddeer.modeling;

import java.util.Collections;
import java.util.List;

import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;

public class ModelTable {
	private String name;
	private String nameInSource;
	private List<ModelColumn> columns = Collections.emptyList();
	
	public ModelTable() {
	}
	
	public ModelTable(TableItem it){
		Table parent = it.getParent();
		name = it.getText(parent.getHeaderIndex("Name"));
		nameInSource = it.getText(parent.getHeaderIndex("Name In Source"));
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

	public List<ModelColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ModelColumn> columns) {
		this.columns = columns;
	}
	
	
}
