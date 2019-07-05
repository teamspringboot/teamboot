package com.advert.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtil {

	private static final  Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);
	public static int ExceptionLogLength = 20000;
	public static String getExceptionStackTrace(Exception e, int length) {
		String trace = null;
		StringWriter sw = new StringWriter(1024);
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        trace = sw.toString();
		if (trace!=null) {
			if (trace.length()>length) {
				trace = trace.substring(0, length);
			}
		}
		trace = trace.replace("\"", "").replace("'", "").replace("\n", "<br>").replace("\r", "");
		//logger.debug(trace);
		return trace;
	}
}
