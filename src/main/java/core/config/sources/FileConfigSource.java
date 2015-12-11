/*
 * Copyright Sunros (c) 2015.
 */

package core.config.sources;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class FileConfigSource implements IConfigSource {

	private String configFN;

	public FileConfigSource(String configFN) {
		this.configFN = configFN;
	}

	@Override
	public String getConfigStr() {
		try {
			return FileUtils.readFileToString(new File(configFN));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("Config file is not found: %s", configFN));
		} catch (Exception e) {
			throw new RuntimeException("Cannot load config from file", e);
		}
	}
}
