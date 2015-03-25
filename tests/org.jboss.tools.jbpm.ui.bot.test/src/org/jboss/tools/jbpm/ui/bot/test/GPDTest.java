/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jbpm.ui.bot.test;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.gef.impl.editpart.LabeledEditPart;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.tools.jbpm.ui.bot.test.editor.JBPMEditor;
import org.jboss.tools.jbpm.ui.bot.test.perspective.JBPMJPDL3Perspective;
import org.jboss.tools.jbpm.ui.bot.test.wizard.JBPMProjectWizard;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@CleanWorkspace
@OpenPerspective(JBPMJPDL3Perspective.class)
@Runtime(type = RuntimeReqType.JBPM)
@RunWith(RedDeerSuite.class)
public class GPDTest {

	public static final String PROJECT = "gpdtest";

	@InjectRequirement
	private RuntimeRequirement requirement;

	@Before
	public void createProject() {
		/* Create jBPM3 Project */
		JBPMProjectWizard projectWizard = new JBPMProjectWizard();
		projectWizard.open();
		projectWizard.setName(PROJECT);
		projectWizard.next();
		System.out.println(requirement);
		System.out.println(requirement.getConfig());
		System.out.println(requirement.getConfig().getName());
		projectWizard.setRuntime(requirement.getConfig().getName());
		projectWizard.finish();
	}

	@Test
	public void gpdTest() {
		/* Open Simple Diagram */
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src", "main", "jpdl", "simple.jpdl.xml").open();

		// debug();

		/* Find Nodes */
		final JBPMEditor editor = new JBPMEditor("simple.jpdl.xml");

		String[] nodes = { "start", "first", "end" };

		for (String node : nodes) {
			new LabeledEditPart(node).select();
		}

		/* Resize Nodes */
		for (String node : nodes) {
			// TODO Implement resize
			// editor.getEditPart(node).select().focus();
			// editor.getEditPart(node).resize(PositionConstants.SOUTH_EAST, 100, 70);
		}
		editor.save();

		final Rectangle[] abounds = new Rectangle[1];

		for (final String node : nodes) {
			Rectangle bounds = new JBPMEditPart(node).getBounds();
			// TODO Implement drag
			// editor.drag(editor.getEditPart(node), bounds.x + 100, bounds.y + 100);
		}
		editor.save();

		/* Edit Labels */
		for (String node : nodes) {
			new LabeledEditPart(node).setLabel(node + "_NEXT");
			editor.save();
		}

		/* Check changed labes */
		for (String node : nodes) {
			new LabeledEditPart(node + "_NEXT").select();
		}
	}

	private class JBPMEditPart extends LabeledEditPart {

		public JBPMEditPart(String label) {
			super(label);
		}

		public Rectangle getBounds() {
			return Display.syncExec(new ResultRunnable<Rectangle>() {

				@Override
				public Rectangle run() {
					return getFigure().getBounds();
				}

			});
		}
	}

}
