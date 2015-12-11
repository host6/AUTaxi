/*
 * Copyright Sunros (c) 2015.
 */

package core.log;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class LogLayout extends Layout {

	private core.log.ExceptionFormatStrategy m_exceptionFormatStrategy = new core.log.ExceptionFormatStrategy();
	private core.log.TraceFormatStrategy m_traceFormatStrategy = new core.log.TraceFormatStrategy();

	@Override
	public void activateOptions() {}

	@Override
	public String format(LoggingEvent event) {
		if (event.getThrowableInformation() != null || event.getLevel() == Level.ERROR || event.getLevel() == Level.FATAL) {
			return m_exceptionFormatStrategy.format(event);
		} else {
			return m_traceFormatStrategy.format(event);
		}
	}

	@Override
	public boolean ignoresThrowable() { return false; }
}
