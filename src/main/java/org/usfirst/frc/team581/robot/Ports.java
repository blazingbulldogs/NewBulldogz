/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team581.robot;

/**
 * This class provides meaningful names for motors, pneumatic valves, and so on.
 */
public class Ports {
	/* 
	 * final public means that anyone can read these values, but not rewrite them.
	 * static means that the value can be read from the class, like Ports.leftMotor,
	 * without instantiating it by calling "new Ports()".
	 */
	final public static int gamepad1 = 0;
	final public static int gamepad2 = 1;

	final public static int leftMotor = 1;
	final public static int rightMotor = 0;
	final public static boolean leftMotorInverted = false;
	final public static boolean rightMotorInverted = false;
	
	final public static int rightEncoderChannelA = 2;
	final public static int rightEncoderChannelB = 3;
	final public static boolean rightEncoderReverse = false;
	
	final public static int leftEncoderChannelA = 0;
	final public static int leftEncoderChannelB = 1;
	final public static boolean leftEncoderReverse = true;
	
	final public static int grabLeftMotor = 3;
	final public static int grabRightMotor = 4;
	final public static boolean grabLeftMotorInverted = false;
	final public static boolean grabRightMotorInverted = true;
	
	final public static int solenoid1 = 0;
	final public static int solenoid2 = 1;

	final public static int compressor = 0;

	// TODO: The default ID is 0. It is recommended to not use 0 in case you add more talons.
	final public static int talon1 = 0;
	final public static int talon2 = 1;
	final public static boolean talon1Inverted = false;
	final public static boolean talon2Inverted = true;
	final public static boolean talonSensorPhase = false;

	// I want to assign each subsystem its own slot(s) to log into.
	// Expand this as we need more logging.
	final public static int logArm = 4;
	final public static int logEncLeft = 0;
	final public static int logEncRight = 5;
	final public static int logVirtualMotorLeft = 1;
	final public static int logVirtualMotorRight = 6;
	final public static int logDriveCommand = 2;


}
