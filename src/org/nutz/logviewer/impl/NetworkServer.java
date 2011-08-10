package org.nutz.logviewer.impl;

import org.nutz.ioc.impl.PropertiesProxy;

public class NetworkServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertiesProxy pp = new PropertiesProxy();
		pp.setPaths("network.properties");
		
	}

}
