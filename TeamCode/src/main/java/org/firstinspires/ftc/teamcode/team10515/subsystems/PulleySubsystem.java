package org.firstinspires.ftc.teamcode.team10515.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.drivers.RevServo;
import org.firstinspires.ftc.teamcode.team10515.states.PulleyStateMachine;

public class PulleySubsystem implements ISubsystem<PulleyStateMachine, PulleyStateMachine.State> {
    public static PulleyStateMachine pulleyStateMachine;
    private RevServo elevatorServo;

    public PulleySubsystem(RevServo elevatorServo){
        setPulleyStateMachine(new PulleyStateMachine());
        setElevatorServo(elevatorServo);
    }

    @Override
    public PulleyStateMachine getStateMachine() {
        return pulleyStateMachine;
    }

    @Override
    public PulleyStateMachine.State getState() {
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
        return "Pulley Subsystem";
    }

    @Override
    public void writeToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update(double dt) {
        getStateMachine().update(dt);
        getElevatorServo().setPosition(getState().getPosition());
    }

    public static void setPulleyStateMachine(PulleyStateMachine pulleyStateMachine){
        PulleySubsystem.pulleyStateMachine = pulleyStateMachine;
    }

    public RevServo getElevatorServo(){
        return elevatorServo;
    }


    public void setElevatorServo(RevServo elevatorServo){
        this.elevatorServo = elevatorServo;
    }

}
