package org.usfirst.frc.team972;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
	WPI_TalonSRX r1 = new WPI_TalonSRX(1);
	WPI_TalonSRX r2 = new WPI_TalonSRX(2);
	WPI_TalonSRX r3 = new WPI_TalonSRX(3);
	WPI_TalonSRX l1 = new WPI_TalonSRX(4);
	WPI_TalonSRX l2 = new WPI_TalonSRX(5);
	WPI_TalonSRX l3 = new WPI_TalonSRX(6);
	
	Joystick joy = new Joystick(0);

	@Override
	public void robotInit() {
		
	}
	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		r1.set(ControlMode.PercentOutput, joy.getRawAxis(5));
		r2.set(ControlMode.PercentOutput, joy.getRawAxis(5));
		r3.set(ControlMode.PercentOutput, joy.getRawAxis(5));
		l1.set(ControlMode.PercentOutput, -joy.getRawAxis(1));
		l2.set(ControlMode.PercentOutput, -joy.getRawAxis(1));
		l3.set(ControlMode.PercentOutput, -joy.getRawAxis(1));
	}

}
