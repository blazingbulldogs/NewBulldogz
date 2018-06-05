package org.usfirst.frc.team581.robot.utilities;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/*
 * This class pretends to be an encoder but actually returns the average of 
 * two encoders' displacement. This indicates how far the robot has gone without
 * favoring one side or the other.
 */
public class PositionAveragePIDSource implements PIDSource {

	private Encoder enc1, enc2;
	
	public PositionAveragePIDSource(Encoder e1, Encoder e2) {
		enc1 = e1;
		enc2 = e2;
	}
	
	@Override
	public double pidGet() {
		return (enc1.getDistance() + enc2.getDistance()) / 2;
	}
	
	// These two methods are required to implement PIDSource, but are uninteresting.
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kRate;
	}

}
