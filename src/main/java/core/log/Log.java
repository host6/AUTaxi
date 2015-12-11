/*
 * Copyright Sunros (c) 2015.
 */

package core.log;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.PropertyConfigurator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by ADMIN on 06.04.2015.
 *
 */
public final class Log {
	private static final String DEFAULT_LOG4J =
			"log4j.rootLogger=TRACE, stdout, file\n" +
					"log4j.appender.stdout=org.apache.log4j.ConsoleAppender\n" +
					"log4j.appender.stdout.layout=core.log.LogLayout\n" +
					"log4j.appender.stdout.Target=System.out\n" +
					"log4j.appender.file=core.log.SunrosAppender\n" +
					"log4j.appender.file.layout=core.log.LogLayout\n" +
					"log4j.appender.stdout.encoding=Cp866\n" +
					"log4j.appender.file.encoding=Cp866\n" +
					"log4j.logger.servlets=TRACE\n" +
					"log4j.logger.core.api.CtxListener=TRACE\n";

	public static void init(String appName) {
		SunrosAppender.initAppender(new File(Paths.get("").toAbsolutePath().toString(), "logs"), appName);
		File flog4j = new File("log4j.properties");
		if (!flog4j.exists()) {
			FileOutputStream fout;
			try {
				fout = new FileOutputStream(flog4j);
				try {
					ByteArrayInputStream log4jTemplate = new ByteArrayInputStream(DEFAULT_LOG4J.getBytes());
					IOUtils.copy(log4jTemplate, fout);
					fout.flush();
					log4jTemplate.close();
				} finally {
					fout.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		PropertyConfigurator.configure("log4j.properties");
	}
}
