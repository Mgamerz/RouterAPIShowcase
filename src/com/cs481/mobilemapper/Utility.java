package com.cs481.mobilemapper;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {
	public static String convertToDataSegment(String str){
		str = str.substring(0, str.length()-1); //remove last }
		str = str.substring(1, str.length()); //remove first {
		str = str.replaceFirst("\"data\":", "");
		return str;
	}
}
