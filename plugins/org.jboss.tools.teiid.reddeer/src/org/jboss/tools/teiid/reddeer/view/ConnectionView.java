package org.jboss.tools.teiid.reddeer.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithTooltipTextMatcher;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.YesButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;

public class ConnectionView extends WorkbenchView {

    public ConnectionView() {
        super("Connections");
    }

    public static ConnectionView getInstance() {
        ConnectionView connectionView = new ConnectionView();
        connectionView.open();
        return connectionView;
    }

    public boolean existConnectionProfile(String... inputPath) {
        refreshView();
        List<String> path = new ArrayList<String>();
        path.add("Local Profiles");
        path.addAll(Arrays.asList(inputPath));
        try {
            new DefaultTreeItem(path.toArray(new String[0])).select();
        } catch (org.eclipse.reddeer.core.exception.CoreLayerException ex) {
            if (ex.getMessage()
                .contains("There are no items matching matcher tree item text on position <0> matches")) {
                return false;
            } else {
                throw ex; // another problem
            }
        }
        return true;
    }
    
    public boolean existDataSource(String name) {
        refreshView();
        try {
            new DefaultTreeItem("Deployed", name).select();
        } catch (org.eclipse.reddeer.core.exception.CoreLayerException ex) {
            if (ex.getMessage()
                .contains("There are no items matching matcher tree item text on position <0> matches")) {
                return false;
            } else {
                throw ex; // another problem
            }
        }
        return true;
    }
    
    public void createConnectionProfile() {
        this.activate();
        List<String> path = new ArrayList<String>();
        path.add("Local Profiles");
        new PushButton(this, new WithTooltipTextMatcher("Create Connection Profile")).click();
        new DefaultShell("New Connection Profile"); // TODO create class for it?
    }

    public void createDataSource() {
        this.activate();
        new DefaultTreeItem("Deployed").select();
        new PushButton(this, new WithTooltipTextMatcher("Create Data Source")).click();
        new DefaultShell("Create DataSource"); // TODO create class for it?
    }

    public void deleteConnectionProfile(String... inputPath) {
        refreshView();
        List<String> path = new ArrayList<String>();
        path.add("Local Profiles");
        path.addAll(Arrays.asList(inputPath));
        new DefaultTreeItem(path.toArray(new String[0])).select();
        new PushButton(this, new WithTooltipTextMatcher("Delete")).click();
        DefaultShell deleteDialog = new DefaultShell("Delete confirmation");
        new YesButton(deleteDialog).click();
        refreshView();
    }

    public void deleteDataSource(String name) {
        refreshView();
        new DefaultTreeItem("Deployed", name).select();
        new PushButton(this, new WithTooltipTextMatcher("Delete")).click();
        DefaultShell deleteDialog = new DefaultShell("Delete DataSource");
        new YesButton(deleteDialog).click();
        new WaitWhile(new ShellIsActive("Progress Information"));
        refreshView();
    }

    public void editConnectionProfile(String... inputPath) {
        refreshView();
        List<String> path = new ArrayList<String>();
        path.add("Local Profiles");
        path.addAll(Arrays.asList(inputPath));
        new DefaultTreeItem(path.toArray(new String[0])).select();
        new PushButton(this, new WithTooltipTextMatcher("Edit")).click();
        new DefaultShell("Properties for " + path.get(path.size()-1));
    }

    public void editDataSource(String name) {
        refreshView();
        new DefaultTreeItem("Deployed", name).select();
        new PushButton(this, new WithTooltipTextMatcher("Edit")).click();
        new DefaultShell("Edit DataSource"); // TODO create class for it?
    }

    public void refreshView() {
        this.activate();
        new PushButton(this, new WithTooltipTextMatcher("Refresh profiles and deployed data sources")).click();
    }

    public void changeDefaultServer(String nameNewServer) {
        this.activate();
        new DefaultHyperlink(new WithTooltipTextMatcher("Change Default Server;")).activate();
        if (new ShellIsAvailable("Default server unchanged").test()) {
            throw new Error("Only 1 server is configured");
        }
        DefaultShell shell = new DefaultShell("Server Selection");
        if (new ShellIsAvailable(shell).test()) {
            new DefaultCombo().setSelection(nameNewServer);
            new OkButton().click();
        }
        if (new ShellIsAvailable("Change of Teiid Version").test()) { // the teiid instances are different version
            new YesButton(new DefaultShell("Change of Teiid Version")).click();
        }
        if (new ShellIsAvailable("Untested Teiid Version").test()) { // if test untestet teiid version
            new YesButton(new DefaultShell("Untested Teiid Version")).click();
        }
        if (new ShellIsAvailable("Disconnect Current Default Instance").test()) { // if you want to disconnect old instance before switching
            new YesButton(new DefaultShell("Disconnect Current Default Instance")).click();
        }
        if (new ShellIsAvailable("Default Server Changed").test()) {
            new OkButton(new DefaultShell("Default Server Changed")).click();
        } else if (new ShellIsAvailable("Default server unchanged").test()) {
            new OkButton(new DefaultShell("Default server unchanged")).click();
        } else {
            throw new Error("Default server not been changed due to an error");
        }
    }
}
