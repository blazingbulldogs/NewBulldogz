package org.usfirst.frc.team581.robot.commands;

import org.usfirst.frc.team581.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class GrabberManual extends Command {

  public GrabberManual() {
    super();
    requires(Robot.grabber);
  }
  
  @Override
  protected void execute() {
    double control = Robot.driverControls.getControlpadRightY();
    Robot.grabber.grab(control);
  }
  
  @Override
  protected boolean isFinished() {
    return false;
  }

}
