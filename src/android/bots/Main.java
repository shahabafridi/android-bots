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


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * Consists overall application data and data saving methods
 * Parent class of all UI classes
 * @author Chamika
 * 
 */
public class Main extends Activity {
	
	public static final String PREFS_NAME = "MyPrefsFile"; //Use for shared preferences

	public static final String SAVENAME = "data"; //filename of the data storing file
	
	public static Hashtable<Integer, String> CellID=new Hashtable<Integer, String>();
	public static Hashtable<String, ArrayList<Integer>> AllLocations=new Hashtable<String, ArrayList<Integer>>();
	public static Hashtable<Integer,RuleProfile> ProfileService =new Hashtable<Integer,RuleProfile>();
	public static Hashtable<String,RuleBattery> BatteryService=new Hashtable<String, RuleBattery>();
	public static Vector<Integer> AlarmService = new Vector<Integer>();
	public static String[] allLocations;
	
	public boolean addID(int cellid, String locationName){
					
		CellID.put(cellid, locationName);
		return true;
	}
	
	
	/**
	 * Make the allLocation[] which is used to add locations for the ListView of Locations class
	 */
	public void buildLocationArray(){
				
		//Get DDMS out for checking AllLocation array data
		Enumeration<String> e = AllLocations.keys();
		int i=0;
		while(e.hasMoreElements()){
			i++;
			Log.d("AllLocation arrAy",e.nextElement());
		}
		
		allLocations=new String[i];
		
		e = AllLocations.keys();
		i=0;
		
		while(e.hasMoreElements()){
			allLocations[i]=e.nextElement();			
			i++;	
		}
		
	}
	
	/**
	 * update Hashtable AllLocations with Location name 
	 */
	public void buildLocations(){
		AllLocations=new Hashtable<String, ArrayList<Integer>>();
		
		Enumeration<Integer> w = CellID.keys();
		int z=0;
		while(w.hasMoreElements()){
			Log.d("CellID element",w.nextElement().toString());
			z++;
		}
		Log.d("CellID","CellID Coung="+z);
		
		Enumeration<Integer> e = CellID.keys();
		    
	    while(e.hasMoreElements()){
	      int key=e.nextElement();
	      String value=CellID.get(key);
	     
	      if(AllLocations.containsKey(value)){
	    	  ArrayList<Integer> temp=AllLocations.get(value);
	    	  temp.add(key);
	    	  AllLocations.put(value, temp);	    	  
	      }
	      else{
	    	  ArrayList<Integer> temp=new ArrayList<Integer>();
	    	  temp.add(key);
	    	  AllLocations.put(value, temp);
	      }
	    	  
	      Log.d("buildLocation", "Locations hashtable created successfully");
	    }
		
		
	}
	
	/**
	 * save the application settings in the filename "SAVENAME"
	 */
	public void saveData(){
		
		buildLocations();
		buildLocationArray();
		
		Vector temp=new Vector();
		
		temp.add(CellID);
		temp.add(ProfileService);
		temp.add(AlarmService);
		temp.add(BatteryService);
		
		
		FileOutputStream fos;
		//Try-catch block for exception handling
		try {
			fos = this.openFileOutput(SAVENAME, Context.MODE_PRIVATE);
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(fos);
			oos.writeObject(temp); 
	    	oos.close();
	    	Log.d("Serialization", "Completed sucessully");
			}	    	
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d("FOS", "File Not found");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("FOS", "IOException");
		}
	}
	
	
	/**
	 * Retrieve the application settings from the filename "SAVENAME". Data are stored in a serialized vector
	 */
	public void loadData(){
		
		try {
			FileInputStream fis = this.openFileInput(SAVENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Vector<?> temp= (Vector<?>) ois.readObject();
			CellID = (Hashtable<Integer, String>) temp.get(0);
			ProfileService = (Hashtable<Integer, RuleProfile>) temp.get(1);
			AlarmService = (Vector<Integer>) temp.get(2);
			BatteryService= (Hashtable<String, RuleBattery>) temp.get(3);
						
		} catch (Exception e) {
			CellID = new Hashtable<Integer, String>();
			ProfileService =new Hashtable<Integer, RuleProfile>();
			AlarmService = new Vector<Integer>();
			BatteryService= new Hashtable<String, RuleBattery>();
		}
		
		//update application variables with loaded data
		buildLocations();
		buildLocationArray();
	}
}
