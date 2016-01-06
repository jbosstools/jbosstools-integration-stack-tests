/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jbpm.ui.bot.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.gef.impl.editpart.LabeledEditPart;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
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
public class GPDPaletteTest {

	public static final String PROJECT = "palettetest";

	@InjectRequirement
	protected RuntimeRequirement requirement;

	@Before
	public void createProject() {
		/* Create jBPM3 Project */
		JBPMProjectWizard projectWizard = new JBPMProjectWizard();
		projectWizard.open();
		projectWizard.setName(PROJECT).next();
		projectWizard.setRuntime(requirement.getConfig().getName()).finish();
	}

	@Test
	public void insertNodes() {
		/* Open Simple Diagram */
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src", "main", "jpdl", "simple.jpdl.xml").open();

		String[] entities = {
			"Start",
			"State",
			"End",
			"Fork",
			"Join",
			"Decision",
			"Node",
			"Task Node",
			"Mail Node",
			"ESB Service",
			"Process State",
			"Super State" };

		/* Add All Entities */
		JBPMEditor editor = new JBPMEditor("simple.jpdl.xml");

		new LabeledEditPart("start").click();
		new ContextMenu("Delete").select();
		new LabeledEditPart("first").click();
		new ContextMenu("Delete").select();
		new LabeledEditPart("end").click();
		new ContextMenu("Delete").select();

		for (int i = 0; i < entities.length; i++) {
			int x = 100;
			int y = 100 + 10 * i;
			String entity = entities[i];
			editor.insertEntity(entity, x, y);
		}
		editor.save();
	}

}
