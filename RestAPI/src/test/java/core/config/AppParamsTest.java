package core.config;

import autaxi.core.config.AppParams;
import junit.framework.TestCase;

public class AppParamsTest extends TestCase {

	public void testGetApplicationName() throws Exception {
		String host = AppParams.getComputerName();
		assertEquals(host, AppParams.getHostNameViaNativeCall());
		assertEquals(host, AppParams.getM_hostName());
	}

	public void testGetComputerName() throws Exception {

	}
}