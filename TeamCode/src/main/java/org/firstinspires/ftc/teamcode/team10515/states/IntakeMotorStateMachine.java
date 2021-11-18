package org.firstinspires.ftc.teamcode.team10515.states;

import org.firstinspires.ftc.teamcode.lib.util.Namable;

public class IntakeMotorStateMachine extends SimpleState<IntakeMotorStateMachine.State> {
    public IntakeMotorStateMachine() {

        super(State.IDLE);
    }

    @Override
    public String getName() {
        return "Intake Motor State Machine";
    }

    public enum State implements Namable {
        IDLE("Idle", 0d),
        //INTAKE("Intake", 0.9d),
        //OUTTAKE("Outtake", -0.7d);
        //use mm/sec
        INTAKE("Intake", 5750),
        INTAKE1("Intake", 6000),
        INTAKE2("Intake", 6250),
        INTAKE3("Intake", 5500),
        INTAKE4("Intake", 5200),
        OUTTAKE("Outtake", -5500);

        private final String name;
        private final double speed;

        State(final String name, final double speed) {
            this.name  = name;
            this.speed = speed;
        }

        @Override
        public String getName() {
            return name;
        }

        public double getSpeed() {
            return speed;
        }
    }
}
