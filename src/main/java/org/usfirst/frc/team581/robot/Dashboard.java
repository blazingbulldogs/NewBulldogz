package org.usfirst.frc.team581.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
	/**
	 * @param key Index of simple log item to log to
	 * @param logged Something with a `toString()` function to be logged
	 */
	public static void log(int key, Object logged) {
		SmartDashboard.putString("DB/String " + key, logged.toString());
	}

	/**
	 * Clear all log content 
	 */
	public static void clear(){
		for(int i = 0; i < 8; i++){
			log(i, "");
		}
	}
}
