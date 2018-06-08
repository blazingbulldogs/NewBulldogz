package org.usfirst.frc.team581.robot.subsystems;

import org.usfirst.frc.team581.robot.Dashboard;
import org.usfirst.frc.team581.robot.Ports;
import org.usfirst.frc.team581.robot.Robot;
//import org.usfirst.frc.team581.robot.commands.ArmAngle;
//import org.usfirst.frc.team581.robot.commands.ArmDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Arm extends Subsystem {
	private TalonSRX talonLeader = new TalonSRX(Ports.talon1);
	private TalonSRX talonFollower = new TalonSRX(Ports.talon2);

	// Talons can store 4 slots of PID config values, but we're only using one.
	private static final int slotIdx = 0;

	// Talons can run two PID loops concurrently, but we're only using the primary.
	private static final int pidLoopIdx = 0;

	// Wait 10ms for commands to time out because, e.g. the talon was unreachable
	// In this case a message to the driver console will be printed
	// All config commands are done in the constructor, i.e. on robot boot
	private static final int kTimeoutMs = 10;

	public Arm() {
		talonLeader.setInverted(Ports.talon1Inverted);
		talonFollower.setInverted(Ports.talon2Inverted);

		// Indicate the type of sensor we want to run in the PID loop
		talonLeader.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, pidLoopIdx, kTimeoutMs);

		// Correct for the sensor being out of phase, e.g. encoder values is increasing
		// with positive motor rotation
		talonLeader.setSensorPhase(Ports.talonSensorPhase);

		// These are two ways to configure tolerance: not sure which is better.
		// Will need to experiment.
		// talonLeader.configNeutralDeadband(0.02, kTimeoutMs);
		// talonLeader.configAllowableClosedloopError(0, Math.round(0.02f * 4096),
		// kTimeoutMs);

		// Soft limits will prevent the motor from turning past a certain angle.
		// In theory this is great: make sure we don't ram the arm into the robot.
		// But for debugging, we're disabling these limits.
		// talonLeader.configReverseSoftLimitThreshold(2000, kTimeoutMs);
		// talonLeader.configForwardSoftLimitThreshold(4400, kTimeoutMs);
		talonLeader.configForwardSoftLimitEnable(false, kTimeoutMs);
		talonLeader.configReverseSoftLimitEnable(false, kTimeoutMs);

		// Run the config in our slot on our chosen PID loop.
		talonLeader.selectProfileSlot(slotIdx, pidLoopIdx);
		// Configure PID constants. Start with P, then D, then I.
		talonLeader.config_kP(slotIdx, 2.4, kTimeoutMs);
		talonLeader.config_kD(slotIdx, 0.0, kTimeoutMs);
		talonLeader.config_kI(slotIdx, 0.0, kTimeoutMs);
		// feed-forward gain: not used for position control
		talonLeader.config_kF(slotIdx, 0.0, kTimeoutMs);

		// Set the time to ramp the output (seconds) - avoid jerky motors
		talonLeader.configClosedloopRamp(0.25, kTimeoutMs);

		talonFollower.follow(talonLeader);
	}

	public void driveArm() {
		double control = Robot.driver_controls.getGamepadRightY();
		if (Math.abs(control) < 0.98) {
			control /= 4.0; // limit speed unless at full throttle
		}
		talonLeader.set(ControlMode.PercentOutput, control);
		this.log("@ " + Math.round(control * 100) + "%");
	}

	public void setAngle(int angle) {
		talonLeader.set(ControlMode.Position, angle);
		this.log("-> " + angle);
	}

	private void log(String logInfo) {
		Dashboard.log(Ports.logArm, "arm: " + pulseWithPosition() + " " + logInfo);
	}

	private int pulseWithPosition() {
		// & 0xFFF means only use the bits that are relevant
		return talonLeader.getSensorCollection().getPulseWidthPosition() & 0xFFF;
	}

	@Override
	protected void initDefaultCommand() {
	}

}
