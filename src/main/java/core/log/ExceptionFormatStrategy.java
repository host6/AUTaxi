/*
 * Copyright Sunros (c) 2015.
 */

package core.log;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import java.util.Date;

public class ExceptionFormatStrategy extends core.log.BaseFormatStrategy {
	
	private static final String INDENT__________ = "                  ";
	private static final String STACK___________ = "Stack           : ";
	private static final String CAUSE___________ = "Cause           : ";
	private static final String MESSAGE_________ = "Message         : ";
	private static final String COMMENTS________ = "Comments        : ";
	private static final String ADDRESS_________ = "Address         : ";
	private static final String EXCEPTION_PLACE_ = "Exception place : ";
	private static final String THREAD__________ = "Thread          : ";
	private static final String EXCEPTION_CLASS_ = "Exception class : ";
	private static final String APPLICATION_____ = "Application     : ";
	private static final String DATE_TIME_______ = "Date,Time       : ";
	private static final String QUANTITY________ = "Quantity        : ";
	private static final String SEPARATOR = "--------------------------------------------------------------------------------";
		
	private String m_exceptionMessage = null;
	private Throwable m_throwable = null;
	private Throwable m_cause = null;
	private String m_exceptionClass = null;

	private String m_lastExceptionInfo = null;
	private Date m_lastDate = null;
	private int m_sameExceptCounter = 0;
	private long m_waiting_period = 0;
	
	public long getWaitingPeriod() {
		return m_waiting_period;
	}

	public void setWaitingPeriod(long millis) {
		m_waiting_period = millis;
	}
	
	@Override
	public String format(LoggingEvent event) {
		
		Date currentDate = new Date();
				
		String date = getFormattedDate(currentDate);
		
		tryInitThrowableInformation(event);
		getStringBuffer().setLength(0);
		printLine(SEPARATOR);
		printLine(DATE_TIME_______ + date);
		printLine(APPLICATION_____ + getApplication());
		printLine(THREAD__________ + getThreadInfo(event));
		printLine(EXCEPTION_CLASS_ + getExceptionClass());
		printLine(EXCEPTION_PLACE_ + "[" + getClassSimpleName(event) + "] " + getLocation(event));
		printLine(ADDRESS_________);
		printLine(COMMENTS________ + event.getMessage());
		printLine(MESSAGE_________ + getExceptionMessage());
		printStack(STACK___________, m_throwable);
		printStack(CAUSE___________, m_cause);
						
		String currentExceptionInfo = getStringBuffer().toString();
		int ind = currentExceptionInfo.indexOf(APPLICATION_____);
		currentExceptionInfo = currentExceptionInfo.substring(ind);
							
		if (m_lastExceptionInfo != null && getInfoWithoutThread(m_lastExceptionInfo).equals(getInfoWithoutThread(currentExceptionInfo))) {
            m_sameExceptCounter += 1;
			if (currentDate.getTime() - m_lastDate.getTime() >= m_waiting_period) {
				if (m_sameExceptCounter > 1) {
					printLine(QUANTITY________ + m_sameExceptCounter);
				}
				m_lastDate = currentDate;
				m_sameExceptCounter = 0;
			} else {
				return "";
			}
		} else {
			if (m_sameExceptCounter > 0) {
						
				StringBuilder oldExceptionWithQuantity = new StringBuilder(128);
				oldExceptionWithQuantity.append(SEPARATOR);
				oldExceptionWithQuantity.append(Layout.LINE_SEP);
				oldExceptionWithQuantity.append(DATE_TIME_______);
				oldExceptionWithQuantity.append(date);
				oldExceptionWithQuantity.append(Layout.LINE_SEP);
				oldExceptionWithQuantity.append(m_lastExceptionInfo);
				if (m_sameExceptCounter > 1) {
					oldExceptionWithQuantity.append(QUANTITY________);
					oldExceptionWithQuantity.append(m_sameExceptCounter);
					oldExceptionWithQuantity.append(Layout.LINE_SEP);
				}
				oldExceptionWithQuantity.append(Layout.LINE_SEP);
				
				getStringBuffer().insert(0, oldExceptionWithQuantity);
							
				m_sameExceptCounter = 0;
			}
			
			m_lastExceptionInfo = currentExceptionInfo;
			m_lastDate = currentDate;
		}
		printLine("");
		return getStringBuffer().toString();
	}

	private String getExceptionMessage() {
		return m_exceptionMessage == null ? "" : m_exceptionMessage;
	}

	private String getExceptionClass() {
		return m_exceptionClass == null ? "" : m_exceptionClass;
	}

	protected String getLocation(LoggingEvent event) {
		LocationInfo locationInfo = event.getLocationInformation();
		return locationInfo == null || locationInfo.fullInfo == null ? "" : locationInfo.fullInfo;

	}
	
	protected String getApplication() {
		return core.log.SunrosAppender.getApplicationName();
	}

	protected void printStack(String name, Throwable t) {
		if (t != null) {
			StackTraceElement[] stackArray = t.getStackTrace();
			for (int i = 0; i < stackArray.length; i++) {
				if (i == 0) {
					printLine(name + stackArray[0]);
				} else {
					printLine(INDENT__________ + stackArray[i]);
				}
			}
		}
	}

	private void tryInitThrowableInformation(LoggingEvent event) {
		ThrowableInformation m_throwableInformation = event.getThrowableInformation();
		if (m_throwableInformation != null) {			
			m_throwable = m_throwableInformation.getThrowable();

			if (m_throwable.getClass() == RuntimeException.class 
					&& m_throwable.getCause() != null
					&& (m_throwable.getMessage() == null ||
						m_throwable.getMessage().equals(m_throwable.getCause().toString()))) {
				m_throwable = m_throwable.getCause();
			}
			if (m_throwable != null) {
				m_exceptionMessage = m_throwable.getMessage();
				m_exceptionClass = m_throwable.getClass().getName();
				m_cause = m_throwable.getCause();
			}
		}
	}

	protected void printLine(String text) {
		getStringBuffer().append(text);
		getStringBuffer().append(Layout.LINE_SEP);
	}
	
	protected String getInfoWithoutThread(String s) {
		
		String res = s;
		if (s != null) {
			int indThr   = s.indexOf(THREAD__________);
			int indExcCl = s.indexOf(EXCEPTION_CLASS_);
			
			if (indThr != -1) {
				String app = s.substring(0, indThr);
				res = s.substring(indExcCl);
				res = app + res;
			}
		}
						
		return res;
	}
	
	protected String getThreadInfo(LoggingEvent event) {
		return event.getThreadName();
	}
}
