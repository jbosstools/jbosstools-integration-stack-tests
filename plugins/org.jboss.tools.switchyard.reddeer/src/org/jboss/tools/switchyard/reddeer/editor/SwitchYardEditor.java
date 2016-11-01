package org.jboss.tools.switchyard.reddeer.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.graphiti.ui.platform.GraphitiShapeEditPart;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.handler.IBeforeShellIsClosed;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.direct.preferences.PreferencesUtil;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.perspectives.ResourcePerspective;
import org.jboss.reddeer.gef.api.EditPart;
import org.jboss.reddeer.gef.api.Palette;
import org.jboss.reddeer.gef.condition.EditorHasEditParts;
import org.jboss.reddeer.gef.editor.GEFEditor;
import org.jboss.reddeer.gef.handler.ViewerHandler;
import org.jboss.reddeer.gef.view.PaletteView;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComposite;
import org.jboss.tools.switchyard.reddeer.preference.CompositePropertiesPage;
import org.jboss.tools.switchyard.reddeer.wizard.BPELServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.BPMNServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.BeanServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.CamelJavaServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.CamelXMLServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.DroolsServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ExistingBPELServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ExistingBPMNServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ExistingCamelXMLServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ExistingDroolsServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ReferenceWizard;
import org.junit.Assert;

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

	private static Logger log = Logger.getLogger(SwitchYardEditor.class);
	private static Shell remainedShell = null;

	protected File sourceFile;
	protected SwitchYardComponent composite;

	public SwitchYardEditor() {
		super(activateDesignTab());
		composite = getComposite();
	}

	private static String activateDesignTab() {
		new DefaultEditor(TITLE);
		new DefaultCTabItem("Design").activate();
		return TITLE;
	}

	public SwitchYardComposite getComposite() {
		String compositeName = getCompositeName();
		return new SwitchYardComposite(compositeName);
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

	public SwitchYardComponent addComponent() {
		log.info("Add generic component");
		int oldCount = getNumberOfEditParts();
		getPalette().activateTool(TOOL_COMPONENT);
		composite.click();
		new WaitUntil(new EditorHasEditParts(this, oldCount));
		return new SwitchYardComponent(TOOL_COMPONENT);
	}

	public void addService() {
		log.info("Add generic service");
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

	public CamelJavaServiceWizard addCamelJavaImplementation() {
		return addCamelJavaImplementation(composite);
	}

	public CamelJavaServiceWizard addCamelJavaImplementation(EditPart editPart) {
		addTool(TOOL_CAMEL_JAVA, editPart);
		return new CamelJavaServiceWizard(this);
	}

	public CamelXMLServiceWizard addCamelXMLImplementation() {
		addCamelXmlImplementation(composite);
		return new CamelXMLServiceWizard(this);
	}

	public ExistingCamelXMLServiceWizard addCamelXmlImplementation(EditPart editPart) {
		addTool(TOOL_CAMEL_XML, editPart);
		return new ExistingCamelXMLServiceWizard(this);
	}

	public BPELServiceWizard addBPELImplementation() {
		addBPELImplementation(composite);
		return new BPELServiceWizard();
	}

	public ExistingBPELServiceWizard addBPELImplementation(EditPart editPart) {
		addTool(TOOL_BPEL, editPart);
		return new ExistingBPELServiceWizard();
	}

	public BPMNServiceWizard addBPMNImplementation() {
		addBPMNImplementation(composite);
		return new BPMNServiceWizard();
	}

	public ExistingBPMNServiceWizard addBPMNImplementation(EditPart editPart) {
		addTool(TOOL_BPMN, editPart);
		return new ExistingBPMNServiceWizard();
	}

	public DroolsServiceWizard addDroolsImplementation() {
		addDroolsImplementation(composite);
		return new DroolsServiceWizard();
	}

	public ExistingDroolsServiceWizard addDroolsImplementation(EditPart editPart) {
		addTool(TOOL_RULES, editPart);
		return new ExistingDroolsServiceWizard();
	}

	protected void addTool(String tool, EditPart editPart) {
		getPalette().activateTool(tool);
		editPart.click();
	}

	@Override
	public Palette getPalette() {
		new PaletteView().open();
		PaletteViewer paletteViewer = Display.syncExec(new ResultRunnable<PaletteViewer>() {

			@Override
			public PaletteViewer run() {
				return viewer.getEditDomain().getPaletteViewer();
			}
		});
		// Workaround for JBTISTEST-379
		if (paletteViewer == null) {
			new ResourcePerspective().open();
			new JavaEEPerspective().open();
		}
		return super.getPalette();
	}

	public CompositePropertiesPage showProperties() {
		getComposite().getContextButton("Properties").click();
		return new CompositePropertiesPage("");
	}

	public String xpath(String expr) throws FileNotFoundException {
		XPathEvaluator xpath = new XPathEvaluator(new FileReader(getSourceFile()));
		String result = xpath.evaluateString(expr);
		return result;
	}

	public File getSourceFile() {
		if (sourceFile == null) {
			IEditorInput editorInput = editorPart.getEditorInput();
			if (editorInput instanceof FileEditorInput) {
				FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
				sourceFile = fileEditorInput.getPath().toFile();
			}
		}
		return sourceFile;
	}

	public String getSource() throws IOException {
		StringBuffer source = new StringBuffer();
		BufferedReader in = new BufferedReader(new FileReader(getSourceFile()));
		String line = null;
		while ((line = in.readLine()) != null) {
			source.append(line).append("\n");
		}
		in.close();
		return source.toString();
	}

	public List<SwitchYardComponent> getComponents() {
		List<SwitchYardComponent> components = new ArrayList<SwitchYardComponent>();
		String compositeLabel = getCompositeName();

		List<org.eclipse.gef.EditPart> list = ViewerHandler.getInstance().getEditParts(viewer);
		for (org.eclipse.gef.EditPart editPart : list) {
			String label = getLabel(editPart);
			if (label != null && !label.equals(compositeLabel)) {
				components.add(new SwitchYardComponent(label));
			}
		}
		return components;
	}

	private String getLabel(Object obj) {
		if (obj instanceof GraphitiShapeEditPart) {
			IFigure figure = ((GraphitiShapeEditPart) obj).getFigure();
			IFigure tooltip = figure.getToolTip();
			if (tooltip instanceof Label) {
				Label label = (Label) tooltip;
				return label.getText();
			}
		}
		return null;
	}

	private void doSave() {
		log.info("Save SwitchYard editor");
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		remainedShell = null;
		ShellHandler.getInstance().closeAllNonWorbenchShells(new IBeforeShellIsClosed() {

			@Override
			public void runBeforeShellIsClosed(Shell shell) {
				remainedShell = shell;
			}
		});

		super.save();

		if (PreferencesUtil.isAutoBuildingOn()) {
			new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
			new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		} else {
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}

		if (remainedShell != null) {
			Assert.fail("Shell '" + remainedShell.getText() + "' remains open");
		}
	}

	@Override
	public void save() {
		new WaitUntil(new EditorIsSaved(), TimePeriod.VERY_LONG);
	}

	public void saveAndClose() {
		save();
		close();
	}

	private class EditorIsSaved extends AbstractWaitCondition {

		@Override
		public boolean test() {
			new SwitchYardEditor().doSave();
			return !new SwitchYardEditor().isDirty();
		}

		@Override
		public String description() {
			return "Editor is still dirty";
		}

	}
}
