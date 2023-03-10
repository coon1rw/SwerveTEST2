package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PneumaticSubsystem extends SubsystemBase{
    
    Solenoid singleSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
    Joystick controller = new Joystick(0);

    public void pneumatic(){
        if(controller.getTrigger()){
            singleSolenoid.set(true);
        }
        else {
            singleSolenoid.set(false);
        }
    }

}
