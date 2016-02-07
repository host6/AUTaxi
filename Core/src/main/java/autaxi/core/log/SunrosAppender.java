/*
 * Copyright Sunros (c) 2015.
 */

package autaxi.core.log;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ADMIN on 04.04.2015.
 *
 */
public class SunrosAppender extends FileAppender{


    private static final int SLEEP_TIME_MILLIS = 50; // sleep between logging attempts
    private static final int RETRIES           = 60;  // logging attempts count

    public static final String EXCEPT_FILE_NAME           =   "except.log";      // except and fatal
    public static final String TRACE_FILE_NAME            =   "trace.log";       // info
    public static final String MISC_FILE_NAME             =   "misc.log";        // other levels


    private static final Map<String, String> defaultLogs;
    private static final Map<String, String> customLogs;
    static {
        defaultLogs = new HashMap<>();

        customLogs = new HashMap<>();
    /* put your custom loggers here... */
	    //customLogs.put(YourLoggerClass.class.getDesc(), YOUR_DESIRED_FILENAME);
    }

    private static File logsFolder = null;

    private static String applicationName = null;

    private static String getLogFileFullName(String fileName){
        if (logsFolder == null || fileName == null){
            return null;
        }
        return new File(logsFolder, fileName).getAbsolutePath();
    }

    public static void initAppender(File logsFolderArg, String applicationNameArg) {
        logsFolder = logsFolderArg;
        applicationName = applicationNameArg;
    }

    @Override
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO,
                                     int bufferSize) throws IOException {
        super.setFile(fileName, append, bufferedIO, bufferSize);
    }

    @Override
    protected void setQWForFiles(Writer writer) {
        qw = new autaxi.core.log.ExeptionPropagationWriter(writer, errorHandler);
    }

    private void writeExceptionManually(Throwable t){
        try{
            File file = new File(logsFolder, EXCEPT_FILE_NAME);
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(file);
                t.printStackTrace(pw);
            } finally {
                if (pw != null) {
                    try {
                        pw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void append(LoggingEvent event) {
        String fileName = resolveFilename(event);
        if (fileName == null){
            return;
        }
        for (int i = 0; i < getRetries(); i++) {
            try {
                try {
                    setFile(fileName, fileAppend, bufferedIO, bufferSize);
                    super.subAppend(event);
                } finally {
                    closeFile();
                }
                break;
            } catch (Exception e) {
                writeExceptionManually(e);
                e.printStackTrace();
                try {
                    Thread.sleep(getSleepTimeMillis());
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    private String getCustomLogFullFileName(String loggerName){
        if (loggerName == null){
            return null;
        }
        String customLog = customLogs.get(loggerName);
        if (customLog != null) {
            return getLogFileFullName(customLog);
        }
        return null;
    }

    private String getDefaultLogFullFileName(Level level){
        String defaultLogFileName = defaultLogs.get(level.toString());
        if (defaultLogFileName == null) {
            defaultLogFileName = TRACE_FILE_NAME;
        }
        return getLogFileFullName(defaultLogFileName);
    }

    private String resolveFilename(LoggingEvent event) {
        String loggerName = event.getLoggerName();
        String result = getCustomLogFullFileName(loggerName);
        if (result == null){
            result = getDefaultLogFullFileName(event.getLevel());
        }
        return result;
    }

    public int getSleepTimeMillis() {
        return SLEEP_TIME_MILLIS;
    }

    public int getRetries() {
        return RETRIES;
    }

    public static String getApplicationName() {
        return applicationName == null ? "" : applicationName;
    }
}
