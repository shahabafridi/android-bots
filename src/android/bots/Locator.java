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


import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Catches the cellID with separate Thread
 * @author Chamika
 *
 */
public class Locator extends Main implements Runnable{
	
	private TextView Status;
	private EditText Location;
//	private Thread thread;
	String LocationString;
	private Timer timer;
	private final int LOCATION_UPDATE_INTERVAL = 1000;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locator);
		
		Status=(TextView)findViewById(R.id.TextView_Status);
		Location=(EditText)findViewById(R.id.EditText_LocationName);
		
		Button buttonLocatorStart=(Button)findViewById(R.id.Button_StartSearch);
		Button buttonLocatorStop=(Button) findViewById(R.id.Button_StopSearch);
		
		
		buttonLocatorStart.setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {  // onClick Method
	    		btnLocatorStart(v);
	    	}});
		
		buttonLocatorStop.setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {  // onClick Method
	    		btnLocatorStop(v);
	    	}});
		
		timer=new Timer();
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
	    
	    editor.putString("help", "locator");
	    editor.commit();
	    
		startActivity(item.getIntent());
		return true;
	}


	
	/**
	 * Scan button onClick() method
	 * @param v Layout View
	 */
	public void btnLocatorStart(View v){
		LocationString= Location.getText().toString();
		
//		thread = new Thread(this);  //Old method
//		thread.start();	
		
		timer.scheduleAtFixedRate(new RunningLocator(this) , 0, LOCATION_UPDATE_INTERVAL);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		super.buildLocations();
		super.buildLocationArray();
	}

	/**
	 * Stop scan button onClick() method
	 * @param v Layout View
	 */
	public void btnLocatorStop(View v){
//		thread.stop(); //Old method
		
		if (timer != null) {
			timer.cancel();
			Toast.makeText(this, "Location finding Stopped", Toast.LENGTH_LONG).show();
		}
		
		Status.setText("Stopped");
	}

	/**
	 * perform Locator thread
	 * will be neglected after testing
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		//Messages are used for sending Thread event to the foreground activity
		Message ms=new Message();
			
		//Access telephone manager in the device
		TelephonyManager tm  = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
    	GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
        int cellid=location.getCid();
        
		ms.arg2=cellid;
		
        if(addID(cellid, LocationString)){
        	ms.arg1=1;
        }
        else{
        	ms.arg1=0;
        }
		
		handler.sendMessage(ms);
		
		
	}
	
	//Handler use to take the action when the message received
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Status.setText("Started to add " + Location.getText().toString() + "cell ids");
			if(msg.arg1 == 1){
				Status.setText("Added cellid " + msg.arg2 + " as " + LocationString);
			}
			else if(msg.arg1==0){
				Status.setText("Cellid " + msg.arg2 + " is already added.");
			}
		}
	};
	

}

/**
 * TimerTask for perform cellID catching and passing them to UI
 * @author Chamika
 *
 */
class RunningLocator extends TimerTask {

	Locator locator;
	GsmCellLocation location;
	
	/**
	 * Constructor
	 * @param loc Locator UI
	 */
	public RunningLocator(Locator loc){
		locator=loc;
		TelephonyManager tm  = (TelephonyManager) locator.getSystemService(Context.TELEPHONY_SERVICE); 
    	location = (GsmCellLocation) tm.getCellLocation();
	}
	
	/** Will be called when TimerTask starts */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		//Messages are used for sending Thread event to the foreground activity
		Message ms=new Message();
			
		//Access telephone manager in the device
		
        int cellid=location.getCid();
        
		ms.arg2=cellid;
		
        if(locator.addID(cellid, locator.LocationString)){
        	ms.arg1=1;
        	Log.d("Loc thread", "Location added to list");
        }
        else{
        	ms.arg1=0;
        }
		
		locator.handler.sendMessage(ms);
		
	}

}
