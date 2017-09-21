package org.jboss.tools.teiid.reddeer.wizard.newWizard;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.teiid.reddeer.dialog.ProcedureDialog;
import org.jboss.tools.teiid.reddeer.dialog.UserDefinedFunctionDialog;

public class NewProcedureWizard {
	
	public static ProcedureDialog createViewProcedure(){
		new DefaultShell("Select Procedure Type");
		new RadioButton("Procedure").click();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		return new ProcedureDialog(true);
	}
	
	public static ProcedureDialog createSourceProcedure() {
		new DefaultShell("Select Procedure Type");
		new RadioButton("Procedure").click();
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		return new ProcedureDialog(false);
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
