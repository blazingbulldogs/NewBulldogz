package org.usfirst.frc.team581.robot.subsystems;

import org.usfirst.frc.team581.robot.Ports;
import org.usfirst.frc.team581.robot.utilities.PositionAveragePIDSource;
import org.usfirst.frc.team581.robot.utilities.PositionDifferencePIDSource;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive extends Subsystem {
	/* final means that the variable cannot be reassigned: it's a constant.
	 * private means that other classes can't depend on the inner workings of this one,
	 * and instead have to use the methods it defines.
	 */
	
	// Spark is a type of motor controller
	final private Spark m_left = new Spark(Ports.leftMotor);
	final private Spark m_right = new Spark(Ports.rightMotor);

	// DifferentialDrive controls motors based on inputs
	final private DifferentialDrive diffDrive = new DifferentialDrive(m_left, m_right);

	// Encoders are hardware that measure how far or how fast each side of the
	// drive train has moved.
	final private Encoder encR = new Encoder(
			Ports.rightEncoderChannelA,
			Ports.rightEncoderChannelB,
			Ports.rightEncoderReverse,
			Encoder.EncodingType.k4X);
	final private Encoder encL = new Encoder(
			Ports.leftEncoderChannelA,
			Ports.leftEncoderChannelB,
			Ports.leftEncoderReverse,
			Encoder.EncodingType.k4X);
	final private double INCHES_PER_PULSE = 1/18.9; // Measured by experiment

	// A PIDSource is anything that can provide data to a PID. Usually it's an encoder,
	// but we have custom logic to wrap both encoders in different ways (average, difference).
	final private PositionDifferencePIDSource rotatationTurned =
			new PositionDifferencePIDSource(encL, encR); // may need to switch order
	final private PositionAveragePIDSource distanceMoved =
			new PositionAveragePIDSource(encL, encR);

	final private double kP = 0.005; // These constants are tuned by experiment
	final private double kD = 0.001; // We may need to use different constants for each PID
	final private double kI = 0.0;
	final private double TOLERANCE = 2.0; // inches
	// A PIDController runs the PID loop. "Controller" here has nothing to do with joysticks.
	// "angle" refers to how far the robot has gone to one side.
	final private PIDController pidRotation =
			new PIDController(kP, kI, kD, rotatationTurned, new AngleWriter());
	final private PIDController pidDistance =
			new PIDController(kP, kI, kD, distanceMoved, new DistanceWriter());

	// These variables store the output of the PIDController. They are updated at different
	// times but we need both to drive the robot.
	private double rotation = 0.0;
	private double distance = 0.0;

	public Drive() {
	    m_left.setInverted(Ports.leftMotorInverted);
		m_right.setInverted(Ports.rightMotorInverted);
		
		encL.setDistancePerPulse(INCHES_PER_PULSE);
		encR.setDistancePerPulse(INCHES_PER_PULSE);

    	pidRotation.setAbsoluteTolerance(TOLERANCE);
    	pidDistance.setAbsoluteTolerance(TOLERANCE);
    	    	
        stop();
	}
	
	public void stop(){
		encR.reset();
		encL.reset();
		pidRotation.disable();
		pidDistance.disable();
		this.rotation = 0.0;
		this.distance = 0.0;
	}
	
	public void driveForwardDistance(double inches) {
		pidRotation.enable();
		pidDistance.enable();
		
		// The setpoint is the target value of the PID
		pidRotation.setSetpoint(0.0); // drive straight
		pidDistance.setSetpoint(inches);
	}
	
	/*
	 * Why is there a private class inside a class?
	 * 
	 * This "nested" class receives the output of a PID; basically it pretends to be a motor.
	 * When it receives output, it calls a method on the outer class which stores the value
	 * in a variable and drives the robot. The pattern is repeated twice: angle and distance.
	 */
	
	private class AngleWriter implements PIDOutput {
		@Override
		public void pidWrite(double output) {
			Drive.this.setRotation(output);
		}
	}

	private void setRotation(double newRotation) {
		this.rotation = newRotation;
		this.driveArcade();

	}
	
	private class DistanceWriter implements PIDOutput {
		@Override
		public void pidWrite(double output) {
			Drive.this.setDistance(output);
		}
	}
	
	private void setDistance(double newDistance) {
		this.distance = newDistance;
		this.driveArcade();
	}
	
	// The provided DifferentialDrive class already knows how to turn a distance and rotation.
	private void driveArcade() {
		diffDrive.arcadeDrive(distance, rotation);
	}

	// Subsystems are required to define this.
	// It's silly that it can't default to doing nothing.
	@Override
	protected void initDefaultCommand() {
	}
}
