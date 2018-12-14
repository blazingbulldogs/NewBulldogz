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
  public static final int gamepad1 = 0;
  public static final int gamepad2 = 1;

  public static final int leftMotor = 1;
  public static final int rightMotor = 0;
  public static final boolean leftMotorInverted = false;
  public static final boolean rightMotorInverted = false;

  public static final int rightEncoderChannelA = 2;
  public static final int rightEncoderChannelB = 3;
  public static final boolean rightEncoderReverse = false;

  public static final int leftEncoderChannelA = 0;
  public static final int leftEncoderChannelB = 1;
  public static final boolean leftEncoderReverse = true;

  public static final int grabLeftMotor = 3;
  public static final int grabRightMotor = 4;
  public static final boolean grabLeftMotorInverted = false;
  public static final boolean grabRightMotorInverted = true;

  public static final int solenoid1 = 0;
  public static final int solenoid2 = 1;

  public static final int compressor = 0;

  // TODO: The default ID is 0. It is recommended to not use 0 in case you add
  // more talons.
  public static final int talon1 = 0;
  public static final int talon2 = 1;
  public static final boolean talon1Inverted = false;
  public static final boolean talon2Inverted = true;
  public static final boolean talonSensorPhase = false;

  // I want to assign each subsystem its own slot(s) to log into.
  // Expand this as we need more logging.
  public static final int logArm = 4;
  public static final int logEncLeft = 0;
  public static final int logEncRight = 5;
  public static final int logVirtualMotorDistance = 1;
  public static final int logVirtualMotorRotation = 6;
  public static final int logDriveCommand = 2;

}
