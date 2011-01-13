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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
	
	
/**
 * Setting up Notification bar alarm
 */
public class OnetimeAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "Alarm activated", Toast.LENGTH_LONG).show();
		
		NotificationManager manger = (NotificationManager)     context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, "Wake up alarm", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, Main.class), 0);
		notification.setLatestEventInfo(context, "Android Bots", intent.getExtras().get("Location") + " reached", contentIntent);
		notification.flags = Notification.FLAG_INSISTENT;

		notification.sound = intent.getParcelableExtra("Ringtone");
		notification.vibrate = (long[]) intent.getExtras().get("vibrationPatern");

		// The PendingIntent to launch our activity if the user selects this notification
		manger.notify(0, notification);
	}
	
}
	
	

	
