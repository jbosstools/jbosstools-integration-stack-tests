package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.core.handler.WidgetHandler;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.button.YesButton;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.dialog.CriteriaBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ExpressionBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ReconcilerDialog;
import org.jboss.tools.teiid.reddeer.matcher.ToolBarButtonWithLabel;

public class TransformationEditor {
	private static final Logger log = Logger.getLogger(TransformationEditor.class);
	
	public void insertAndValidateSql(String sql){
		setTransformation(sql);
		saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
        new ShellMenuItem(new WorkbenchShell(), "File", "Save").select();
		new WorkbenchShell();
	}
	
	public void setTransformation(String sql){
		new DefaultStyledText(0).setText(sql);
	}
	
	public void typeTransformation(String text) {
		new DefaultStyledText(0);
		KeyboardFactory.getKeyboard().type(text);
	}

	public void setCoursorPositionInTransformation(final int position){
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				DefaultStyledText styledText = new DefaultStyledText();
				styledText.selectPosition(position);
				int x = styledText.getCursorPosition().x;
				int y = styledText.getCursorPosition().y;
				WidgetHandler handler = WidgetHandler.getInstance();
				handler.notifyItemMouse(5, 0, styledText.getSWTWidget(), null, x, y, 0);
				handler.notifyItemMouse(3, 0, styledText.getSWTWidget(), null, x, y, 1);
				handler.notifyItemMouse(4, 0, styledText.getSWTWidget(), null, x, y, 1);			
			}
		});	
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
	
    public void supportsUpdate(final boolean toggle) {
        log.info(((toggle) ? "checking" : "unchecking") + "Supports Update");
        Display.syncExec(new Runnable() {
            @Override
            public void run() {
                new DefaultToolItem(new ToolBarButtonWithLabel("Supports Update", toggle));
            }
        });
        AbstractWait.sleep(TimePeriod.SHORT);
		try {
			new DefaultShell("Table 'Supports Update' Property Changed");
			new YesButton().click();
		} catch(SWTLayerException e){
			// shell not opened -> continue
		}
	}

}
