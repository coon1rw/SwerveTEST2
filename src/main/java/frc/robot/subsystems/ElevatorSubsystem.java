package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {
    WPI_TalonFX motor = new WPI_TalonFX(14);
    WPI_TalonFX lift_motor = new WPI_TalonFX(0);
    Solenoid singleSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
    Joystick controller = new Joystick(0);
    double totalDistance = 0;
    public void elevator(){
        
        double speed = .15;
        double lspeed = .10;
        // time = distance / speed     time = 21.5in / 15.95 RPS
        // reset
        ///////////// remove comments on lift_motor.set(lspeed) if you want them to move with extender motor //////////
        if (controller.getRawButton(2) && (totalDistance != 0)){
        motor.set(-speed);
        //   lift_motor.set(lspeed);
        Timer.delay(totalDistance);
        System.out.println(totalDistance);
        totalDistance = 0;
        }
        //level 1
        if (controller.getRawButton(7) && ((totalDistance + 1) <= 3)){
        motor.set(speed);
        //   lift_motor.set(lspeed);
        Timer.delay(1);
        totalDistance += 1;
        } else {
            motor.set(0);
        }
        //level 2
        if (controller.getRawButton(9) && ((totalDistance + 2) <= 3)){
            motor.set(speed);
        //   lift_motor.set(lspeed);
            Timer.delay(2);
            totalDistance += 2;
        } 
        //level 3
        if (controller.getRawButton(11) && ((totalDistance + 3) <= 3)){
            motor.set(speed);
        //   lift_motor.set(lspeed);
            Timer.delay(3);
            totalDistance += 3;
        } 
        // Lift Motor  control using top
        if (controller.getPOV() == 180){
        lift_motor.set(-lspeed);
        }  else {
        lift_motor.set(0);
        }

        if (controller.getPOV() == 0) {
        lift_motor.set(lspeed);
        }
            }

}