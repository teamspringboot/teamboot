package com.advert.util;
import java.util.UUID;


public class UUIDGenerator {
	public static String nextIdentifier() {	
		 UUID uuid = UUID.randomUUID();
		 return uuid.toString();
	}
}
