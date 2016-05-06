package org.jboss.tools.teiid.reddeer.editor;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

public class Reconciler {
	public static final String DIALOG_TITLE = "Reconcile Virtual Target Columns";

	public static class ResolverType {
		public static final String KEEP_VIRTUAL_TARGET = "Convert all source SQL symbol datatypes";
		public static final String KEEP_SQL_SYMBOLS = "Change all target column datatypes";
	}

	public Reconciler() {
		activate();
	}

	public Reconciler activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	/**
	 * @param left - virtual target column
	 * @param right - unmatched SQL symbol
	 */
	public void bindAttributes(String left, String right) {
		activate();
		new DefaultTable(0).deselectAll();
		new DefaultTable(1).deselectAll();
		new DefaultTable().select(left);
		new DefaultTable(1).select(right);
		new PushButton("< Bind").click();
	}

	public void addNewVirtualTargetAttribute(String unmatchedSQLSymbol) {
		activate();
		new DefaultTable(1).select(unmatchedSQLSymbol);
		new PushButton("< New").click();
	}
	
	public ExpressionBuilder openExpressionBuilder(String attribute){
		new DefaultTable(0).deselectAll();
		new DefaultTable(0).select(attribute);
		return new ExpressionBuilder(true);
	}

	public void clearRemainingUnmatchedSymbols() {
		activate();
		new PushButton("Clear").click();
	}

	/**
	 * Closes and saves the reconciler
	 */
	public void close() {
		activate();
		new PushButton("OK").click();
	}

	/**
	 * 
	 * @param resolveType - Reconciler.ResolverType..KEEP_VIRTUAL_TARGET|KEEP_SQL_SYMBOLS
	 */
	public void resolveTypes(String resolveType) {
		activate();
		new PushButton("Type Resolver...").click();
		new PushButton(resolveType).click();
		new PushButton("OK").click();
	}
}
