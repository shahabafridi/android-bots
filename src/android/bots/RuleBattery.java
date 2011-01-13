/**
 *  This program is free software; you can redistribute it and/or modify it under 
 *  the terms of the GNU General Public License as published by the Free Software 
 *  Foundation; either version 3 of the License, or (at your option) any later 
 *  version.
 *  You should have received a copy of the GNU General Public License along with 
 *  this program; if not, see <http://www.gnu.org/licenses/>. 
 *  Use this application at your own risk.
 *
 *  Copyright (c) 2010 by Chamika Weerasinghe.
 *  AndroidBots
 *  
 */

package android.bots;

import java.io.Serializable;

/**
 * Stores data appropriate to Battery options
 * @author Chamika
 *
 */
public class RuleBattery extends Rule implements Serializable {

	private static final long serialVersionUID = 6313341507713670287L;
	int brightness,timeout;
	boolean wifi,bluetooth,gps,twog,dataSync;
	
	/**
	 * Constructor
	 * @param brightness Screen Brightness
	 * @param timeout Screen timeout
	 * @param wifi Wifi on/off 
	 * @param bluetooth Bluetooth on/off
	 * @param gps GPS on/off
	 * @param twog 2G network only on/off
	 * @param dataSync Data synchronization on/off
	 */
	public RuleBattery(int brightness, int timeout, boolean wifi,
			boolean bluetooth, boolean gps, boolean twog, boolean dataSync) {
		super();
		this.brightness = brightness;
		this.timeout = timeout;
		this.wifi = wifi;
		this.bluetooth = bluetooth;
		this.gps = gps;
		this.twog = twog;
		this.dataSync = dataSync;
	}
	
	
}
