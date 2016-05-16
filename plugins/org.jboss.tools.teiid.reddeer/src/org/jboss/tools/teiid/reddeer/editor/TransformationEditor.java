package org.jboss.tools.teiid.reddeer.editor;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.dialog.CriteriaBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ExpressionBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ReconcilerDialog;

public class TransformationEditor {
	private static final Logger log = Logger.getLogger(TransformationEditor.class);
	
	public void insertAndValidateSql(String sql){
		setTransformation(sql);
		saveAndValidateSql(sql);
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
	}
	
	public void setTransformation(String sql){
		new DefaultStyledText(0).setText(sql);
	}
	
	public void saveAndValidateSql(String sql){
		new DefaultToolItem("Save/Validate SQL").click();
	}
	
	public void close() {
		new DefaultToolItem("Close").click();
	}
	
	public CriteriaBuilderDialog openCriteriaBuilder() {
		log.info("Opening Criteria Builder Dialog");
		new DefaultToolItem("Criteria Builder").click();
		return new CriteriaBuilderDialog();
	}
	
	public ExpressionBuilderDialog openExpressionBuilder(){
		log.info("Opening Expression Builder Dialog");
		new DefaultToolItem("Expression Builder").click();
		return new ExpressionBuilderDialog();
	}
	
	public ReconcilerDialog openReconciler() {
		log.info("Opening Reconciler Dialog");
		new DefaultToolItem("Reconcile Transformation SQL with Target Columns").click();
		return new ReconcilerDialog();
	}

}
