package org.firstinspires.ftc.teamcode.team10515.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.drivers.RevMotor;
import org.firstinspires.ftc.teamcode.team10515.states.IntakeMotorStateMachine;

public class IntakeMotorSubsystem implements ISubsystem<IntakeMotorStateMachine, IntakeMotorStateMachine.State> {
    private static IntakeMotorStateMachine intakeMotorStateMachine;
    private RevMotor intakeWheels;
    private double kP = (1/5000d);
    private double output = 0d;

    public IntakeMotorSubsystem(RevMotor intakeMotor){//RevMotor leftFlywheel, RevMotor rightFlywheel) {
        setIntakeMotorStateMachine(new IntakeMotorStateMachine());
       setIntakeWheels(intakeMotor);
//        setLeftFlywheel(leftFlywheel);
//        setRightFlywheel(rightFlywheel);
    }

    @Override
    public IntakeMotorStateMachine getStateMachine() {
        return intakeMotorStateMachine;
    }

    @Override
    public IntakeMotorStateMachine.State getState() {
        return getStateMachine().getState();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        getIntakeWheels().setPower(0d);
    }

    @Override
    public void writeToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update(double dt) {
        double error = getState().getSpeed() - getIntakeWheels().getVelocity();
        output = kP * error;
        getStateMachine().update(dt);
        //getIntakeWheels().setPower(getState().getSpeed());
        getIntakeWheels().setPower(output);
    }

    @Override
    public String getName() {
        return "Flywheel Subsystem";
    }

    private static void setIntakeMotorStateMachine(IntakeMotorStateMachine intakeMotorStateMachine) {
        IntakeMotorSubsystem.intakeMotorStateMachine = intakeMotorStateMachine;
    }

    public double getOutput() {
        return output;
    }

    private void setIntakeWheels(RevMotor intakeMotor){
        this.intakeWheels = intakeMotor;
    }
    private RevMotor getIntakeWheels(){
        return intakeWheels;
    }
}
