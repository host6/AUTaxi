/* 
 * Copyright(C) UnTill 
 * All Rights Reserved. 
 * 
 */


import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class EmbeddedServer {


	public static int PORT = 8080;

	private static Tomcat server;

	public static Tomcat getServer() {
		return server;
	}

	public static void setServer(Tomcat server) {
		EmbeddedServer.server = server;
	}

	public static void main(String[] args) throws Exception {
		startWebSrv();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				stopWebSrv(true);
			} catch (Exception e) {

			}
		}));
		server.getServer().await();
	}

	public static void startWebSrv(Tomcat server) throws Exception {

		String webappDirLocation = "src/main/webapp/";

		if (EmbeddedServer.server != null) {
			throw new IllegalStateException("startWebServer() can not be called twice");
		}

		EmbeddedServer.server = server;

		server.setPort(PORT);
		server.getHost().setAutoDeploy(true);
		server.getHost().setDeployOnStartup(true);
		server.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
		server.start();
	}

	public static void startWebSrv() throws Exception {
		EmbeddedServer.startWebSrv(new Tomcat());
	}


	public static void stopWebSrv(boolean bStartStopServer) throws Exception {
		if (server != null && bStartStopServer) {
			server.stop();
			server = null;
		}
	}

}
