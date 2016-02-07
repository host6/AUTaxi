/*
 * Copyright Sunros (c) 2015.
 */

package autaxi.core.config;


import java.net.InetAddress;

/**
 * Created by Denis Gribanov on 29.03.2015.
 *
 */
public class AppParams {
    private static volatile String m_hostName;

    private final String m_untillHome;
    private final String m_appName;

    public static String getM_hostName() {
        return m_hostName;
    }

    public static void setM_hostName(String m_hostName) {
        AppParams.m_hostName = m_hostName;
    }

    public AppParams(String untillHome, String appName) {
        m_untillHome = untillHome;
        m_appName = appName;
    }

    public String getAppFolder() {
        return m_untillHome;
    }

    public String getApplicationName() {
        return m_appName;
    }

    public static String getHostNameViaNativeCall() {
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            m_hostName = addr.getHostName();
            return m_hostName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getComputerName() {

	    if (m_hostName != null) {
		    return m_hostName;
        }
        return getHostNameViaNativeCall();
    }

}