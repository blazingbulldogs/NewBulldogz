package org.usfirst.frc.team581.robot.commands;

import org.usfirst.frc.team581.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ArmManual extends Command {

  public ArmManual() {
    super();
    requires(Robot.arm);
  }

  @Override
  protected void execute() {
    double control = Robot.driverControls.getControlpadLeftY();
    Robot.arm.driveArm(control);
  }
  
  @Override
  protected boolean isFinished() {
    return false;
  }
}
