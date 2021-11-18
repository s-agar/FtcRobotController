package org.firstinspires.ftc.teamcode.team10515.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.drivers.RevMotor;
import org.firstinspires.ftc.teamcode.team10515.states.ShooterStateMachine;

public class ShooterSubsystem implements ISubsystem<ShooterStateMachine, ShooterStateMachine.State> {

    private static ShooterStateMachine shooterStateMachine;
    private RevMotor ShooterWheel;
    private RevMotor ShooterWheel2;
    private double kP = (1/15600d);//was 10000d //14600d
    private double output = 0d;

    public ShooterSubsystem(RevMotor shooterMotor, RevMotor shooterMotor2){
        setShooterStateMachine(new ShooterStateMachine());
        setShooterWheel1(shooterMotor);
        setShooterWheel2(shooterMotor2);
    }

    @Override
    public ShooterStateMachine getStateMachine() {
        return shooterStateMachine;
    }

    @Override
    public ShooterStateMachine.State getState() {
        return getStateMachine().getState();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        getShooterWheel1().setPower(0d);
        getShooterWheel2().setPower(0d);

    }

    @Override
    public String getName() {
        return "Shooter Subsystem";
    }

    @Override
    public void writeToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update(double dt) {
        //calculate PID
        double error = getState().getSpeed() - getShooterWheel1().getVelocity();
        output = kP * error;

        //old code
        getStateMachine().update(dt);
        getShooterWheel1().setPower(output);
        getShooterWheel2().setPower(output);

    }

    private static void setShooterStateMachine(ShooterStateMachine ShooterStateMachine){
        ShooterSubsystem.shooterStateMachine = ShooterStateMachine;
    }

    private void setShooterWheel1(RevMotor shooterMotor){
        this.ShooterWheel = shooterMotor;
    }
    private void setShooterWheel2(RevMotor shooterMotor2){
        this.ShooterWheel2 = shooterMotor2;
    }

    public RevMotor getShooterWheel1(){
        return ShooterWheel;
    }
    public RevMotor getShooterWheel2(){
        return ShooterWheel2;
    }

    public double getOutput() {
        return output;
    }

}
