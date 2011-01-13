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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Splash extends Main {
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        //loads data from serialized file
        loadData();
        
        //Create layout views       
        ImageButton buttonProfileBot=(ImageButton) findViewById(R.id.ImageButton_ProfileBot);
        ImageButton buttonAlarmBot=(ImageButton) findViewById(R.id.ImageButton_AlarmBot);
        ImageButton buttonBattteryBot=(ImageButton) findViewById(R.id.ImageButton_BatteryBot);
        ImageButton buttonLocator=(ImageButton)findViewById(R.id.ImageButton_Locator);
        Button buttonStart=(Button) findViewById(R.id.Button_SplashStart);
        Button buttonStop=(Button)findViewById(R.id.Button_SplashStop);
        
        
        buttonStop.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {  // onClick Method
        		btnSplashStop(v);
        	}});
        
        buttonStart.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {  // onClick Method
        		btnSplashStart(v);
        	}});
        
        buttonBattteryBot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {  // onClick Method
				btnSplashBatteryBot(v);		
							
			}
			});
        
        buttonProfileBot.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {  // onClick Method
    		btnSplashProfileBot(v);
        	}});
        
        buttonAlarmBot.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {  // onClick Method
        		btnSplashAlarmBot(v);
        	}});
        
        buttonLocator.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {  // onClick Method
        		btnSplashLocator(v);
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
	    
	    editor.putString("help", "main");
	    editor.commit();
	    
		startActivity(item.getIntent());
		return true;
	}


    
    /**
     * Locator button onClick() method
     * @param v Layout View
     */
    public void btnSplashLocator(View v){
    	startActivity(new Intent(Splash.this,Locator.class));
    }
    
    /**
     * ProfileBot button onClick() method
     * @param v Layout View
     */
    public void btnSplashProfileBot(View v){
    	startActivity(new Intent(Splash.this,ProfileBot.class));
    }
    
    /**
     * AlarmBot button onClick() method
     * @param v Layout View
     */
    public void btnSplashAlarmBot(View v){
    	startActivity(new Intent(Splash.this,AlarmBot.class));}
    
    /**
     * BatteryBot button onClick() method
     * @param v Layout View
     */
    public void btnSplashBatteryBot(View v){
    	startActivity(new Intent(Splash.this,BatteryBot.class));
    }
    
    /**
     * Start Bots button onClick() method
     * @param v Layout View
     */
    public void btnSplashStart(View v){    	
    	//start service after saving application data    	
    	saveData();
    	startService(new Intent(getApplicationContext(),BotService.class)); 
    
    }
    
    /**
     * Stop Bots button onClick() method
     * @param v Layout View
     */
    public void btnSplashStop(View v){
    	//stop service
    	stopService(new Intent(getApplicationContext(),BotService.class)); 
    }
    
}