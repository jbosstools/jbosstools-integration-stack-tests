package org.jboss.tools.drools.reddeer.view;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;
import org.jboss.tools.drools.reddeer.util.ItemLookup;

public class AgendaView extends WorkbenchView {
    private static final Pattern NAME_PATTERN = Pattern.compile("[\\w\\s]*");

    public AgendaView() {
        super("Drools", "Agenda");
    }

    public List<String> getAgendaGroupNames() {
        open();
        List<String> groups = new LinkedList<String>();

        for (TreeItem item : new DefaultTree().getItems()) {
            groups.add(cleanAgendaGroupName(item.getText()));
        }

        return groups;
    }

    public void selectAgendaGroup(String name) {
        open();
        ItemLookup.getItemInTree(new DefaultTree(), new RegexMatchers(name + ".*").getMatchers());
    }

    public List<String> getActivations() {
        open();
        List<String> activations = new LinkedList<String>();

        for (String group : getAgendaGroupNames()) {
            activations.addAll(getActivationsForGroup(group));
        }

        return activations;
    }

    public List<String> getActivationsForGroup(String groupName) {
        open();
        TreeItem group = ItemLookup.getItemInTree(new DefaultTree(), new RegexMatchers(groupName + ".*").getMatchers());

        List<String> activations = new LinkedList<String>();

        for (TreeItem item : group.getItems()) {
            String ruleLine = item.getItems().get(0).getText();
            activations.add(ruleLine.substring(11, ruleLine.lastIndexOf('"')));
        }

        return activations;
    }

    private String cleanAgendaGroupName(String original) {
        Matcher m = NAME_PATTERN.matcher(original);
        m.find();
        return m.group();
    }
}
