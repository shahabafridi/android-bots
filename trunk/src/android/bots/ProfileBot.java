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


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity of the AlarmBot layout
 * @author Chamika
 *
 */
public class ProfileBot extends Main {
	
	//ListView for showing profiles 
	ListView listview;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profilebot);
		
		//Build data in application
		buildLocations();
		buildLocationArray();
		
		ListView profileList=(ListView) findViewById(R.id.ListView_Profiles);
		
		//Initialize array for adding to the ListView
        String[] profiles={"Home","Indoor","Outdoor","Silent","Vibrate"};
        
        //Initialize array to add to the ListView
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,R.layout.menu_item, profiles);
        profileList.setAdapter(adapt);
        
        //Set OnItemClickListeners for the ListView
        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View itemClicked, int arg2,
					long arg3) {
				
				//SharedPreferences used to pass selected profile to the Locations view
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			    SharedPreferences.Editor editor = settings.edit();
			    buildLocationArray();
			      
				TextView textViewTemp = (TextView) itemClicked;
				String strText = textViewTemp.getText().toString();
				
				editor.putString("current", "profile");
				
				if(strText.equals("Home")){
					Log.d("ProfileBot","Home Selected");
					editor.putString("profile", "Home");
				}
				else if(strText.equals("Indoor")){
					Log.d("ProfileBot","Indoor Selected");
					editor.putString("profile", "Indoor");
				}
				else if(strText.equals("Outdoor")){
					Log.d("ProfileBot","Outdoor Selected");
					editor.putString("profile", "Outdoor");
				}
				else if(strText.equals("Silent")){
					Log.d("ProfileBot","Silent Selected");
					editor.putString("profile", "Silent");
				}
				else if(strText.equals("Vibrate")){
					Log.d("ProfileBot","Vibrate Selected");
					editor.putString("profile", "Vibrate");
				}
				
				editor.commit();
				startActivity(new Intent(ProfileBot.this,Locations.class));
				
			}
        	
        });
		
	}
	
	/**
	 * Setup the options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.helpmenu, menu);
		menu.findItem(R.id.item_Help).setIntent(new Intent(this, HelpActivity.class)); 		
		return true;
	}
	
	/**
	 * set shared preferences for help activity and execute it
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    
	    editor.putString("help", "profilebot");
	    editor.commit();
	    
		startActivity(item.getIntent());
		return true;
	}


	
	
	
	

}
