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

import java.io.Serializable;

import android.app.Activity;

/**
 * Parent class of RuleBattery and RuleProfile classes
 * @author Chamika
 *
 */
public class Rule extends Activity implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	int type;
	int ruleID;
	
	/**
	 * Constructor
	 */
	public Rule(){
		type=0;
		ruleID=0;
	}
	
	/**
	 * Constructor
	 * @param type Type of the rule
	 */
	public Rule(int type){
		this.type=type;
	}


}
