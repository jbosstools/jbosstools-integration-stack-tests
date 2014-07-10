package org.jboss.tools.fuse.reddeer.utils;

import java.io.ByteArrayOutputStream;

/**
 * Implementation of ByteArrayOutputStream knowing it's status
 * 
 * @author tsedmik
 */
public class StatusByteArrayOutputStream extends ByteArrayOutputStream {
	
	private boolean closeStatus = false;

	/**
	 * Checks whether is stream closed
	 * 
	 * @return <b>true</b> - stream is closed, <b>false</b> - otherwise
	 */
	public boolean isClosed() {
		
		return closeStatus;
	}
	
	/**
	 * Closes the output stream
	 */
	public void close() {
		
		this.close();
		closeStatus = true;
	}
}
