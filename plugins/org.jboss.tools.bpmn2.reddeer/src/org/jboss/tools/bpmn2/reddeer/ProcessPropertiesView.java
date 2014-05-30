package org.jboss.tools.bpmn2.reddeer;

import org.jboss.tools.reddeer.DefaultPropertiesView;

/**
 * 
 */
public class ProcessPropertiesView extends DefaultPropertiesView {

	/**
	 * 
	 */
	public ProcessPropertiesView() {
	}
	
	/**
	 * 
	 * @param index
	 */
	public void selectTab(int index) {
		setSelectionIndex(index);
	}
	
	/**
	 * 
	 * @param tabClass
	 * @param index
	 * @return
	 */
	public <T> T getTab(int index, Class<T> asType) {
		selectTab(index);
		return getSelectedTab(asType);
	}

	/**
	 * 
	 * @param tabClass
	 * @param label
	 * @return
	 */
	public <T> T getTab(String label, Class<T> asType) {
		selectTab(label);
		return getSelectedTab(asType);
	}
	
	/**
	 * 
	 * @param asType
	 * @return
	 */
	public <T> T getSelectedTab(Class<T> asType) {
		int selectionIndex = getSelectionIndex();
		if (selectionIndex > -1) {
			try {
				return (T) asType.getConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Could not create an instance of type '" + asType + "'", e);
			}
		}
		return null;
	}
	
}
