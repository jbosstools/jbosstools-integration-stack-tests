package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.teiid.reddeer.dialog.ViewProcedureDialog;

public class ProcedureWizard {
	
	public static ViewProcedureDialog createViewProcedure(){
		new DefaultShell("Select Procedure Type");
		new RadioButton("Procedure").click();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		return new ViewProcedureDialog();
	}
	
	public static void  createSourceProcedure() {
		new DefaultShell("Select Procedure Type");
		new RadioButton("Procedure").click();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		//return new SourceProcedureDialog();
	}
	
	public static void createUserDefinedFunction(){
		new DefaultShell("Select Procedure Type");
		new RadioButton("User Defined Function").click();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		//return new UserDefinedFunctionDialog();
	}
	
	public static void createSourceFunction(){
		new DefaultShell("Select Procedure Type");
		new RadioButton("Source Function").click();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		//return new SourceFunctionDialog();
	}
	
	public static void createNativeQueryProcedure(){
		new DefaultShell("Select Procedure Type");
		new RadioButton("Native Query Procedure").click();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		//return new NativeQueryProcedureDialog();
	}
	
}
