package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * JPA binding page
 * 
 * @author apodhrad
 * 
 */
public class JPABindingPage extends OperationOptionsPage<JPABindingPage> {

	public LabeledText getNativeQuery() {
		return new LabeledText(new DefaultGroup("Consumer Options"), "Native Query");
	}

	public LabeledText getNamedQuery() {
		return new LabeledText(new DefaultGroup("Consumer Options"), "Named Query");
	}

	public LabeledText getQuery() {
		return new LabeledText(new DefaultGroup("Consumer Options"), "Query");
	}

	public LabeledText getMaximumResults() {
		return new LabeledText(new DefaultGroup("Consumer Options"), "Maximum Results");
	}

	public LabeledText getTransactionManager() {
		return new LabeledText("Transaction Manager");
	}

	public LabeledText getPersistenceUnit() {
		return new LabeledText("Persistence Unit*");
	}

	public LabeledText getEntityClassName() {
		return new LabeledText("Entity Class Name*");
	}

	public CheckBox getTransacted() {
		return new CheckBox(new DefaultGroup("Consumer Options"), "Transacted");
	}

	public CheckBox getLockEntity() {
		return new CheckBox(new DefaultGroup("Consumer Options"), "Lock Entity");
	}

	public CheckBox getDelete() {
		return new CheckBox(new DefaultGroup("Consumer Options"), "Delete");
	}

	public CheckBox getUsePersist() {
		return new CheckBox(new DefaultGroup("Producer Options"), "Use Persist");
	}

	public CheckBox getFlushonSend() {
		return new CheckBox(new DefaultGroup("Producer Options"), "Flush on Send");
	}
}
