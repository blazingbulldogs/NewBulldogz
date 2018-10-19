package org.usfirst.frc.team581.robot.utilities;

import edu.wpi.first.wpilibj.PIDOutput;

public class VirtualMotor implements PIDOutput {

	private double motorValue = 0.0;
	
	public double getValue() {
		return this.motorValue;
	}
	
	@Override
	public void pidWrite(double output) {
		motorValue = output;
	}
	
	public void reset() {
		motorValue = 0.0;
	}
}
