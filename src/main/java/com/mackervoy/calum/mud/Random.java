/**
 * 
 */
package com.mackervoy.calum.mud;

import java.util.UUID;

/**
 * @author Calum Mackervoy
 * A class to provide random generation
 */
public class Random {
	public static String getRandomUUIDString() {
		UUID randomUUID = UUID.randomUUID();
		return randomUUID.toString().replaceAll("-", "");
	}
}
