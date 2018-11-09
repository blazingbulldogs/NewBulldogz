package org.usfirst.frc.team581.robot.commands;

import org.usfirst.frc.team581.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveWithGamepad extends Command {

  public DriveWithGamepad() {
    super();
    requires(Robot.drive);
  }

  @Override
  protected void initialize() {
    Robot.drive.setVelocityMode();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.drive.driveAtVelocity(Robot.driverControls.getDrivepadRightY(), Robot.driverControls.getDrivepadLeftX());
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.drive.stop();
  }

}
