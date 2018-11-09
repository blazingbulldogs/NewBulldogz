package org.usfirst.frc.team581.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team581.robot.Ports;
import org.usfirst.frc.team581.robot.commands.GrabberManual;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Grabber extends Subsystem {
  // TODO: What do these solenoids do? Can they be given better names?
  private Solenoid solenoid1 = new Solenoid(Ports.solenoid1);
  private Solenoid solenoid2 = new Solenoid(Ports.solenoid2);

  // These motors are never used together with the solenoids.
  // Either we need to write a more sophisticated method, or we have two classes.
  private Spark grabberLeftMotor = new Spark(Ports.grabLeftMotor);
  private Spark grabberRightMotor = new Spark(Ports.grabRightMotor);
  private SpeedControllerGroup grabberMotors = new SpeedControllerGroup(grabberLeftMotor, grabberRightMotor);

  public Grabber() {
    super();
    grabberLeftMotor.setInverted(Ports.grabLeftMotorInverted);
    grabberRightMotor.setInverted(Ports.grabRightMotorInverted);
  }

  protected void initDefaultCommand() {
    setDefaultCommand(new GrabberManual());
  }

  public void setSolenoid1(boolean state) {
    solenoid1.set(state);
  }
  
  public void setSolenoid2(boolean state) {
    solenoid2.set(state);
  }

  // final public double GRABBER_POWER = 0.65;
  // Apparently we needed adjustable power so this was made a parameter?

  public void grab(double grabberPower) {
    grabberMotors.set(grabberPower);
  }

  public void eject(double grabberPower) {
    grabberMotors.set(-grabberPower);
  }

  private double limit(double power) {
    final double minPower = 0.4;
    if (power < 0) {
      return clamp(-1.0, power, -minPower);
    } else {
      return clamp(minPower, power, 1.0);
    }
  }

  private double clamp(double lo, double x, double hi) {
    return Math.min(Math.max(lo, x), hi);
  }

  public void grabPower(double GPOWER, double TPOWER) {
    double leftPower = limit(GPOWER / 2 + TPOWER / 4);
    double rightPower = limit(GPOWER / 2 + TPOWER / 4);
    grabberLeftMotor.set(leftPower);
    grabberLeftMotor.set(rightPower);
  }

  public void stopMotor() {
    grabberMotors.set(0);
  }

}
