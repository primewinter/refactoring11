package com.model2.mvc.common.util;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {

	public static String getClientIP(HttpServletRequest request) {
		
		String ip = request.getHeader("X-FORWARDED-FOR");
		
		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
		}
	
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		
		if (ip == null) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}

		if (ip == null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}

		if (ip == null) {
			ip = request.getRemoteAddr();
		}

		return ip;



	
	}

}
