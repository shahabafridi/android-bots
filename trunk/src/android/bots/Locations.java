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

import java.util.ArrayList;
import java.util.Enumeration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Displays a list of stored locations
 * @author Chamika
 *
 */
public class Locations extends Main {

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locations);
		
		
		//Initialize ListView   
		ListView locationList=(ListView) findViewById(R.id.ListView_Locations);
		
		//get all the location to array for ListView adding
		String[] allLocationsArry=super.allLocations;
		
		
		for(int i=0;i<allLocationsArry.length;i++){
			Log.d("allLocation",allLocationsArry[i]);
		}
		String editingProfile="";
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.menu_item2, allLocationsArry);
        locationList.setAdapter(adapter);
		
		
		//Get previous layout using shared preferences
        //Get editing profile name from shared preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String currentSelect = settings.getString("current", "profile");
		
		
		if(currentSelect.equals("profile")){
			editingProfile = settings.getString("profile", "Vibrate");
			
			//create rule according to editing profile name
		    Log.d("Locations","shared Pref profile=" + editingProfile);
			final RuleProfile ruleP = makeProfileBot(editingProfile);
			
			//Add button OnClick Listeners for profiles
			locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View itemClicked, int arg2,
						long arg3) {
					
					
					TextView textViewTemp = (TextView) itemClicked;
					String strText = textViewTemp.getText().toString();
					
					Log.d("Locations",strText + " Selected");
					
					ArrayList currentcellIDs=AllLocations.get(strText);
					
					//Add the cell ID and RuleProfile to the hashtable
					for(int i=0;i<currentcellIDs.size();i++){
						if(ProfileService.containsKey(currentcellIDs.get(i))){
							ProfileService.remove(currentcellIDs.get(i));
							ProfileService.put((Integer) currentcellIDs.get(i), ruleP);
						}
						else{
							ProfileService.put((Integer) currentcellIDs.get(i), ruleP);
						}
						
					}
					
					//Used for listing the cellid and it's settings
					Enumeration e=ProfileService.keys();
					while(e.hasMoreElements()){
						int key=(Integer) e.nextElement();
						Log.d("Profile Service","CellID=" + Integer.toString(key) + " " + ProfileService.get(key).toString());
					}
				}
	        	
	        });
		}
		
		else if(currentSelect.equals("alarm")){
			Log.d("Locations","shared Pref alarm running");
			
			//Add button OnClick Listeners for profiles
			locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View itemClicked, int arg2,
						long arg3) {
					
					
					TextView textViewTemp = (TextView) itemClicked;
					String strText = textViewTemp.getText().toString();
					
					Log.d("Locations",strText + " Selected");
					
					ArrayList currentcellIDs=AllLocations.get(strText);
					
					//Add the cell ID to the AlarmService vector
					for(int i=0;i<currentcellIDs.size();i++){
						if(!(AlarmService.contains(currentcellIDs.get(i)))){
							AlarmService.add((Integer) currentcellIDs.get(i));
						}
						
					}
					
					//Used for listing the cellid and it's settings
					for(int i=0;i<AlarmService.size();i++){
						Log.d("AlarmService","CellID=" + AlarmService.get(i).toString());
					}
				}
	        	
	        });
		}
		
		
		
        
        
		
		
	}
	
	/**
	 * make a RuleProfile object according to the local variables.
	 * @param profileName Name of the profile
	 * @return Created profile rule according to the profile name
	 */
	public RuleProfile makeProfileBot(String profileName){
		RuleProfile ruleProfile;
		
		if(profileName.equals("Home")){
			ruleProfile=new RuleProfile(1, 2, 4);
		}
		else if(profileName.equals("Indoor")){
			ruleProfile=new RuleProfile(1, 2, 1);
		}
		else if(profileName.equals("Outdoor")){
			ruleProfile=new RuleProfile(1, 2, 7);
		}
		else if(profileName.equals("Silent")){
			ruleProfile=new RuleProfile(1, 0, 0);
		}
		else{
			ruleProfile=new RuleProfile(1, 1, 0);
		}	
		
		return ruleProfile;
	}
	
	

}
