package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.teiid.reddeer.dialog.ProcedureViewDialog;
import org.jboss.tools.teiid.reddeer.dialog.UserDefinedFunctionDialog;

public class ProcedureWizard {
	
	public static ProcedureViewDialog createViewProcedure(){
		new DefaultShell("Select Procedure Type");
		new RadioButton("Procedure").click();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		return new ProcedureViewDialog();
	}
	
	public static void  createSourceProcedure() {
		new DefaultShell("Select Procedure Type");
		new RadioButton("Procedure").click();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		//return new ProcedureSourceDialog();
	}
	
	public static UserDefinedFunctionDialog createUserDefinedFunction(){
		new DefaultShell("Select Procedure Type");
		new RadioButton("User Defined Function").click();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		return new UserDefinedFunctionDialog();
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
