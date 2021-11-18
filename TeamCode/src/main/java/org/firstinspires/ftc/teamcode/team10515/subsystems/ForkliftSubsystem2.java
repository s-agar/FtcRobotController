package org.firstinspires.ftc.teamcode.team10515.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.drivers.RevMotor;
import org.firstinspires.ftc.teamcode.team10515.states.ForkliftStateMachine2;

public class ForkliftSubsystem2 implements ISubsystem<org.firstinspires.ftc.teamcode.team10515.states.ForkliftStateMachine2, org.firstinspires.ftc.teamcode.team10515.states.ForkliftStateMachine2.State> {
    private static org.firstinspires.ftc.teamcode.team10515.states.ForkliftStateMachine2 ForkliftStateMachine2;
    private RevMotor forkliftMotor;
    private boolean presetMode = true;

    public ForkliftSubsystem2(RevMotor forkliftMotor){
        setForkliftStateMachine2(new ForkliftStateMachine2());
        setForkliftMotor(forkliftMotor);
    }

    @Override
    public org.firstinspires.ftc.teamcode.team10515.states.ForkliftStateMachine2 getStateMachine() {
        return ForkliftStateMachine2;
    }

    @Override
    public org.firstinspires.ftc.teamcode.team10515.states.ForkliftStateMachine2.State getState() {
        return getStateMachine().getState();
    }

    @Override
    public void start() {    }

    @Override
    public void stop() {    }

    @Override
    public String getName() {
        return "Forklift Subsystem 2";
    }

    @Override
    public void writeToTelemetry(Telemetry telemetry) {    }

    @Override
    public void update(double dt) {
        getStateMachine().update(dt);
        if (presetMode)
            getForkliftMotor().setPower(getState().getPower(getCurrentAngle()));
    }

    public double getCurrentAngle() {
        double encoderTicks = getForkliftMotor().getCurrentEncoderTicks() / getForkliftMotor().getEncoderTicksPerRevolution();
        return encoderTicks * 180;
    }

    public RevMotor getForkliftMotor(){
        return forkliftMotor;
    }

    public static void setForkliftStateMachine2(org.firstinspires.ftc.teamcode.team10515.states.ForkliftStateMachine2 ForkliftStateMachine2){
        ForkliftSubsystem2.ForkliftStateMachine2 = ForkliftStateMachine2;
    }

    public void setForkliftMotor(RevMotor motor){
        this.forkliftMotor = motor;
    }

    public void setPresetMode(boolean preset){
        this.presetMode = preset;
    }
}
