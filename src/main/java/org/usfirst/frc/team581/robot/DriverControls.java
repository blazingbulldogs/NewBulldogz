/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team581.robot;

import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.buttons.Button;
// import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class DriverControls {
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());

  private Joystick gamepad1 = new Joystick(Ports.gamepad1);
  private Joystick gamepad2 = new Joystick(Ports.gamepad2);

  // We have two gamepads, each with two sticks, each with two axes.

  public double getDrivepadLeftX() {
    return snapToZero(gamepad1.getX());
  }

  public double getDrivepadLeftY() {
    return snapToZero(-gamepad1.getY());
  }

  public double getDrivepadRightX() {
    return snapToZero(gamepad1.getZ());
  }

  public double getDrivepadRightY() {
    return snapToZero(-gamepad1.getThrottle());
  }

  public double getControlpadLeftX() {
    return snapToZero(gamepad2.getX());
  }

  public double getControlpadLeftY() {
    return snapToZero(-gamepad2.getY());
  }

  public double getControlpadRightX() {
    return snapToZero(gamepad2.getZ());
  }

  public double getControlpadRightY() {
    return snapToZero(-gamepad2.getThrottle());
  }

  private double snapToZero(double rawInput) {
    if (Math.abs(rawInput) < 0.01) {
      return 0.0;
    } else {
      return rawInput;
    }
  }
}
