package org.firstinspires.ftc.teamcode.team10515.states;

import org.firstinspires.ftc.teamcode.lib.util.Namable;
import org.firstinspires.ftc.teamcode.lib.util.Time;
import org.firstinspires.ftc.teamcode.lib.util.TimeUnits;

public class PulleyStateMachine extends TimedState<PulleyStateMachine.State> {
    public PulleyStateMachine(){
        super(State.DOWN);
    }

    @Override
    protected Time getStateTransitionDuration() {
        return new Time(1d, TimeUnits.SECONDS);
    }

    @Override
    public String getName() {
        return "Pulley State Machine";
    }

    public enum State implements Namable{
        DOWN("Down", 0d), UP("Up", 0.75d);

        private final String name;
        private final double position;

        State(final String name, final double position){
            this.name = name;
            this.position = position;

        }

        public double getPosition() {
            return position;
        }


        @Override
        public String getName(){
            return name;
        }
    }
}