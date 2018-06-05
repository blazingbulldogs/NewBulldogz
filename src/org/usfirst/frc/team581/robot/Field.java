package org.usfirst.frc.team581.robot;

/*
 * Tells you which side of the scales and switch are ours,
 * relative to which side the robot starts on.
 */
public class Field {

	private String data;
	
	// The side of the field that the robot starts on.
	// Currently this should be changed by hand before each match.
	// Ideally this could be set in the driver's station on a hardware switch.
	private final char startingSide = 'R';

	public Field(String data) {
		this.data = data;
		if (data == null || data.length() != 3) {
			data = "xxx"; // placeholder if data is not what we expect
		}
	}
	
	public boolean nearSwitchOk() {
		// indices start at 0
		return data.charAt(0) == startingSide;
	}
	
	public boolean ScalehOk() {
		return data.charAt(1) == startingSide;
	}
	
	public boolean farSwitchOk() {
		return data.charAt(2) == startingSide;
	}
}
