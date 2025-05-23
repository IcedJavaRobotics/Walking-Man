// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.arm.ClockwiseArmCommand;
import frc.robot.commands.arm.CounterClockwiseArmCommand;
import frc.robot.commands.arm.DanceCommand;
import frc.robot.commands.arm.LeftArmCommand;
import frc.robot.commands.arm.RightArmCommand;
import frc.robot.commands.arm.WaveCommand;
import frc.robot.subsystems.*;


import static frc.robot.Constants.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

  private final TankSubsystem tankSubsystem = new TankSubsystem();
  //private final ArmSubsystem armSubsystem = new ArmSubsystem();
  private final RightArmSubsystem rightArmSubsystem = new RightArmSubsystem();
  private final LeftArmSubsystem leftArmSubsystem = new LeftArmSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
private final XboxController driverController = new XboxController(DRIVER_CONTROLLER_PORT);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    tankSubsystem.setDefaultCommand(
      new RunCommand(() -> tankSubsystem.arcadeDrive(getJoystickY(), -getJoystickX(), getRightTriggerValue()), tankSubsystem)
    );
    // Configure the trigger bindings
    configureBindings();
  }

  

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    new JoystickButton(driverController, XboxController.Button.kRightBumper.value)
                .whileTrue(new RightArmCommand(rightArmSubsystem, 0.8));
    new JoystickButton(driverController, XboxController.Button.kLeftBumper.value)
                .whileTrue(new LeftArmCommand(leftArmSubsystem, 1));

    new Trigger(()-> getLeftTriggerValue()).whileTrue(new LeftArmCommand(leftArmSubsystem, -0.48));
    new Trigger(() -> getRightTriggerValue()).whileTrue(new RightArmCommand(rightArmSubsystem, -0.3));

    // new JoystickButton(driverController, XboxController.Button.kX.value)
    //             .whileTrue(new ClockwiseArmCommand(leftArmSubsystem, rightArmSubsystem));
    // new JoystickButton(driverController, XboxController.Button.kY.value)
    //             .whileTrue(new CounterClockwiseArmCommand(leftArmSubsystem, rightArmSubsystem));


    new JoystickButton(driverController, XboxController.Button.kB.value)
                .whileTrue(new DanceCommand(leftArmSubsystem, rightArmSubsystem));
    new JoystickButton(driverController, XboxController.Button.kA.value)
                .whileTrue(new WaveCommand(rightArmSubsystem));

    new JoystickButton(driverController, XboxController.Button.kStart.value).whileTrue(tankSubsystem.setNormalCommand());
    new JoystickButton(driverController, XboxController.Button.kBack.value).whileTrue(tankSubsystem.setSlowCommand());
    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    // m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }

  private double deadZoneMod(double val) {
    if (Math.abs(val) <= Constants.DEADZONE) {
      return 0;
    } else {
      return ((Math.abs(val) - 0.2) * 1.25) * (val/Math.abs(val));
    }
  }

  public double getJoystickX() {
    if ( driverController != null ) {
      return deadZoneMod(driverController.getRightX());
    } else {
      return 0;
    }
  }

  public boolean getRightTriggerValue(){
    if ( driverController != null ) {
      if(driverController.getRightTriggerAxis() >= 0.5){
        return true;
      }
      return false;
    } else {
      return false;
    }
  }
  public boolean getLeftTriggerValue(){
    if ( driverController != null ) {
      if(driverController.getRightTriggerAxis() >= 0.5){
        return true;
      }
      return false;
    } else {
      return false;
    }
  }

  public double getJoystickY() {
    if ( driverController != null ) {
      return deadZoneMod(driverController.getLeftY());
    } else {
      return 0;
    }
  }

}
