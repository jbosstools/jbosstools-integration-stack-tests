package org.jboss.tools.switchyard.reddeer.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.internal.features.context.impl.base.PictogramElementContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.IContextButtonEntry;
import org.eclipse.graphiti.tb.IContextButtonPadData;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.internal.parts.IPictogramElementEditPart;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.ui.IEditorPart;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.MainComponent;
import org.jboss.tools.switchyard.reddeer.widget.ContextButtonEntry;

/**
 * SwitchYeard editor
 * 
 * @author apodhrad
 * 
 */
public class SwitchYardEditor extends SWTBotGefEditor {

	private MainComponent mainComponent;
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();

	private DiagramEditor diagramEditor;

	public SwitchYardEditor() {
		super(bot.editorByTitle("switchyard.xml").getReference(), bot);
		diagramEditor = UIThreadRunnable.syncExec(new Result<DiagramEditor>() {
			public DiagramEditor run() {
				final IEditorPart editor = partReference.getEditor(true);
				return (DiagramEditor) editor.getAdapter(DiagramEditor.class);
			}
		});
		mainComponent = new MainComponent(mainEditPart().children().get(0));
	}

	public void addComponent(String component) {
		activateTool(component);
		clickMainComponent();
	}

	private void clickMainComponent() {
		mainComponent.click();
	}

	public MainComponent getMainComponent() {
		return mainComponent;
	}

	public void addComponent(String component, Integer[] coords) {
		activateTool(component);
		mainComponent.click(coords[0], coords[1]);
	}

	@Override
	public void save() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new GefEditor("switchyard.xml").save();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public List<ContextButtonEntry> getContextButtonEntries(final Component component) {
		return Display.syncExec(new ResultRunnable<List<ContextButtonEntry>>() {
			
			@Override
			public List<ContextButtonEntry> run() {
				List<IContextButtonEntry> entries = new ArrayList<IContextButtonEntry>();
				IToolBehaviorProvider[] tool = diagramEditor.getDiagramTypeProvider().getAvailableToolBehaviorProviders();
				for (int i = 0; i < tool.length; i++) {
					IPictogramElementContext context = createPictogramElementContext(component);
					IContextButtonPadData pad = tool[i].getContextButtonPad(context);
					System.out.println(pad.getClass());
					System.out.println(pad);
					entries.addAll(pad.getDomainSpecificContextButtons());
					entries.addAll(pad.getGenericContextButtons());
				}
				List<ContextButtonEntry> contextButtonEntries = new ArrayList<ContextButtonEntry>();
				for (IContextButtonEntry entry: entries) {
					ContextButtonEntry contextButtonEntry = new ContextButtonEntry(entry);
					contextButtonEntries.add(contextButtonEntry);
				}
				return contextButtonEntries;
			}
		});
	}

	@SuppressWarnings("restriction")
	private IPictogramElementContext createPictogramElementContext(
			final Component component) {
		EditPart part = component.getEditPart().part();

		if (part instanceof IPictogramElementEditPart) {
			IPictogramElementEditPart peep = (IPictogramElementEditPart) part;
			PictogramElement pe = peep.getPictogramElement();
			return new PictogramElementContext(pe);
		}
		throw new RuntimeException("Cannot create PictogramElementContext");
	}

	@Override
	public void saveAndClose() {
		bot.closeAllShells();
		super.saveAndClose();
	}
	
	

}
