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

import android.content.Context;
import android.media.AudioManager;
import android.widget.Toast;

/**
 * Store profile rules
 * @author Chamika
 *
 */
public class RuleProfile extends Rule implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	int volume;
	int ringerType;  // 0-SILENT, 1-VIBRATE, 2-NORMAL

	
	/**
	 * Constructor
	 * @param type Type of the rule
	 */
	public RuleProfile(int type) {
		super(type);
	}
	
	/**
	 * Constructor
	 * @param type Type of rule
	 * @param ringertype Ringer Type
	 * @param volume Volume level
	 */
	public RuleProfile(int type,int ringertype, int volume){
		super(type);
		this.volume=volume;
		this.ringerType=ringertype;
	}
	
	/**
	 * Perform Audio settings in the device
	 */
	public void setProfileSetting(){
		
		AudioManager am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		
		am.setStreamVolume(AudioManager.STREAM_RING, volume, 1); // set the volume
		
		if(ringerType == 0)
			am.setRingerMode(AudioManager.RINGER_MODE_SILENT); //set ringEr mode
		else if(ringerType == 1)
			am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		else
			am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			
		Toast.makeText(this, "Sound Settings applied", 500).show();
		
	}
	
	/**
	 * Logging the variable values
	 */
	@Override
	public String toString(){
		return "Ringer Mode=" + ringerType + " Volume=" + volume ;
		
	}

}
