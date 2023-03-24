// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  public static CTREConfigs ctreConfigs;

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    ctreConfigs = new CTREConfigs();
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();


    motor.configFactoryDefault();
		
		/* Config the sensor used for Primary PID and sensor direction */
    motor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 
                                            Constants.kPIDLoopIdx,
				                            Constants.kTimeoutMs);

		/* Ensure sensor is positive when output is positive */
		motor.setSensorPhase(Constants.kSensorPhase);

		/**
		 * Set based on what direction you want forward/positive to be.
		 * This does not affect sensor phase. 
		 */ 
		motor.setInverted(Constants.kMotorInvert);
		/*
		 * Talon FX does not need sensor phase set for its integrated sensor
		 * This is because it will always be correct if the selected feedback device is integrated sensor (default value)
		 * and the user calls getSelectedSensor* to get the sensor's position/velocity.
		 * 
		 * https://phoenix-documentation.readthedocs.io/en/latest/ch14_MCSensor.html#sensor-phase
		 */
        // motor.setSensorPhase(true);

		/* Config the peak and nominal outputs, 12V means full */
		motor.configNominalOutputForward(0, Constants.kTimeoutMs);
		motor.configNominalOutputReverse(0, Constants.kTimeoutMs);
		motor.configPeakOutputForward(0.2, Constants.kTimeoutMs);
		motor.configPeakOutputReverse(-0.1, Constants.kTimeoutMs);

		/**
		 * Config the allowable closed-loop error, Closed-Loop output will be
		 * neutral within this range. See Table in Section 17.2.1 for native
		 * units per rotation.
		 */
		motor.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

		/* Config Position Closed Loop gains in slot0, tsypically kF stays zero. */
		motor.config_kF(Constants.kPIDLoopIdx, Constants.kGains.kF, Constants.kTimeoutMs);
		motor.config_kP(Constants.kPIDLoopIdx, Constants.kGains.kP, Constants.kTimeoutMs);
		motor.config_kI(Constants.kPIDLoopIdx, Constants.kGains.kI, Constants.kTimeoutMs);
		motor.config_kD(Constants.kPIDLoopIdx, Constants.kGains.kD, Constants.kTimeoutMs);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    motor.set(ControlMode.Position, startTargetPosition);
  }

  @Override
  public void disabledPeriodic() {
    motor.set(ControlMode.Position, startTargetPosition);
  }

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }

    //shooter
    startTargetPosition = motor.getSelectedSensorPosition();
    targetPosition = 9235 + startTargetPosition; 
    motor.set(ControlMode.Position, targetPosition); 
    Timer.delay(1);
    shooter.set(.45);
    shooter1.set(-.5);
    Timer.delay(1);
    shooter.set(0);
    shooter1.set(0);
    motor.set(ControlMode.Position, startTargetPosition);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  double startTargetPosition;
  double targetPosition;

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    startTargetPosition = motor.getSelectedSensorPosition();
    motor.setNeutralMode(kBrakeDurNeutral);
    shooter.setNeutralMode(kBrakeDurNeutral);
    shooter1.setNeutralMode(kBrakeDurNeutral);

    motor.set(ControlMode.Position, startTargetPosition);
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  WPI_TalonFX motor = new WPI_TalonFX(16);
  WPI_TalonFX shooter = new WPI_TalonFX(15);
  WPI_TalonFX shooter1 = new WPI_TalonFX(14);
  NeutralMode kBrakeDurNeutral = NeutralMode.Brake;

  Joystick joystick1 = new Joystick(1);
  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

  if(joystick1.getRawButton(1)){
    shooter.set(-.25);
  } else {
    shooter.set(0);
    shooter1.set(0);
  }
  if(joystick1.getRawButton(2)){
    shooter.set(.45);
    shooter1.set(-.5);
  }

//arm
  //down
  if (joystick1.getRawButton(7)){
    targetPosition = -25000 + startTargetPosition;
    motor.set(ControlMode.Position, targetPosition);
  }



//up
  if (joystick1.getRawButton(9)){
    //motor.set(.2);
    targetPosition = startTargetPosition;
    motor.set(ControlMode.Position, targetPosition);
  }
// level 2
  if(joystick1.getRawButton(11)){
    targetPosition = 9250 + startTargetPosition; 
    motor.set(ControlMode.Position, targetPosition);
  }
  }
  

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}