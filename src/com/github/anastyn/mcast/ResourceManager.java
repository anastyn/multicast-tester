package com.github.anastyn.mcast;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceManager {

	private static final ResourceBundle BUNDLE;
	
	static {
		System.out.println("Initializing the ResourceManager");
		BUNDLE = ResourceBundle.getBundle("resources");
	}
	
	/**
	 * Return version of the application
	 */
	public static String getVersion() {
		return BUNDLE.getString("version");
	}
	
	/**
	 * Return the jar usage message
	 */
	public static String getUsage() {
		return BUNDLE.getString("usage");
	}
	
	/**
	 * Return console input encoding
	 */
	public static String getEncoding() {
		return BUNDLE.getString("encoding");
	}
	
}
