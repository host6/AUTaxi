/*
 * Copyright Sunros (c) 2015.
 */

package core.log;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseFormatStrategy {

	private final StringBuffer m_stringBuffer = new StringBuffer(128);
	private SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
	
	public String format(LoggingEvent event) {
		m_stringBuffer.setLength(0);
		m_stringBuffer.append(getFormattedDate(new Date()));
		m_stringBuffer.append("> ");
		m_stringBuffer.append(event.getMessage());
		m_stringBuffer.append(Layout.LINE_SEP);
		return m_stringBuffer.toString();
	}
	
	public StringBuffer getStringBuffer() {
		return m_stringBuffer;
	}
	
	public String getFormattedDate(Date date) {
		return m_dateFormat.format(date);
	}

	protected String getClassSimpleName(LoggingEvent event) {
		
		String loggerClass = event.getLoggerName();
		if (loggerClass == null) {
			return "";
		}
		int i = loggerClass.lastIndexOf('.');
		if (i > 0) {
			return loggerClass.substring(i + 1);
		}
		return loggerClass;
	}

}

