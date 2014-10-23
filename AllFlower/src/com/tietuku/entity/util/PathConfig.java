package com.tietuku.entity.util;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;


public class PathConfig {
	private static Properties config;
	
	static{
		try {
			config = PropertiesLoaderUtils.loadAllProperties("tietuku_setting.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	


	
	public static String getProperty(String property){

        if(property=="tie.tu.ku.accessKey"){
            return "1841d85ee99537369dd06e0779bce2041b0323de";
        }
        else{
            return "ead74d8482e253e85910c75e444e4b475b412bdf";
        }

	//	return config.getProperty(property);
	}
	
	public static void main(String[] args){
		System.out.println(PathConfig.getProperty("tie.tu.ku.post.api"));
	}
}
