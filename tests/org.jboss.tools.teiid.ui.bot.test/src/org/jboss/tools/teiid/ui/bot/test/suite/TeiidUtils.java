package org.jboss.tools.teiid.ui.bot.test.suite;

import java.io.File;
import java.util.List;

import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.teiid.reddeer.util.FileUtils;
import org.jboss.tools.teiid.reddeer.util.TeiidDriver;

/**
 * 
 * @author apodhrad
 *
 */
public class TeiidUtils {

	public static String getTeiidDriverPath(ServerRequirement serverRequirement) {
		String serverPath = serverRequirement.getConfig().getServerBase().getHome();
		List<File> files = FileUtils.find(serverPath, "teiid.*[jdbc|client].jar");
		if (files.isEmpty() || !files.get(0).exists()) {
			throw new RuntimeException("Cannot find teiid driver");
		}
		return files.get(0).getAbsolutePath();
	}

	public static TeiidDriver getTeiidDriver(ServerRequirement serverRequirement) {
		String teiidDriverPath = getTeiidDriverPath(serverRequirement);
		return new TeiidDriver(teiidDriverPath);
	}
}
