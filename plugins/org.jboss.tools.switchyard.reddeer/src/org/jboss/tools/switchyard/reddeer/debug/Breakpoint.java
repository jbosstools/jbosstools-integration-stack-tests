package org.jboss.tools.switchyard.reddeer.debug;

import java.util.List;

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * SwitchYard breakpoint
 * 
 * @author apodhrad
 * 
 */
public class Breakpoint extends DefaultTreeItem {

	public enum TriggeringPhase {

		IN("IN"), OUT("OUT"), FAULT("FAULT");

		private String label;

		private TriggeringPhase(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

	}

	public enum TriggerOn {

		ENTRY("ENTRY"),
		RETURN("RETURN"),
		FAULT("FAULT"),
		TRANSACTION("TRANSACTION"),
		SECURITY("SECURITY"),
		POLICY("POLICY"),
		TARGET_INVOCATION("TARGET_INVOCATION"),
		VALIDATION("VALIDATION"),
		TRANSFORMATION("TRANSFORMATION");

		private String label;

		private TriggerOn(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

	}

	public Breakpoint(String label) {
		super(label);
	}

	public void check(TriggeringPhase triggeringPhase) {
		check(triggeringPhase.getLabel(), true);
	}

	public void uncheck(TriggerOn triggerOn) {
		check(triggerOn.getLabel(), false);
	}

	public void uncheck(TriggeringPhase triggeringPhase) {
		check(triggeringPhase.getLabel(), false);
	}

	public void check(TriggerOn triggerOn) {
		check(triggerOn.getLabel(), true);
	}

	public void check(String trigger, boolean checked) {
		select();
		new CheckBox(trigger).toggle(checked);
	}

	public boolean isChecked(TriggeringPhase triggeringPhase) {
		return isChecked(triggeringPhase.getLabel());
	}

	public boolean isChecked(TriggerOn triggerOn) {
		return isChecked(triggerOn.getLabel());
	}

	public boolean isChecked(String label) {
		select();
		return new CheckBox(label).isChecked();
	}

	public List<TableItem> getTransformers() {
		select();
		return new DefaultTable().getItems();
	}

	public void checkTransformer(String from, String to) {
		setChecked(true, getTransformers(), from, to);
	}

	public void uncheckTransformer(String from, String to) {
		setChecked(false, getTransformers(), from, to);
	}

	public List<TableItem> getValidators() {
		select();
		return new DefaultTable().getItems();
	}

	public void checkValidator(String name) {
		setChecked(true, getValidators(), name);
	}

	public void uncheckValidator(String name) {
		setChecked(false, getValidators(), name);
	}

	private void setChecked(boolean checked, List<TableItem> tableItems, String... value) {
		for (TableItem tableItem : tableItems) {
			boolean result = true;
			for (int i = 0; i < value.length; i++) {
				if (!value[i].equals(tableItem.getText(i))) {
					result = false;
				}
			}
			if (result) {
				tableItem.setChecked(checked);
			}
		}
	}

	public void delete() {
		select();
		new BreakpointsView().removeSelectedBreakpoints();
	}
}
