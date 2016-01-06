package org.jboss.tools.bpmn2.ui.bot.complex.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestPhase {
	public Phase phase();

	public enum Phase {
		MODEL, VALIDATE, RUN
	}
}
