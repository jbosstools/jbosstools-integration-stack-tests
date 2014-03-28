package org.jboss.tools.awt;

import java.awt.AWTException;
import java.awt.Robot;

/**
 * See: http://undocumentedmatlab.com/blog/gui-automation-robot/
 */
public class AWTBot {
    
    private Robot robot;

    private int delay;
    
    /**
     * 
     * @throws Exception 
     */
    public AWTBot() {
        this(100);
    }
    
    /**
     * 
     * @param delay
     * @param driver 
     */
    public AWTBot(int delay) {
    	try {
    		this.delay = delay;
            this.robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException("Initialization failed.", e);
		}
    }
    
    /**
     * 
     * @return 
     */
    public Robot getRobot() {
        return robot;
    }
    
    /**
     * Type a key combination. E.g. CTRL+SHIFT+A
     * 
     * @param keycodes 
     */
    public void typeKeyCombo(int... keycodes) {
        // press from 1..n
        for (int i = 0; i <= keycodes.length - 1; i++) {
            robot.keyPress(keycodes[i]);
        }
        // release from n..1
        for (int i = keycodes.length - 1; i >= 0; i--) {
            robot.keyRelease(keycodes[i]);
        }
        robot.delay(delay);
    }
    
    /**
     * Type several key codes.
     * 
     * @param keycodes 
     */
    public void type(int... keycodes) {
        for (int k : keycodes) {
            robot.keyPress(k);
            robot.keyRelease(k);
            robot.delay(delay);
        }
    }
    
}
