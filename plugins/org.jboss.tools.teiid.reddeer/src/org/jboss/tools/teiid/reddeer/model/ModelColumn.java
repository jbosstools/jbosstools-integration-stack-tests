package org.jboss.tools.teiid.reddeer.model;

import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;

public class ModelColumn {

	private String location;
	private String name;
	private String nameInSource;
	private String nativeType;
	private Integer length;
	private String datatype;
	private Integer numericPrecision;
	private Integer numericScale;

	public ModelColumn() {

	}

	public ModelColumn(TableItem it) {
		Table parent = it.getParent();
		location = it.getText(parent.getHeaderIndex("Location"));
		name = it.getText(parent.getHeaderIndex("Name"));
		nameInSource = it.getText(parent.getHeaderIndex("Name In Source"));
		nativeType = it.getText(parent.getHeaderIndex("Native Type"));
		length = Integer.valueOf(it.getText(parent.getHeaderIndex("Length")));
		numericPrecision = Integer.valueOf(it.getText(parent.getHeaderIndex("Numeric Precision")));
		numericScale = Integer.valueOf(it.getText(parent.getHeaderIndex("Numeric Scale")));
		datatype = it.getText(parent.getHeaderIndex("Datatype")).split(" :")[0];
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getNativeType() {
		return nativeType;
	}

	public void setNativeType(String nativeType) {
		this.nativeType = nativeType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public Integer getNumericPrecision() {
		return numericPrecision;
	}

	public void setNumericPrecision(Integer numericPrecision) {
		this.numericPrecision = numericPrecision;
	}

	public Integer getNumericScale() {
		return numericScale;
	}

	public void setNumericScale(Integer numericScale) {
		this.numericScale = numericScale;
	}

}
