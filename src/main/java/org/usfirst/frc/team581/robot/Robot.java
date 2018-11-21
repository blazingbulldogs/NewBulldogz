/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team581.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import java.util.Date;

import org.usfirst.frc.team581.robot.commands.DriveForward;
import org.usfirst.frc.team581.robot.commands.DriveWithGamepad;
import org.usfirst.frc.team581.robot.subsystems.Arm;
import org.usfirst.frc.team581.robot.subsystems.Drive;
import org.usfirst.frc.team581.robot.subsystems.Grabber;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
  // Do initialization in robotInit, since we don't override the constructor
  public static Drive drive;
  public static DriverControls driverControls;
  // The Compressor is not a subsystem, just the wpilib class. It's not very complex.
  public static Compressor compressor;
  public static Field field;
  public static Arm arm;
  public static Grabber grabber;

  Command m_autonomousCommand;
  Command m_teleopCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    drive = new Drive();
    driverControls = new DriverControls();
    compressor = new Compressor(Ports.compressor);
    compressor.start();
    arm = new Arm();
    grabber = new Grabber();

    // m_chooser.addDefault("Default Auto", new DriveForward(4));
    // chooser.addObject("My Auto", new MyAutoCommand());
    // SmartDashboard.putData("Auto mode", m_chooser);
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    if (m_teleopCommand != null) {
      m_teleopCommand.cancel();
    }
    drive.stop();
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    field = new Field(DriverStation.getInstance().getGameSpecificMessage());

    // When practicing, it's possible to go from teleop to autonomous
    if (m_teleopCommand != null) {
      m_teleopCommand.cancel();
    }

    m_autonomousCommand = new DriveForward(4);
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
    Logger.log(Ports.logDriveCommand, drive.getCurrentCommandName());
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    m_teleopCommand = new DriveWithGamepad();
    m_teleopCommand.start();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void testInit() {
    Logger.clear();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    drive.log();
    Logger.log(7, new Date());
  }
}
