import autaxi.EmbeddedServer;
import org.apache.catalina.startup.Tomcat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by admin on 29.11.2015.
 */
public class EmbeddedServerTest {

	@Mock
	Tomcat server;

	@After
	public void tearDown() throws Exception {
		EmbeddedServer.setServer(null);
	}

	@Before
	public void setUp() {
		EmbeddedServer.setServer(server);
	}

	@Test
	public void testStartWebSrv() throws Exception {
		Tomcat server = spy(new Tomcat());

		doNothing().when(server).start();

		EmbeddedServer.startWebSrv(server);

		verify(server).setPort(EmbeddedServer.PORT);
		verify(server).addWebapp(anyString(), anyString());
		verify(server).start();
	}

	@Test(expected = IllegalStateException.class)
	public void testStartWebSrvTwice() throws Exception {

		Tomcat server = spy(new Tomcat());

		doNothing().when(server).start();

		EmbeddedServer.startWebSrv(server);
		EmbeddedServer.startWebSrv(server);
	}

	@Test
	public void testStopWebSrv() throws Exception {

		Tomcat server = spy(new Tomcat());

		doNothing().when(server).start();

		EmbeddedServer.startWebSrv(server);
		EmbeddedServer.stopWebSrv(true);

		verify(server).stop();
		assertNull(EmbeddedServer.getServer());
	}
}