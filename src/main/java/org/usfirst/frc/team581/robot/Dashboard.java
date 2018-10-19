package org.usfirst.frc.team581.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
	public static void log(int key, Object logMe) {
		SmartDashboard.putString("DB/String " + key, logMe.toString());
	}
}
