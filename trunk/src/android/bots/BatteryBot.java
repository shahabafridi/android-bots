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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

/**
 * Activity of the BatteryBot layout 
 * @author Chamika
 *
 */
public class BatteryBot extends Main {

	Spinner spinner;
	String previousSelection;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.batterybot);
		
		previousSelection = "High";
		
		spinner=(Spinner) findViewById(R.id.Spinner_BatteryLevel);
		
		ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this,R.array.arrBatteryLevels, android.R.layout.simple_spinner_item); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		loadView(previousSelection);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.d("Battery","Item" +arg2 +"selected");
				
				saveView(previousSelection);
				
				if(spinner.getSelectedItemPosition() == 2){
					previousSelection = "Low";
				}
				else if(spinner.getSelectedItemPosition() == 1){
					previousSelection = "Medium";
				}
				else{
					previousSelection = "High";
				}
				
				loadView(previousSelection);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
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
	    
	    editor.putString("help", "batterybot");
	    editor.commit();
	    
		startActivity(item.getIntent());
		return true;
	}
	
	/**
	 * This method updates the controllers of the UI
	 * @param selection Selected battery level
	 */
	public void loadView(String selection){
		SeekBar seekbar=(SeekBar) findViewById(R.id.SeekBar_ScreenBrightness);
		EditText screentime = (EditText) findViewById(R.id.EditText_ScreenTimeout);
		CheckBox checkbox_wifi= (CheckBox) findViewById(R.id.CheckBox_Wifi);
		CheckBox checkbox_bluetooth =(CheckBox) findViewById(R.id.CheckBox_Bluetooth);
		CheckBox checkbox_gps=(CheckBox) findViewById(R.id.CheckBox_GPS);
		CheckBox checkbox_twog=(CheckBox) findViewById(R.id.CheckBox_TwoG);
		CheckBox checkbox_datasync=(CheckBox) findViewById(R.id.CheckBox_DataSync);
		
		RuleBattery rule;
		
		if(BatteryService.containsKey(selection)){
			//Load rule for the selected battery level
			rule=BatteryService.get(selection);
		}
		else{
			//create default selection
			rule=new RuleBattery(10, 5000, true, true, true, true, true);
		}
		
		//Set UI values
		seekbar.setProgress(rule.brightness); 
		screentime.setText(Integer.toString(rule.timeout));
		checkbox_wifi.setChecked(rule.wifi);
		checkbox_bluetooth.setChecked(rule.bluetooth);
		checkbox_gps.setChecked(rule.gps);
		checkbox_twog.setChecked(rule.twog);
		checkbox_datasync.setChecked(rule.dataSync);
		
		Log.d("Battery","executed " + selection + " settings" );
		
	}
	
	/**
	 * This method saves the current state of the UI
	 * @param selection Selected battery level for saving
	 */
	public void saveView(String selection){
		
		SeekBar seekbar=(SeekBar) findViewById(R.id.SeekBar_ScreenBrightness);
		EditText screentime = (EditText) findViewById(R.id.EditText_ScreenTimeout);
		CheckBox checkbox_wifi= (CheckBox) findViewById(R.id.CheckBox_Wifi);
		CheckBox checkbox_bluetooth =(CheckBox) findViewById(R.id.CheckBox_Bluetooth);
		CheckBox checkbox_gps=(CheckBox) findViewById(R.id.CheckBox_GPS);
		CheckBox checkbox_twog=(CheckBox) findViewById(R.id.CheckBox_TwoG);
		CheckBox checkbox_datasync=(CheckBox) findViewById(R.id.CheckBox_DataSync);
		
		
		//For viewing in DDMS log
		Log.d("Batter","Seekbar:" + seekbar.getProgress());
		Log.d("Batter","Timeout:" + screentime.getText().toString());
		Log.d("Batter","wifi:" + checkbox_wifi.isChecked() );
		Log.d("Batter","bluetooth:" + checkbox_bluetooth.isChecked());
		Log.d("Batter","gps:"+checkbox_gps.isChecked());
		Log.d("Batter","2G:"+ checkbox_twog.isChecked());
		Log.d("Batter","data Sync:"+ checkbox_datasync.isChecked());
		
		//Create new battery rule for adding
		RuleBattery rule=new RuleBattery(seekbar.getProgress(), Integer.parseInt(screentime.getText().toString()),
				checkbox_wifi.isChecked(), checkbox_bluetooth.isChecked(), checkbox_gps.isChecked(), 
				checkbox_twog.isChecked(), checkbox_datasync.isChecked());
		
		if(selection.equals("Low")){
			if(BatteryService.containsKey("Low")){
				BatteryService.remove("Low");
			}
			BatteryService.put("Low", rule);
			Log.d("Batter","Low battery profile saved");
		}
		else if(selection.equals("Medium")){
			if(BatteryService.containsKey("Medium")){
				BatteryService.remove("Medium");
			}
			BatteryService.put("Medium", rule);
			Log.d("Batter","Medium battery profile saved");
		}
		else{
			if(BatteryService.containsKey("High")){
				BatteryService.remove("High");
			}
			BatteryService.put("High", rule);
			Log.d("Batter","High battery profile saved");
		}
		
	}

}
