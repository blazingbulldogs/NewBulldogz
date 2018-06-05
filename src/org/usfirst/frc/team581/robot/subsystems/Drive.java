package org.usfirst.frc.team581.robot.subsystems;

import org.usfirst.frc.team581.robot.Ports;
import org.usfirst.frc.team581.robot.utilities.VirtualEncoder;
import org.usfirst.frc.team581.robot.utilities.VirtualEncoderOperation;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive extends Subsystem {
	/* final means that the variable cannot be reassigned: it's a constant.
	 * private means that other classes can't depend on the inner workings of this one,
	 * and instead have to use the methods it defines.
	 */
	
	// Spark is a type of motor controller
	final private Spark motorLeft = new Spark(Ports.leftMotor);
	final private Spark motorRight = new Spark(Ports.rightMotor);

	// DifferentialDrive controls motors based on inputs
	final private DifferentialDrive diffDrive = new DifferentialDrive(motorLeft, motorRight);

	// Encoders are hardware that measure how far or how fast each side of the
	// drive train has moved.
	final private Encoder encoderRight = new Encoder(
			Ports.rightEncoderChannelA,
			Ports.rightEncoderChannelB,
			Ports.rightEncoderReverse,
			Encoder.EncodingType.k4X);
	final private Encoder encoderLeft = new Encoder(
			Ports.leftEncoderChannelA,
			Ports.leftEncoderChannelB,
			Ports.leftEncoderReverse,
			Encoder.EncodingType.k4X);
	final private double INCHES_PER_PULSE = 1/18.9; // Measured by experiment

	// A PIDSource is anything that can provide data to a PID. Usually it's an encoder,
	// but we have custom logic to wrap both encoders. These will be configured more in the constructor.
	// We have three so we can swap between distance in auto and velocity in teleop.
	final private VirtualEncoder virtualEncoderRotation =
			new VirtualEncoder(encoderLeft, encoderRight); 
	final private VirtualEncoder virtualEncoderDistance =
			new VirtualEncoder(encoderLeft, encoderRight);
	final private VirtualEncoder virtualEncoderVelocity =
			new VirtualEncoder(encoderLeft, encoderRight);

	final private double kP = 0.005; // These constants are tuned by experiment
	final private double kD = 0.001; // We may need to use different constants for each PID
	final private double kI = 0.0;
	final private double TOLERANCE = 2.0; // inches
	// A PIDController runs the PID loop. "Controller" here has nothing to do with joysticks.
	final private PIDController pidRotation =
			new PIDController(kP, kI, kD, virtualEncoderRotation, new AngleWriter());
	final private PIDController pidDistance =
			new PIDController(kP, kI, kD, virtualEncoderDistance, new DistanceWriter());

	// These variables store the output of the PIDController. They are updated at different
	// times but we need both to drive the robot.
	private double rotation = 0.0;
	private double distance = 0.0;

	public Drive() {
	    motorLeft.setInverted(Ports.leftMotorInverted);
		motorRight.setInverted(Ports.rightMotorInverted);
		
		encoderLeft.setDistancePerPulse(INCHES_PER_PULSE);
		encoderRight.setDistancePerPulse(INCHES_PER_PULSE);
		
		virtualEncoderRotation.setOperation(VirtualEncoderOperation.DIFFERENCE);
		virtualEncoderRotation.setPIDSourceType(PIDSourceType.kDisplacement);
		virtualEncoderDistance.setOperation(VirtualEncoderOperation.AVERAGE);
		virtualEncoderDistance.setPIDSourceType(PIDSourceType.kDisplacement);
		virtualEncoderVelocity.setOperation(VirtualEncoderOperation.AVERAGE);
		virtualEncoderVelocity.setPIDSourceType(PIDSourceType.kRate);

    	pidRotation.setAbsoluteTolerance(TOLERANCE);
    	pidDistance.setAbsoluteTolerance(TOLERANCE);
    	    	
        stop();
	}
	
	public void stop(){
		encoderRight.reset();
		encoderLeft.reset();
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
	
	public boolean onTarget() {
		return pidRotation.onTarget() && pidDistance.onTarget();
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
