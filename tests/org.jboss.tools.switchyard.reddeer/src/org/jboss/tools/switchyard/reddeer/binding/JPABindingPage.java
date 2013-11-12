package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * JPA binding page
 * 
 * @author apodhrad
 * 
 */
public class JPABindingPage extends WizardPage {

	public static final String NAME = "Name";
	public static final String ENTITY_CLASS_NAME = "Entity Class Name*";
	public static final String PERSITENCE_UNIT = "Persistence Unit*";

	public JPABindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public JPABindingPage setEntityClassName(String name) {
		new LabeledText(ENTITY_CLASS_NAME).setFocus();
		new LabeledText(ENTITY_CLASS_NAME).setText(name);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getEntityClassName() {
		return new LabeledText(ENTITY_CLASS_NAME).getText();
	}

	public JPABindingPage setPersistenceUnit(String persistenceUnit) {
		new LabeledText(PERSITENCE_UNIT).setFocus();
		new LabeledText(PERSITENCE_UNIT).setText(persistenceUnit);
		return this;
	}

	public String getPersistenceUnit() {
		return new LabeledText(PERSITENCE_UNIT).getText();
	}
}
