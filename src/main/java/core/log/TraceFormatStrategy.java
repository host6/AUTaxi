/*
 * Copyright Sunros (c) 2015.
 */

package core.log;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Date;

public class TraceFormatStrategy extends core.log.BaseFormatStrategy {

    @Override
    public String format(LoggingEvent event) {
        StringBuffer stringBuffer = getStringBuffer();
        stringBuffer.setLength(0);
        stringBuffer.append(getFormattedDate(new Date()));
        stringBuffer.append(" ").append(getClassSimpleName(event));
        stringBuffer.append("> ");
	    stringBuffer.append(event.getMessage());
        stringBuffer.append(Layout.LINE_SEP);
        return stringBuffer.toString();
    }
}
