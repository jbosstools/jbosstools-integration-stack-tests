package org.jboss.tools.teiid.reddeer.editor;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.widget.PermissionWizardTreeItem;

public class DataRolesEditor {

	public enum PermissionType {
		CREATE("Create"), READ("Read"), UPDATE("Update"), DELETE("Delete"), EXECUTE("Execute"), ALTER("Alter");

		private String header;

		PermissionType(String header) {
			this.header = header;
		}

		public String getHeader() {
			return header;
		}
	}

	public static final String CREATE_TITLE = "New VDB Data Role";
	public static final String EDIT_TITLE = "Edit VDB Data Role";

	private String title;

	public DataRolesEditor(String title) {
		this.title = title;
		activate();
	}

	private DataRolesEditor activate() {
		new DefaultShell(title);
		return this;
	}

	public DataRolesEditor setName(String name) {
		new LabeledText("Name").setText(name);
		return this;
	}

	public DataRolesEditor addRole(String roleName) {
		new DefaultCTabItem("Mapped Enterprise Role or Group").activate();
		new PushButton("Add...").click();
		new DefaultShell("Add Mapped Data Role Name");
		new DefaultText(new DefaultGroup("Name"), 0).setText(roleName);
		new PushButton("OK").click();
		activate();
		return this;
	}

	public boolean getModelPermission(PermissionType permType, String... path) {
		new DefaultCTabItem("Permissions").activate();
		new DefaultCTabItem("Model").activate();
		PermissionWizardTreeItem columnItem = new PermissionWizardTreeItem(path);

		return columnItem.getChecked(getColumnForPermission(permType));
	}

	public DataRolesEditor setModelPermission(PermissionType permType, boolean allowed, String... path) {
		new DefaultCTabItem("Permissions").activate();
		new DefaultCTabItem("Model").activate();
		PermissionWizardTreeItem columnItem = new PermissionWizardTreeItem(path);
		columnItem.select();

		columnItem.setChecked(allowed, getColumnForPermission(permType));
		return this;
	}

	public DataRolesEditor addRowFilter(String condition, boolean constraint, String... target) {
		new DefaultCTabItem("Permissions").activate();
		new DefaultCTabItem("Row Filter").activate();
		new PushButton("Add").click();

		new DefaultShell("Add Row Filter Definition");
		new DefaultStyledText(new DefaultGroup("Condition")).setText(condition);
		new CheckBox("Constraint  >> When enabled, condition will be applied to insert/update/deletes")
				.toggle(constraint);

		new PushButton("...").click();
		new DefaultShell("Target Selection");
		new DefaultTreeItem(target).select();
		new PushButton("OK").click();

		new DefaultShell("Add Row Filter Definition");
		new PushButton("OK").click();

		return this;
	}

	public String getRowFilterCondition(String path) {
		return getRowFilterValue(path, 2);
	}

	public String getRowFilterConstraint(String path) {
		return getRowFilterValue(path, 1);
	}

	public String getRowFilterValue(String path, int index) {
		new DefaultCTabItem("Permissions").activate();
		new DefaultCTabItem("Row Filter").activate();
		TableItem item = new DefaultTable().getItem(path, 0);
		if (item != null) {
			return item.getText(index);
		}
		return "";
	}

	public DataRolesEditor addColumnMask(String condition, String columnExpression, int order, String... target) {
		new DefaultCTabItem("Permissions").activate();
		new DefaultCTabItem("Column Masking").activate();
		new PushButton("Add").click();

		new DefaultShell("Add Column Mask");
		new DefaultStyledText(new DefaultGroup("Condition")).setText(condition);
		new DefaultStyledText(new DefaultGroup("Column Expression")).setText(columnExpression);
		new LabeledText("Order").setText(String.valueOf(order));

		new PushButton("...").click();
		new DefaultShell("Target Selection");
		new DefaultTreeItem(target).select();
		new PushButton("OK").click();

		new DefaultShell("Add Column Mask");
		new PushButton("OK").click();

		return this;
	}

	public String getColumnMask(String column) {
		return getColumnMaskValue(column, 3);
	}

	public String getColumnMaskCondition(String column) {
		return getColumnMaskValue(column, 1);
	}

	private String getColumnMaskValue(String column, int index) {
		new DefaultCTabItem("Permissions").activate();
		new DefaultCTabItem("Column Masking").activate();

		TableItem item = new DefaultTable().getItem(column, 0);
		if (item != null) {
			return item.getText(index);
		}
		return "";
	}

	private int getColumnForPermission(PermissionType permType) {
		return new DefaultTree().getHeaderColumns().indexOf(permType.getHeader());
	}

	public void finish() {
		new PushButton("Finish").click();
	}

	public void cancel() {
		new PushButton("Cancel").click();
	}

	public List<String> getRoles() {
		new DefaultCTabItem("Mapped Enterprise Role or Group").activate();
		List<String> result = new ArrayList<String>();
		for (TableItem it : new DefaultTable().getItems()) {
			result.add(it.getText());
		}

		return result;
	}
}
