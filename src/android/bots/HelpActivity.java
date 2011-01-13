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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Activity of Help layout
 * @author Chamika
 *
 */
public class HelpActivity extends Main {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		TextView TextViewTitle= (TextView) findViewById(R.id.TextView_HelpTitle);
		TextView TextViewText = (TextView) findViewById(R.id.TextView_HelpText);
		
		//Get selected help using shared preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String currentSelect = settings.getString("help", "main");
		
		//Set Title
		TextViewTitle.setText(convertTitle(currentSelect) + " Help");
		
		//Load help file text
		String helpText=loadText(currentSelect);
		
		TextViewText.setText(helpText);
	}
	
	/**
	 * Reads the help file and create string
	 * @param Select Name of the help file
	 * @return Text inside the help file
	 */
	public String loadText(String Select){
		
		InputStream is;
		
		if(Select.equals("locator")){
			is=getResources().openRawResource(R.raw.helplocator);
		}
		else if(Select.equals("alarmbot")){
			is=getResources().openRawResource(R.raw.helpalarmboth);
		}
		else if(Select.equals("batterybot")){
			is=getResources().openRawResource(R.raw.helpbatterybot);
		}
		else if(Select.equals("profilebot")){
			is=getResources().openRawResource(R.raw.helpprofilebot);
		}
		else {
			is=getResources().openRawResource(R.raw.helpmain);
		}
		
        DataInputStream dis=new DataInputStream(is);
        String txt = "";
        try {
        	//read help file line by line
        	while(dis.available()!= 0){
			txt += dis.readLine();
        	}
        	
        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Help","Error while file reading");
			
		}
		return txt;
	}
	
	/**
	 * Convert shared preference name to application title
	 * @param Select shared preferences name
	 * @return Title of the application
	 */
	public String convertTitle(String Select){
		
		String Title;
		
		if(Select.equals("locator")){
			Title = "Locator";
		}
		else if(Select.equals("alarmbot")){
			Title = "Alarm Bot";
		}
		else if(Select.equals("batterybot")){
			Title = "Battery Bot";
		}
		else if(Select.equals("profilebot")){
			Title = "Profile Bot";
		}
		else {
			Title = "Android Bots";
		}
		
		return Title;
	}
	

}
