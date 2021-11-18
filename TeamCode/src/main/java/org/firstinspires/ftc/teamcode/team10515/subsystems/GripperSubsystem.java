package org.firstinspires.ftc.teamcode.team10515.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.drivers.RevServo;
import org.firstinspires.ftc.teamcode.team10515.states.GripperStateMachine;

public class GripperSubsystem implements ISubsystem<GripperStateMachine, GripperStateMachine.State> {
    private static GripperStateMachine gripperStateMachine;
    private RevServo gripperServo;


    public GripperSubsystem(RevServo gripperServo){
        setGripperStateMachine(new GripperStateMachine());
        setGripperServo(gripperServo);
    }

    @Override
    public GripperStateMachine getStateMachine() {
        return gripperStateMachine;
    }

    @Override
    public GripperStateMachine.State getState() {
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
        return "Gripper Subsystem";
    }

    @Override
    public void writeToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update(double dt) {
        getStateMachine().update(dt);
        getGripperServo().setPosition(getState().getPosition());
    }

    public static void setGripperStateMachine(GripperStateMachine gripperStateMachine) {
        GripperSubsystem.gripperStateMachine = gripperStateMachine;
    }

    public RevServo getGripperServo() {
        return gripperServo;
    }


    public void setGripperServo(RevServo gripperServo) {
        this.gripperServo = gripperServo;
    }
}
