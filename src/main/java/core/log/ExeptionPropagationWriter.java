/*
 * Copyright Sunros (c) 2015.
 */

package core.log;

import org.apache.log4j.helpers.QuietWriter;
import org.apache.log4j.spi.ErrorHandler;

import java.io.Writer;

public class ExeptionPropagationWriter extends QuietWriter {

	public ExeptionPropagationWriter(Writer writer, ErrorHandler errorHandler) {
		super(writer, errorHandler);
	}

	@Override
	public void write(String string) {
		if (string != null) {
			try {
				out.write(string);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void flush() {
		try {
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
