package org.firstinspires.ftc.teamcode.team10515.states;

import org.firstinspires.ftc.teamcode.lib.util.Namable;
import org.firstinspires.ftc.teamcode.lib.util.Time;
import org.firstinspires.ftc.teamcode.lib.util.TimeUnits;

public class FlickerStateMachine extends TimedState<FlickerStateMachine.State> {
    public FlickerStateMachine() {
        super(State.INIT);
    }

    @Override
    public String getName() {
        return "Flicker State Machine";
    }

    @Override
    protected Time getStateTransitionDuration() {
        return new Time(10d, TimeUnits.MILLISECONDS);
    }

    public enum State implements Namable {
        INIT("Init", 1.0d, 0.0d), HIT("Hit", 0.7d, 0.3d);

        private final String name;
        private final double leftPosition;
        private final double rightPosition;

        State(final String name, final double leftPosition, final double rightPosition) {
            this.name          = name;
            this.leftPosition  = leftPosition;
            this.rightPosition = rightPosition;
        }

        public double getLeftPosition() {
            return leftPosition;
        }

        public double getRightPosition() {
            return rightPosition;
        }

        @Override

        public String getName() {
            return name;
        }
    }
}
