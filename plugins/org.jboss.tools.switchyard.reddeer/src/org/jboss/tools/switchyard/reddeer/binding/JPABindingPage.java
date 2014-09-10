package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * JPA binding page
 * 
 * @author apodhrad
 * 
 */
public class JPABindingPage extends OperationOptionsPage<JPABindingPage> {

	public static final String ENTITY_CLASS_NAME = "Entity Class Name*";
	public static final String PERSITENCE_UNIT = "Persistence Unit*";

	public JPABindingPage setEntityClassName(String name) {
		new LabeledText(ENTITY_CLASS_NAME).setFocus();
		new LabeledText(ENTITY_CLASS_NAME).setText(name);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getEntityClassName() {
		return new LabeledText(ENTITY_CLASS_NAME).getText();
	}

	public JPABindingPage setPersistenceUnit(String persistenceUnit) {
		new LabeledText(PERSITENCE_UNIT).setFocus();
		new LabeledText(PERSITENCE_UNIT).setText(persistenceUnit);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getPersistenceUnit() {
		return new LabeledText(PERSITENCE_UNIT).getText();
	}

}
