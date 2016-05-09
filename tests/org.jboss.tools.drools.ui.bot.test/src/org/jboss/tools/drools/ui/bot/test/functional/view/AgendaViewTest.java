package org.jboss.tools.drools.ui.bot.test.functional.view;

import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.view.AgendaView;
import org.jboss.tools.drools.ui.bot.test.util.OpenUtility;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class AgendaViewTest extends ViewTestParent {

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	public AgendaViewTest() {
		super(AgendaView.class);
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testAgendaViewDefaultContent() {
		OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));

		RuleEditor editor = new DrlEditor().showRuleEditor();
		editor.setBreakpoint(8);

		RunUtility.debugAsDroolsApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");

		AgendaView agenda = new AgendaView();
		agenda.open();

		List<String> agendaGroups = agenda.getAgendaGroupNames();
		Assert.assertEquals("Unexpected number of agenda groups", 1, agendaGroups.size());
		Assert.assertEquals("Wrong name of default agenda group", "MAIN", agendaGroups.get(0));

		List<String> activations = agenda.getActivations();
		Assert.assertEquals("Unexpected number of activations", 1, activations.size());
		Assert.assertEquals("Wrong rule activated", "Hello World", activations.get(0));
	}

	@Test
	@UsePerspective(DroolsPerspective.class)
	@UseDefaultProject
	public void testCustomAgendaGroup() {
		OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));

		RuleEditor editor = new DrlEditor().showRuleEditor();
		// add a new rule with different agenda group
		editor.setPosition(20, 0);
		editor.writeText("\nrule testRule\n");
		editor.writeText("    agenda-group \"test group\"\n");
		editor.writeText("    when\n");
		editor.writeText("    then\n");
		editor.writeText("        System.out.println(\"Firing!\");\n");
		editor.writeText("end\n");
		editor.save();
		editor.setBreakpoint(8);

		RunUtility.debugAsDroolsApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");

		AgendaView agenda = new AgendaView();
		agenda.open();

		List<String> agendaGroups = agenda.getAgendaGroupNames();
		Assert.assertEquals("Unexpected number of agenda groups", 2, agendaGroups.size());
		Assert.assertTrue("Agenda group 'MAIN' not listed", agendaGroups.contains("MAIN"));
		Assert.assertTrue("Agenda group 'test_group' not listed", agendaGroups.contains("test_group"));

		List<String> activations = agenda.getActivations();
		Assert.assertEquals("Unexpected number of activations", 2, activations.size());
		Assert.assertTrue("Rule 'Hello World' not activated", activations.contains("Hello World"));
		Assert.assertTrue("Rule 'testRule' not activated", activations.contains("testRule"));
	}
}
