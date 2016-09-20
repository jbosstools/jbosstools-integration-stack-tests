package org.jboss.tools.teiid.reddeer.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
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

public class DataRolesDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(DataRolesDialog.class);

	public class PermissionType {
		public static final String CREATE = "Create"; 
		public static final String READ = "Read";
		public static final String UPDATE = "Update";
		public static final String DELETE ="Delete"; 
		public static final String EXECUTE = "Execute";
		public static final String ALTER  = "Alter";
	}

	public static final String CREATE_TITLE = "New VDB Data Role";
	public static final String EDIT_TITLE = "Edit VDB Data Role";

	public DataRolesDialog(String title) {
		super(title);
		activate();
	}
	
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new FinishButton().click();
		new WaitWhile(new ShellWithTextIsActive(title), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	public DataRolesDialog setName(String name) {
		log.info("Setting data role name to '" + name + "'");
		new LabeledText("Name").setText(name);
		return this;
	}

	public DataRolesDialog addRole(String roleName) {
		new DefaultCTabItem("Mapped Enterprise Role or Group").activate();
		new PushButton("Add...").click();
		new DefaultShell("Add Mapped Data Role Name");
		new DefaultText(new DefaultGroup("Name"), 0).setText(roleName);
		new PushButton("OK").click();
		activate();
		return this;
	}

	/**
	 * @param permType DataRolesDialog.PermissionType.*
	 */
	public boolean getModelPermission(String permType, final String... path) {
		new DefaultCTabItem("Permissions").activate();
		new DefaultCTabItem("Model").activate();
		
		final int column = getColumnForPermission(permType);
		return Display.syncExec(new ResultRunnable<Boolean>() {
			@Override
			public Boolean run() {
				int checkPixel = new DefaultTreeItem(path).getSWTWidget().getImage(column).getImageData().getPixel(4, 9);
				switch (checkPixel) {
				case 0x818181:  // PARTIAL - FEDORA
				case 0x808080: // PARTIAL - MAC
				case 0x818181e6: // PARTIAL - WIN
				case 0x00:  // TRUE
					return true;
				case 0xdedddd: // UNAVAILABLE
				case 0x8cbdef: // FALSE
					return false;
				default:
					throw new RuntimeException("unknown check state (designer changed checkbox images?) Color is: " + Integer.toHexString(checkPixel) );
				}
			}
		});
	}

	/**
	 * @param permType DataRolesDialog.PermissionType.*
	 */
	public DataRolesDialog setModelPermission(String permType, final boolean allowed, final String... path) {
		new DefaultCTabItem("Permissions").activate();
		new DefaultCTabItem("Model").activate();
		
		
		new DefaultTreeItem(path).select();
		
		final int column = getColumnForPermission(permType);
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				int checkPixel = new DefaultTreeItem(path).getSWTWidget().getImage(column).getImageData().getPixel(4, 9);
				boolean checked;
				switch (checkPixel) {
				case 0x818181:  // PARTIAL
				case 0x00:  // TRUE
					checked = true;
					break;
				case 0xdedddd: // UNAVAILABLE
				case 0x8cbdef: // FALSE
					checked = false;
					break;
				default:
					throw new RuntimeException("unknown check state (designer changed checkbox images?) Color is: " + Integer.toHexString(checkPixel));
				}
				
				Rectangle bounds = new DefaultTreeItem(path).getSWTWidget().getBounds(column);
				if (checked != allowed) {
					WidgetHandler.getInstance().notifyItemMouse(SWT.MouseDown, 0, new DefaultTreeItem(path).getParent().getSWTWidget(), null,
							bounds.x + 1, bounds.y + 1, 1);
				}
			}
		});
		return this;
	}

	public DataRolesDialog addRowFilter(String condition, boolean constraint, String... target) {
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

	public DataRolesDialog addColumnMask(String condition, String columnExpression, int order, String... target) {
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

	/**
	 * @param permType DataRolesDialog.PermissionType.*
	 */
	private int getColumnForPermission(String permType) {
		return new DefaultTree().getHeaderColumns().indexOf(permType);
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
