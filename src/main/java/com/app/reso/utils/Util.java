package com.app.reso.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class Util {

	public static String generateToken() {
		byte[] bytes = new byte[64];
		new SecureRandom().nextBytes(bytes);
		return Base64.getEncoder().encodeToString(bytes);
	}
}
