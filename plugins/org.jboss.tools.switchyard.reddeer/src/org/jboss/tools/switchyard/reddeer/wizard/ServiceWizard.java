package org.jboss.tools.switchyard.reddeer.wizard;

import org.hamcrest.core.IsNull;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * 
 * @author apodhrad
 * 
 */
public class ServiceWizard<T extends ServiceWizard<?>> extends WizardDialog {

	private String dialogTitle;

	public ServiceWizard() {
		super();
	}

	public ServiceWizard(String dialogTitle) {
		super();
		this.dialogTitle = dialogTitle;
	}

	@SuppressWarnings("unchecked")
	public T activate() {
		new WaitWhile(new JobIsRunning());
		new DefaultShell(dialogTitle).setFocus();
		AbstractWait.sleep(TimePeriod.SHORT);
		return (T) this;
	}

	public T setServiceName(String serviceName) {
		new LabeledText("Service Name:").setText(serviceName);
		return activate();
	}

	public T checkInterfaceType(String interfaceType) {
		AbstractWait.sleep(TimePeriod.SHORT);
		new RadioButton(interfaceType).click();
		AbstractWait.sleep(TimePeriod.SHORT);
		return activate();
	}

	public T selectJavaInterface(String javaInterface) {
		checkInterfaceType("Java").browse();
		new DefaultShell("");
		new DefaultText().setText(javaInterface);
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.LONG);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(new IsNull<String>()));
		return activate();
	}

	public T createJavaInterface(String javaInterface) {
		activate().checkInterfaceType("Java").clickInterface();
		new JavaInterfaceWizard().activate().setName(javaInterface).finish();
		return activate();
	}

	public T selectWSDLInterface(String wsdlInterface) {
		checkInterfaceType("WSDL").browse();
		new DefaultText().setText(wsdlInterface);
		clickOK();
		return activate();
	}

	public T createWSDLInterface(String wsdlInterface) {
		checkInterfaceType("WSDL").clickInterface();
		new Java2WSDLWizard().activate().finish();
		return activate();
	}

	public void clickInterface() {
		new DefaultLink("Interface:").click();
	}

	private void clickOK() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	protected void browse() {
		throw new UnsupportedOperationException("You have to implement browse() method!");
	}

}
