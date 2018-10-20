/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team581.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import java.util.Random;

import org.usfirst.frc.team581.robot.Dashboard;
import org.usfirst.frc.team581.robot.Robot;

/**
 * Drive forward four feet, assuming everything works correctly.
 */
public class DriveForward extends Command {
	double m_feet;
	Random random;

	public DriveForward(double feet) {
		// Call the superclass constructor to do any work it needs to do for us.
		super();

		random = new Random();
		m_feet = feet;

		// Use requires() here to declare subsystem dependencies
		requires(Robot.drive);

		// Because the robot will likely be uncontrolled when we first run this code,
		// let it thrash for only two seconds.
		// setTimeout(2);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		Dashboard.log(2, "STARTING");
		Robot.drive.setDistanceMode(12 * m_feet);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		Dashboard.log(7, random.nextDouble());
		
		Robot.drive.driveForwardToDistance();
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return Robot.drive.onTarget();
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		Dashboard.log(2, "DONE");
		Robot.drive.stop();
	}
}
