package org.usfirst.frc.team581.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
	public static void log(int key, Object logged) {
		SmartDashboard.putString("DB/String " + key, logged.toString());
	}

	public static void clear(){
		for(int i = 0; i < 8; i++){
			log(i, "");
		}
	}
}
