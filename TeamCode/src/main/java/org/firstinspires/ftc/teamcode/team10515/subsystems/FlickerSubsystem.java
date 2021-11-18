package org.firstinspires.ftc.teamcode.team10515.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.drivers.RevServo;
import org.firstinspires.ftc.teamcode.team10515.states.FlickerStateMachine;

public class FlickerSubsystem implements ISubsystem<FlickerStateMachine, FlickerStateMachine.State> {
    private static FlickerStateMachine flickerStateMachine;
    private RevServo leftFlickerServo;
    private RevServo rightFlickerServo;


    public FlickerSubsystem(RevServo leftServo, RevServo rightServo){
        setFlickerStateMachine(new FlickerStateMachine());
        setLeftFlickerServo(leftServo);
        setRightFlickerServo(rightServo);
    }

    @Override
    public FlickerStateMachine getStateMachine() {
        return flickerStateMachine;
    }

    @Override
    public FlickerStateMachine.State getState() {
        return getStateMachine().getState();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getName() {
        return "Flicker Subsystem";
    }

    @Override
    public void writeToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update(double dt) {
        getStateMachine().update(dt);
        getRightFlickerServo().setPosition(getState().getRightPosition());
        getLeftFlickerServo().setPosition(getState().getLeftPosition());
    }

    public static void setFlickerStateMachine(FlickerStateMachine flickerStateMachine) {
        FlickerSubsystem.flickerStateMachine = flickerStateMachine;
    }

    public RevServo getLeftFlickerServo() {
        return leftFlickerServo;
    }
    public RevServo getRightFlickerServo() {
        return rightFlickerServo;
    }


    public void setLeftFlickerServo(RevServo leftFlickerServo) {
        this.leftFlickerServo = leftFlickerServo;
    }
    public void setRightFlickerServo(RevServo rightFlickerServo) {
        this.rightFlickerServo = rightFlickerServo;
    }

}
