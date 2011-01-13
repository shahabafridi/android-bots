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
import java.util.Vector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity of the AlarmBot layout
 * @author Chamika
 * 
 */
public class AlarmBot extends Main {

	String[] addedList;
	String selectedText;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateListView();
	}

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmbot);
		
		selectedText="";
		Button addAlarm=(Button) findViewById(R.id.Button_AlarmBotAddAlarm);
		Button removeAlarm = (Button) findViewById(R.id.Button_AlarmBotRemoveAlarm);
		
		//Initialize ListView 
		ListView RemoveList=(ListView) findViewById(R.id.ListView_Alarms);
		
		//Build an array of used locations 
		buildAddedList();
		
		//Add used locations to ListView
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.menu_item2, addedList);
        RemoveList.setAdapter(adapter);
        
        //Add OnItemClickListener to List View
      	RemoveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View itemClicked, int arg2,
					long arg3) {
				
				
				TextView textViewTemp = (TextView) itemClicked;
				selectedText = textViewTemp.getText().toString();
				
				Log.d("AlarmBot",selectedText + " Selected");
			}});
		
		//Add OnClickListeners to buttons
		addAlarm.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {  // onClick Method
        		btnAddAlarm(v);
        	}});
		
		removeAlarm.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {  // onClick Method
        		btnRemoveAlarm(v);
        	}});
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
	    
	    editor.putString("help", "alarmbot");
	    editor.commit();
	    
		startActivity(item.getIntent());
		return true;
	}

	/**
	 * Add Alarm button onClick method
	 * @param v Layout View
	 */
	public void btnAddAlarm(View v){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    
	    editor.putString("current", "alarm");
	    editor.commit();
	    startActivity(new Intent(AlarmBot.this,Locations.class));
	}
	
	/**
	 * Remove Alarm button onClick method
	 * @param v Layout View
	 */
	public void btnRemoveAlarm(View v){
		
		Log.d("AlarmBot",selectedText + " ready to delete");
		
		if(super.AllLocations.containsKey(selectedText)){
			ArrayList<Integer> DeletingCellIDs= super.AllLocations.get(selectedText);
			for(int i=0;i<DeletingCellIDs.size();i++){
				super.AlarmService.remove(DeletingCellIDs.get(i));
			}
		}
		
		updateListView();
	}
	
	
	/**
	 * Update Alarm Locations
	 */
	public void buildAddedList(){
		Vector<String> added=new Vector<String>();
		
		for(int i=0;i<super.AlarmService.size();i++){
			String location=super.CellID.get(super.AlarmService.get(i));
			if(!(added.contains(location))){
				added.add(location);
			}
		}
		
		addedList =new String[added.size()];
		for(int i=0;i<added.size();i++){
			addedList[i]=added.get(i);
		}
	}
	
	/**
	 * This method updates the added locations ListView
	 */
	public void updateListView(){
		buildAddedList();
		
		//Add used locations to ListView
		ListView RemoveList=(ListView) findViewById(R.id.ListView_Alarms);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.menu_item2, addedList);
        RemoveList.setAdapter(adapter);
	}

}
