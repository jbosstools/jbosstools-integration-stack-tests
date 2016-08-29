package org.jboss.tools.teiid.reddeer.editor;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.dialog.CriteriaBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ExpressionBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ReconcilerDialog;
import org.jboss.tools.teiid.reddeer.widget.TeiidStyledText;

public class TransformationEditor {
	private static final Logger log = Logger.getLogger(TransformationEditor.class);
	
	public void insertAndValidateSql(String sql){
		setTransformation(sql);
		saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
	}
	
	public void setTransformation(String sql){
		new DefaultStyledText(0).setText(sql);
	}
	
	public void typeTransformation(String text) {
		new DefaultStyledText(0);
		KeyboardFactory.getKeyboard().type(text);
	}

	public void setCoursorPositionInTransformation(int position){
		new DefaultStyledText(0).selectPosition(position);
		new TeiidStyledText(0).mouseClickOnCaret();
	}
	
	public String getTransformation(){
		return new DefaultStyledText(0).getText();
	}
	
	public void saveAndValidateSql(){
		new DefaultToolItem("Save/Validate SQL").click();
	}
	
	public void close() {
		new DefaultToolItem("Close").click();
		AbstractWait.sleep(TimePeriod.SHORT);
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
	
	public void expandSelect(){
		log.info("Expanding Select");
		new DefaultToolItem("Expand SELECT * ").click();	
		AbstractWait.sleep(TimePeriod.SHORT);
	}

}