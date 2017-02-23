package com.densan.sample.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {
	
	/**
	 * 環境変数取得
	 * @return
	 * @throws IOException
	 */
	public Properties getConstants() throws IOException {
		
    	//InputStream is = Constants.class.getClassLoader().getResourceAsStream("conf.properties");
    	InputStream is = this.getClass().getResource("conf.properties").openStream();
    	Properties properties = new Properties();
    	properties.load(is);
    	is.close();
    	
    	return properties;
		
	}

}
