package org.jboss.tools.bpmn2.ui.bot.test.jbpm;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class SendTaskWorkItemdHandler implements WorkItemHandler {
	
	private String msgName;
	
	public SendTaskWorkItemdHandler(String msgName) {
		this.msgName = msgName;
	}
	
	

    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        Object message = workItem.getParameter("Message");
        Map<String, Object> results = new HashMap<String, Object>();
        results.put(msgName, message);
        
        manager.completeWorkItem(workItem.getId(), results);
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        // Do nothing, cannot be aborted
    }
}
