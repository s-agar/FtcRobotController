package org.firstinspires.ftc.teamcode.team10515.states;

import org.firstinspires.ftc.teamcode.lib.util.Namable;
import org.firstinspires.ftc.teamcode.lib.util.Time;
import org.firstinspires.ftc.teamcode.lib.util.TimeUnits;

public class ForkliftStateMachine2 extends TimedState<ForkliftStateMachine2.State> {
    public ForkliftStateMachine2() {
        super(State.INIT);
    }

    @Override
    public String getName() {
        return "Forklift State Machine 2";
    }

    @Override
    protected Time getStateTransitionDuration() {
        return new Time(1d, TimeUnits.SECONDS);
    }

    public enum State implements Namable {
        INIT(0d), ALIGN(60d), TOP(155d);

        private final double angle;
        private double minSpeed = 0.2, maxSpeed = 0.6, factorOfMS = 0.75;
        double power = 0d, distance=0d;

        State(final double angle) {
            this.angle = angle;
        }

        public double getAngle() {
            return this.angle;
        }

        public double getPower(double currentAngle){
            double angleDiff = getAngle() - currentAngle;
            double absAngleDiff = Math.abs(angleDiff);

            if (power == 0d)
                distance = Math.abs(getAngle() - currentAngle);

            //return +- if within 1.0 degrees
            if(absAngleDiff <= 1.0) {
                power = 0d;
            }
            else {
                if (absAngleDiff >= factorOfMS * distance) {
                    power = maxSpeed;
                } else {
                    power = ((absAngleDiff / (factorOfMS * distance)) * (maxSpeed - minSpeed)) + minSpeed;
                }

                power *= Math.signum(angleDiff);
            }
            return power;
        }

        @Override
        public String getName() {
            return null;
        }
    }

}
