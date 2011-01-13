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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;


/**
 * This Class runs as the service of the application
 * @author Chamika
 *
 */
public class BotService extends Service {

	private static final String SAVENAME = "data"; //filename of the data saved file
	private final int timeInterval = 3000;   //Service updating interval
	public final int HIGH_BATTERY_LEVEL = 60; //High battery level for battery bot
	public final int LOW_BATTERY_LEVEL = 10; //Low battery level for battery bot
	public final int ALARM_DELAY = 10000; //Define delay to start alarm when destination reached

	private BroadcastReceiver batInfo;
	private int batteryLevel;
	public Timer timer;
	Hashtable<Integer, RuleProfile> profiles;
	Hashtable<String, RuleBattery> BatteryRules;
	Hashtable<Integer, String> CellIDNames;
	Vector<Integer> Alarms;

	/**
	 * getter method for batterylevel
	 * @return current battery level
	 */
	public int getBatteryLevel() {
		return batteryLevel;
	}

	/**
	 * Setter method for battery level
	 * @param batteryLevel batter level
	 */
	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		batInfo = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				int level = intent.getIntExtra("level", 0);
				int scale = intent.getIntExtra("scale", 100);
				batteryLevel = (level * 100) / scale ;
				
			}
		};

		this.registerReceiver(this.batInfo, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		// Load data stored in application UI to this service
		loadData();

		// Calling method to start the service
		startService();

		// Toast message to inform user whether service has started
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Loads all data saved by Application
	 */
	public void loadData() {

		// Try catch block to catch all the exceptions occur
		try {
			FileInputStream fis = this.openFileInput(SAVENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);

			// Retrieve serialized Vector object
			Vector temp = (Vector) ois.readObject();

			// Take alarm Vector from the serialized vector object
			Alarms = (Vector<Integer>) temp.get(2);

			// Take profile Hashtable from the vector object
			profiles = (Hashtable<Integer, RuleProfile>) temp.get(1);

			// Take cell id and names Hashtable
			CellIDNames = (Hashtable<Integer, String>) temp.get(0);

			// Take Battery Level and Rules from the Hashtable
			BatteryRules = (Hashtable<String, RuleBattery>) temp.get(3);

		} catch (FileNotFoundException e) {
			Log.d("Service", "FileNotFoundException in loadData()");
		} catch (IOException e) {
			Log.d("Service", "IOException in loadData()");
		} catch (ClassNotFoundException e) {
			Log.d("Service", "ClassNotFoundException in loadData()");
		}
	}

	/**
	 * start service using timer
	 */
	private void startService() {
		// start timer to retrieve cellID in time intervals
		timer = new Timer();
		// timeInterval integer sets cellid catching interval
		timer.scheduleAtFixedRate(new RunningTimer(this), 0, timeInterval);
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d("My Service", "started");

	}

	
	/**
	 * This method stops the timer to stop service
	 */
	public void stopService() {
		if (timer != null) {
			timer.cancel();
			unregisterReceiver(batInfo);
			Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		stopService(); // stop service when exiting service
	}

	/**
	 * Set profile settings such as ringer type and volume
	 * @param ringerType Ringer type of the phone. SILENT, VIBRATE, NORMAL
	 * @param volume Volume level of the phone. 
	 */
	public void setProfileSetting(int ringerType, int volume) {

		// Access system Audio Manager
		AudioManager am = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);

		am.setStreamVolume(AudioManager.STREAM_RING, volume, 1); // Set the
																	// volume

		// Set ringer mode of device
		if (ringerType == 0)
			am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		else if (ringerType == 1)
			am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		else
			am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

	}

	/**
	 * retrieve cellID for the service
	 */
	public int getCell() {
		// Access to telephone manager of the device
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// Make GSMCellLocation object
		GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
		// Retrieve CellID using GSMCellLocation object
		int cellid = location.getCid();

		return cellid;
	}

	/**
	 * perform battery level action
	 * @param batteryLevel required battery level to set 
	 */
	public void setBatteryConfig(String batteryLevel) {
		RuleBattery currentRule = BatteryRules.get(batteryLevel);

		setBrightness(currentRule.brightness);
		setTimeout(currentRule.timeout);

		if (!currentRule.wifi)
			offWifi();

		if (!currentRule.bluetooth)
			offBluetooth();

		if (!currentRule.gps)
			offGps();

		if (!currentRule.twog)
			setTwoG();

		if (!currentRule.dataSync)
			offDataSync();

	}

	/**
	 * Set alarm
	 * @param location Location name for setting up notification string
	 */
	public void setAlarm(String location) {
		
		//copy botalarm.mp3 resource file to memory card
		copyfile();

		Calendar cal;
		cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		
		Intent intent = new Intent(this,  OnetimeAlarmReceiver.class);
		intent.putExtra("Ringtone", Uri.parse("file:///sdcard/media/audio/ringtones/botalarm.mp3"));
		intent.putExtra("Location", location);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + ALARM_DELAY , pendingIntent); 
		
		Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
	}
	
	
	/**
	 * To write alarm to sdcard so that it can play when notification display
	 */
	public void copyfile(){
		byte[] buffer=null;  
		 InputStream fIn = getBaseContext().getResources().openRawResource(R.raw.botalarm);  
		 int size=0;  
		  
		 try {  
		  size = fIn.available();  
		  buffer = new byte[size];  
		  fIn.read(buffer);  
		  fIn.close();  
		 } catch (IOException e) {  
		  // TODO Auto-generated catch block  
		   
		 }  
		  
		 String path="/sdcard/media/audio/ringtones/";  
		 String filename="botalarm"+".mp3";  
		  
		 boolean exists = (new File(path)).exists();  
		 if (!exists){new File(path).mkdirs();}  
		  
		 FileOutputStream save;  
		 try {  
		  save = new FileOutputStream(path+filename);  
		  save.write(buffer);  
		  save.flush();  
		  save.close();  
		 } catch (Exception e) {  
		  // TODO Auto-generated catch block  
		    
		 } 
	}

	/**
	 * set screen brightness
	 * @param level Brightness level
	 */
	public void setBrightness(int level) {
		android.provider.Settings.System.putInt(getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS, level);
	}

	/**
	 * Set screen timeout
	 * @param duration Timeout duration of screen. Idle time of the phone
	 */
	public void setTimeout(int duration) {
		android.provider.Settings.System.putInt(getContentResolver(),
				android.provider.Settings.System.SCREEN_OFF_TIMEOUT, duration);
	}

	/**
	 * Turn off wifi
	 */
	public void offWifi() {
		WifiManager wifi;
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(false);
	}

	/**
	 * Turn off Bluetooth. specially working for Android 1.5(cupcake)
	 */
	public void offBluetooth() {
		//android.provider.Settings.System.putInt(getContentResolver(),android.provider.Settings.Secure.BLUETOOTH_ON, 0);
		BluetoothService_cupcake btService = new BluetoothService_cupcake();
		
		if(btService.isBluetoothEnabled()){
			btService.stopBluetooth();
		}

	}

	/**
	 * Turn off GPS
	 * Stopped implementing
	 */
	public void offGps() {
		//Turning off GPS programmatically is not supported with Android 1.5 or higher. It is deprecated.
	}

	/**
	 * Set 2G
	 * Stopped implementing
	 */
	public void setTwoG() {
		//Setting 2G network only programmatically is not supported with Android 1.5 or higher.  It is deprecated.
	}

	/**
	 * Disable data synchronization
	 */
	public void offDataSync() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// cm.setNetworkPreference(ConnectivityManager.TYPE_MOBILE);

		android.provider.Settings.System.putInt(getContentResolver(),
				android.provider.Settings.Secure.BACKGROUND_DATA, 0);

		Log.d("Service","background data =" + Boolean.toString(cm.getBackgroundDataSetting()));
	}

}

/**
 * TimerTask for cell ID continuous retrieval
 * @author Chamika
 *
 */
class RunningTimer extends TimerTask {
	BotService serve; // This object used to access service components through
						// the class

	// int previousCell=0;
	String previousLocation = "null";
	String currentLocation = "";
	String previousBatteryLevel = "";
	String currentBatteryLevel = "";

	/**
	 * starts when TimerTask enabled
	 */
	public void run() {

		// Retrieve current cellID
		int currentCellID = serve.getCell();

		// Testing the battery level
		Log.d("Service", "battery level= " + serve.getBatteryLevel());

		// Get the location of the current CellID if exists
		if (serve.CellIDNames.containsKey(currentCellID)) {
			currentLocation = serve.CellIDNames.get(currentCellID);
		}

		// Get DDMS log for location checking
		Log.d("Service", "Previous Location=" + previousLocation);
		Log.d("Service", "Current Location=" + currentLocation);

		// Perform if location changed
		if (!(currentLocation.equals(previousLocation))) {
			if (serve.profiles.containsKey(currentCellID)) {
				RuleProfile rp = (RuleProfile) serve.profiles
						.get(currentCellID);
				Log.d("Cell", "loaded rule which cellid= " + currentCellID);

				serve.setProfileSetting(rp.ringerType, rp.volume);
				previousLocation = currentLocation.toString();
			}

			if (serve.Alarms.contains(currentCellID)) {
				serve.setAlarm(currentLocation);
			}
		}

		// Perform battery changes
		if (serve.getBatteryLevel() >= serve.HIGH_BATTERY_LEVEL) {
			currentBatteryLevel = "High";
		} else if (serve.getBatteryLevel() >= serve.LOW_BATTERY_LEVEL) {
			currentBatteryLevel = "Medium";
		} else {
			currentBatteryLevel = "Low";
		}

		if (!(currentBatteryLevel.equals(previousBatteryLevel))) {
			previousBatteryLevel = currentBatteryLevel;
			serve.setBatteryConfig(currentBatteryLevel);
			Log.d("Service", "Battery config " + currentBatteryLevel
					+ " applied");
		} else {
			Log.d("Service", "No change in battery level");
		}

	}

	
	/**
	 * Constructor
	 * @param serv Access service methods within this class
	 */
	public RunningTimer(BotService serv) {
		serve = serv;
	}


}
