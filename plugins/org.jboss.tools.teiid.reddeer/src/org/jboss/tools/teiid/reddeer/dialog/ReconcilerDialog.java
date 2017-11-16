package org.jboss.tools.teiid.reddeer.dialog;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.teiid.reddeer.matcher.ButtonWithToolTipMatcher;

public class ReconcilerDialog extends AbstractDialog {
	private static final Logger log = Logger.getLogger(ReconcilerDialog.class);
	
	public static final String DIALOG_TITLE = "Reconcile Virtual Target Columns";

	public static class ResolverType {
		public static final String KEEP_VIRTUAL_TARGET = "Convert all source SQL symbol datatypes";
		public static final String KEEP_SQL_SYMBOLS = "Change all target column datatypes";
	}

	public ReconcilerDialog() {
		super("Reconcile Virtual Target Columns");
	}
	
	@Override
	public void finish() {
		log.info("Finishing '" + title + "' Dialog");
		new PushButton("OK").click();
		new WaitWhile(new ShellIsActive(title), TimePeriod.DEFAULT);
		
	}

	/**
	 * Binds specified attributes.
	 * @param left - virtual target column
	 * @param right - unmatched SQL symbol
	 */
	public ReconcilerDialog bindAttributes(String left, String right) {
		log.info("Binding unmatched '" + right + "' to virtual '" + left + "'");
		new DefaultTable(0).deselectAll();
		new DefaultTable(1).deselectAll();
		new DefaultTable().select(left);
		new DefaultTable(1).select(right);
		new PushButton("< Bind").click();
		return this;
	}

	/**
	 * Creates new virtual attribute.
	 * i.e. pushes attribute from the right list to the left list.
	 */
	public ReconcilerDialog addNewVirtualTargetAttribute(String unmatchedSQLSymbol) {
		log.info("Adding new virtual attribute '" + unmatchedSQLSymbol + "'");
		new DefaultTable(1).select(unmatchedSQLSymbol);
		new PushButton("< New").click();
		return this;
	}
	
	/**
	 * Opens expression builder dialog for specified attribute.
	 */
	public ExpressionBuilderDialog openExpressionBuilder(String attribute){
		log.info("Opening Expression Builder Dialog for '" + attribute + "' attribute");
		new DefaultTable(0).deselectAll();
		new DefaultTable(0).select(attribute);
        Display.syncExec(new Runnable() {
            @Override
            public void run() {
                new PushButton(new ButtonWithToolTipMatcher("Create Expression")).click();
            }
        });
		return new ExpressionBuilderDialog();
	}

	/**
	 * Clears unmatched SQL symbols
	 */
	public ReconcilerDialog clearRemainingUnmatchedSymbols() {
		log.info("Clearing unmatched SQL symbols");
        Display.syncExec(new Runnable() {
            @Override
            public void run() {
                new PushButton(new ButtonWithToolTipMatcher("Clear")).click();
            }
        });
		return this;
	}

	/**
	 * Resolves data types according to specified resolver type.
	 * @param resolveType - Reconciler.ResolverType..KEEP_VIRTUAL_TARGET|KEEP_SQL_SYMBOLS
	 */
	public ReconcilerDialog resolveTypes(String resolveType) {
		log.info("Resolving data types - action: " + resolveType);
		new PushButton("Type Resolver...").click();
		new PushButton(resolveType).click();
		new PushButton("OK").click();
		return this;
	}
}
