package org.jboss.tools.switchyard.reddeer.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.reddeer.gef.api.EditPart;
import org.jboss.reddeer.gef.condition.EditorHasEditParts;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.graphiti.impl.graphitieditpart.LabeledGraphitiEditPart;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.switchyard.reddeer.wizard.BPMServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.BeanServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.CamelJavaWizard;
import org.jboss.tools.switchyard.reddeer.wizard.DroolsServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ReferenceWizard;

/**
 * 
 * @author apodhrad
 *
 */
public class SwitchYardEditor extends GEFEditor {

	public static final String TITLE = "switchyard.xml";
	public static final String TOOL_COMPONENT = "Component";
	public static final String TOOL_SERVICE = "Service";
	public static final String TOOL_REFERENCE = "Reference";
	public static final String TOOL_BEAN = "Bean";
	public static final String TOOL_CAMEL_JAVA = "Camel (Java)";
	public static final String TOOL_CAMEL_XML = "Camel (XML)";
	public static final String TOOL_BPEL = "Process (BPEL)";
	public static final String TOOL_BPMN = "Process (BPMN)";
	public static final String TOOL_RULES = "Rules";

	protected File sourceFile;
	protected CompositeEditPart composite;

	public SwitchYardEditor() {
		super(TITLE);
		composite = getCompositeEditPart();
	}

	public CompositeEditPart getCompositeEditPart() {
		String compositeName = getCompositeName();
		return new CompositeEditPart(compositeName);
	}

	public String getCompositeName() {
		File sourceFile = getSourceFile();
		try {
			XPathEvaluator xpath = new XPathEvaluator(new FileReader(sourceFile));
			String name = xpath.evaluateString("/switchyard/@name");
			return name;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void autoLayout() {
		composite.getContextButton("Auto-layout Diagram").click();
		save();
	}

	public void addComponent() {
		int oldCount = getNumberOfEditParts();
		getPalette().activateTool(TOOL_COMPONENT);
		composite.click();
		new WaitUntil(new EditorHasEditParts(this, oldCount));
	}

	public void addService() {
		getPalette().activateTool(TOOL_SERVICE);
		composite.click();
	}

	public ReferenceWizard addReference() {
		getPalette().activateTool(TOOL_REFERENCE);
		composite.click();
		return new ReferenceWizard();
	}

	public BeanServiceWizard addBeanImplementation() {
		return addBeanImplementation(composite);
	}

	public BeanServiceWizard addBeanImplementation(EditPart editPart) {
		addTool(TOOL_BEAN, editPart);
		return new BeanServiceWizard();
	}

	public CamelJavaWizard addCamelJavaImplementation() {
		return addCamelJavaImplementation(composite);
	}

	public CamelJavaWizard addCamelJavaImplementation(EditPart editPart) {
		addTool(TOOL_CAMEL_JAVA, editPart);
		return new CamelJavaWizard(this);
	}

	public void addCamelXmlImplementation() {

	}

	public void addBPELImplementation() {
		addBPELImplementation(composite);
	}

	public void addBPELImplementation(EditPart editPart) {
		getPalette().activateTool(TOOL_BPEL);
		editPart.click();
	}

	public BPMServiceWizard addBPMNImplementation() {
		return addBPMNImplementation(composite);
	}

	public BPMServiceWizard addBPMNImplementation(EditPart editPart) {
		addTool(TOOL_BPMN, editPart);
		return new BPMServiceWizard();
	}

	public DroolsServiceWizard addDroolsImplementation() {
		return addDroolsImplementation(composite);
	}

	public DroolsServiceWizard addDroolsImplementation(EditPart editPart) {
		addTool(TOOL_RULES, editPart);
		return new DroolsServiceWizard();
	}

	protected void addTool(String tool, EditPart editPart) {
		getPalette().activateTool(tool);
		editPart.click();
	}

	public String xpath(String expr) throws FileNotFoundException {
		XPathEvaluator xpath = new XPathEvaluator(new FileReader(getSourceFile()));
		String result = xpath.evaluateString(expr);
		return result;
	}

	protected File getSourceFile() {
		if (sourceFile == null) {
			IEditorInput editorInput = editorPart.getEditorInput();
			if (editorInput instanceof FileEditorInput) {
				FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
				sourceFile = fileEditorInput.getPath().toFile();
			}
		}
		return sourceFile;
	}

	private class CompositeEditPart extends LabeledGraphitiEditPart {

		public CompositeEditPart(String label) {
			super(label);
		}

		public Rectangle getBounds() {
			IFigure figure = super.getFigure();
			return figure.getBounds();
		}
	}

	public void saveAndClose() {
		super.close(true);
	}

}
