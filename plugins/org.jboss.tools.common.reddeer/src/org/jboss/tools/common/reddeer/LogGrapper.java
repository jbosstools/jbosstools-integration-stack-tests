package org.jboss.tools.common.reddeer;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.tools.common.reddeer.view.ErrorLogView;

/**
 * Utilizes access to Error Log View
 * 
 * @author tsedmik
 */
public class LogGrapper {

	/**
	 * Retrieves all error logs about Fuse plugins
	 * 
	 * @param plugin name of plugin or substring of it
	 * @return List of errors in Fuse plugins. In case of no error occurred, an empty List is returned
	 */
	public static List<LogMessage> getPluginErrors(String plugin) {

		List<LogMessage> fuseErrors = new ArrayList<LogMessage>();
		List<LogMessage> allErrors = new ErrorLogView().getErrorMessages();
		for (LogMessage message : allErrors) {
			if (message.getPlugin().toLowerCase().contains(plugin)) {
				fuseErrors.add(message);
			}
		}
		return fuseErrors;
	}
}
