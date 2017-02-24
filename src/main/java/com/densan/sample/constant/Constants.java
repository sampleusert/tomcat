package com.densan.sample.constant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;



public class Constants {
	
	/**
	 * 環境変数取得
	 * @return
	 * @throws IOException
	 */
	public Properties getConstants() throws IOException {
		Class c = this.getClass();
    	InputStream is = c.getResourceAsStream("conf.properties");
    	//InputStream is = c.getResource("conf.properties").openStream();
    	Properties properties = new Properties();
    	properties.load(is);
    	is.close();
    	
    	return properties;
	}
	
	public ResourceBundle getConstants2() throws IOException {
		
		File dicDir = Paths.get("/tmp").toFile();
		URLClassLoader urlLoader = new URLClassLoader(new URL[]{dicDir.toURI().toURL()});
		ResourceBundle myResource = ResourceBundle.getBundle("conf", Locale.getDefault(), urlLoader);
		return myResource;
		
	}	

}
