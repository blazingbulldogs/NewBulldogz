package org.usfirst.frc.team581.robot.subsystems;

import org.usfirst.frc.team581.robot.Logger;
import org.usfirst.frc.team581.robot.Ports;
import org.usfirst.frc.team581.robot.utilities.VirtualEncoder;
import org.usfirst.frc.team581.robot.utilities.VirtualEncoderOperation;
import org.usfirst.frc.team581.robot.utilities.VirtualMotor;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive extends Subsystem {
	/*
	 * Drive subsystem - controls the wheels to move the robot
	 *
	 * Overview of how we drive straight: we have access to a PID, which is a system
	 * that hones in an input on a target value using an output. The input is taken
	 * from the encoders and can be either displacement or velocity; the output is
	 * fed to the motors. But instead of running left and right in two separate
	 * PIDs, we target a rotation and a displacement (auto) or velocity (teleop). To
	 * do this we use virtual encoders as PID inputs and virtual motors and PID
	 * outputs. Keep reading and this will make more sense.
	 *
	 * Java note: final means that the variable cannot be reassigned: it's a
	 * constant. private means that other classes can't depend on the inner workings
	 * of this one, and instead have to use the methods it defines.
	 */

	// Encoders are hardware that measure how far or how fast each side of the
	// drive train has moved.
	final private static Encoder encoderRight = new Encoder(Ports.rightEncoderChannelA, Ports.rightEncoderChannelB,
			Ports.rightEncoderReverse, Encoder.EncodingType.k4X);
	final private static Encoder encoderLeft = new Encoder(Ports.leftEncoderChannelA, Ports.leftEncoderChannelB,
			Ports.leftEncoderReverse, Encoder.EncodingType.k4X);
	final private double INCHES_PER_PULSE = 1 / 18.9; // Measured by experiment

	// A PIDSource is anything that can provide data to a PID. Usually it's an
	// encoder, but we have custom logic to wrap both encoders. These will be
	// configured more in the constructor.
	// We have three so we can swap between distance in auto and velocity in teleop.
	final private VirtualEncoder virtualEncoderRotation = new VirtualEncoder(encoderLeft, encoderRight);
	final private VirtualEncoder virtualEncoderDistance = new VirtualEncoder(encoderLeft, encoderRight);
	final private VirtualEncoder virtualEncoderVelocity = new VirtualEncoder(encoderLeft, encoderRight);

	// A PIDOutput is anything that can receive data from a PID. Usually it's a
	// motor, but these virtual motors will store the values until we need them.
	final private VirtualMotor virtualMotorRotation = new VirtualMotor();
	final private VirtualMotor virtualMotorDistance = new VirtualMotor();

	// A PIDController runs the PID. "Controller" here has nothing to do with
	// joysticks or any other hardware.
	final private PIDController pidRotation =
			// the P, I, and D constants are determined by experiment.
			// Each loop may need different values.
			new PIDController(0.05, 0.0, 0.001, virtualEncoderRotation, virtualMotorRotation);
	final private PIDController pidDistance = new PIDController(0.5, 0.0, 0.001, virtualEncoderDistance,
			virtualMotorDistance);
	final private PIDController pidVelocity = new PIDController(0.005, 0.0, 0.001, virtualEncoderVelocity,
			virtualMotorDistance);

	// Spark is the brand of motor controller
	final private static Spark motorLeft = new Spark(Ports.leftMotor);
	final private static Spark motorRight = new Spark(Ports.rightMotor);

	// DifferentialDrive controls motors based on inputs
	final private DifferentialDrive diffDrive;

	public Drive() {
		super();

		motorLeft.setInverted(Ports.leftMotorInverted);
		motorRight.setInverted(Ports.rightMotorInverted);
		diffDrive = new DifferentialDrive(motorLeft, motorRight);

		encoderLeft.setDistancePerPulse(INCHES_PER_PULSE);
		encoderRight.setDistancePerPulse(INCHES_PER_PULSE);

		virtualEncoderRotation.setOperation(VirtualEncoderOperation.DIFFERENCE);
		virtualEncoderRotation.setPIDSourceType(PIDSourceType.kDisplacement);
		virtualEncoderDistance.setOperation(VirtualEncoderOperation.AVERAGE);
		virtualEncoderDistance.setPIDSourceType(PIDSourceType.kDisplacement);
		virtualEncoderVelocity.setOperation(VirtualEncoderOperation.AVERAGE);
		virtualEncoderVelocity.setPIDSourceType(PIDSourceType.kRate);

		pidRotation.setAbsoluteTolerance(2.0); // inches of left/right difference
		pidDistance.setAbsoluteTolerance(2.0); // inches forward
		pidVelocity.setAbsoluteTolerance(0.5); // inches per second

		stop();
	}

	public void stop() {
		encoderRight.reset();
		encoderLeft.reset();
		pidRotation.disable();
		pidDistance.disable();
		pidVelocity.disable();
		virtualMotorRotation.reset();
		virtualMotorDistance.reset();
	}

	public void log() {
		Logger.log(Ports.logEncLeft, "encLeft: " + encoderLeft.get());
		Logger.log(Ports.logEncRight, "encRight: " + encoderRight.get());
		Logger.log(Ports.logVirtualMotorDistance, "vrtlMtrDist: " + virtualMotorDistance.getValue());
		Logger.log(Ports.logVirtualMotorRotation, "vrtlMtrRot: " + virtualMotorRotation.getValue());
	}

	// Call this once to initialize the PIDs to drive straight
	public void setDistanceMode(double inches) {
		pidRotation.enable();
		pidVelocity.disable();
		pidDistance.enable();

		// The setpoint is the target value of the PID
		pidRotation.setSetpoint(0.0); // drive straight
		pidDistance.setSetpoint(inches);
	}

	// After calling setDistanceMode, call this method until the target is reached.
	public void driveForwardToDistance() {
		log();
		diffDrive.arcadeDrive(virtualMotorDistance.getValue(), virtualMotorRotation.getValue());
	}

	// Call this once to initialize the PIDs to track velocity
	public void setVelocityMode() {
		pidRotation.enable();
		pidDistance.disable();
		pidVelocity.enable();

		pidRotation.setSetpoint(0.0);
		pidVelocity.setSetpoint(0.0);
	}

	// After calling setVelocityMode, call this method with operator input
	public void driveAtVelocity(double forward, double right) {
		diffDrive.arcadeDrive(forward, right);
	}

	// Call this to learn if the robot has reached its destination
	public boolean onTarget() {
		boolean rotationOk = !pidRotation.isEnabled() || pidRotation.onTarget();
		boolean distanceOk = !pidDistance.isEnabled() || pidDistance.onTarget();
		boolean velocityOk = !pidVelocity.isEnabled() || pidVelocity.onTarget();

		return rotationOk && distanceOk && velocityOk;
	}

	// Subsystems are required to define this.
	// It's silly that it can't default to doing nothing.
	@Override
	protected void initDefaultCommand() {
	}
}
