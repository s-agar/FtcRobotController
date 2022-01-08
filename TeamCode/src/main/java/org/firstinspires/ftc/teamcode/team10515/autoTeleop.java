package org.firstinspires.ftc.teamcode.team10515;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.lib.geometry.Rotation2d;
import org.firstinspires.ftc.teamcode.team10515.auto.UGBase;
import org.firstinspires.ftc.teamcode.team10515.control.EnhancedGamepad;
import org.firstinspires.ftc.teamcode.team10515.states.FlickerStateMachine;
import org.firstinspires.ftc.teamcode.team10515.states.ForkliftStateMachine2;
import org.firstinspires.ftc.teamcode.team10515.states.GripperStateMachine;
import org.firstinspires.ftc.teamcode.team10515.states.IntakeMotorStateMachine;
import org.firstinspires.ftc.teamcode.team10515.states.PulleyStateMachine;
import org.firstinspires.ftc.teamcode.team10515.states.ShooterStateMachine;

/**
 * This {@code class} acts as the driver-controlled program for FTC team 10515 for the Skystone
 * challenge. By extending {@code SkystoneRobot}, we already have access to all the robot subsystems,
 * so only tele-operated controls need to be defined here.
 *
 * The controls for this robot are:
 *  User 1:
 *      Drive:
 *          Left & Right joysticks -> Mecanum drive
 *          Left-trigger           -> Auto foundation movement
 *          Y-button (pressed)     -> Forklift up
 *          X-button (pressed)     -> Forklift down
 *      //Vision:
 *          //Left bumper (pressed)  -> Auto feed
 *      Right bumper (pressed) -> Toggle end game extension slides to extend
 *  User 2:
 *          Dpad-down              -> Stops Shooter
 *          Dpad-right             -> Speed for Power Shots
 *          Dpad-Up                -> Speed for high shots
 *          Right-trigger          -> Turns the Intake On
 *          Left - Trigger         -> Reverses Intake
 *          Back                   -> Stopping the intake
 *          Y - Button             -> Elevator goes up
 *          A - Button             -> Elevator goes down
 *          X - Button             -> Flicker hit rings and come back
 *          Left bumper (pressed)  -> Toggles automation
 *          Right bumper (pressed) -> Intake Servo Hit
 *
 * @see UltimateGoalRobot
 */
@TeleOp(name = "Auto Tele-Op", group = "Main")
public class autoTeleop extends UGTeleOpRobot {
    private boolean iselevatorUp = false;   //elevator starts in down position
    private boolean isFlicked = false;      //flickers are inside

    //intake Servo activated
    private int count = 0;
    private int intakeChange = 0;
    private int shooterChange = 0;
    public int bumper = 0;
    public int nextTurn = 0;
    private double currSpeed = 0;
    private String name = "Idle";

    boolean hitLeftPowerShot, hitRightPowerShot, hitMidPowerShot = false;
    enum FlickState {
        FLICK,
        WAITFLICK,
        BACK,
        WAITBACK,
        IDLE,
        TURN,
        TURN2,
        WAITTURN,
        FLICK2,
        BACK2,
        BACKNTURN
    }
    int NumFlicks = 0, NumFlicksThree = 0;
    FlickState FlickThree = FlickState.IDLE;
    FlickState FlickPowershots = FlickState.IDLE;

    enum Mode {
        DRIVER_CONTROL,
        AUTOMATIC_CONTROL
    }
    public ElapsedTime resetFlicker = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    public ElapsedTime resetFlickThree = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    public ElapsedTime resetFlickPS = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    public ElapsedTime resetWobble = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    public ElapsedTime elapsedTimeFlick = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);


    public enum ArmState {
        IDLE,
        PRESS_B,
        PRESS_X
    }

    public ArmState currentState;
    Mode currentMode = Mode.DRIVER_CONTROL;

    // The coordinates we want the bot to automatically go to when we press the A button
    Pose2d highShotPose = new Pose2d(0,0,10);
    double highShotHeading = Math.toRadians(10);

    @Override
    public void init() {
        drive = new UGBase(hardwareMap, true);
        drive.setPoseEstimate(PoseStorage.currentPose);
        currentState = ArmState.IDLE;

        super.init();
    }

    @Override
    public void start() {
        //Feeder.setManualControlExtension(() -> gamepad2.b ? 0.5d : gamepad2.x ? -0.5d : 0d);
    }

    @Override
    public void loop() {
        super.loop();
        getEnhancedGamepad1().update();
        getEnhancedGamepad2().update();


        Pose2d poseEstimate = drive.getPoseEstimate();
        switch (currentMode) {
            case DRIVER_CONTROL:
                drive.setWeightedDrivePower(
                        new Pose2d(
                                -gamepad1.left_stick_y,
                                -gamepad1.left_stick_x,
                                -gamepad1.right_stick_x
                        )
                );

                if (getEnhancedGamepad1().isDpadUpJustPressed()) {
                    // If the A button is pressed on gamepad1, we generate a splineTo()
                    // trajectory on the fly and follow it
                    // We switch the state to AUTOMATIC_CONTROL
                    Trajectory highShotPosition = drive.trajectoryBuilder(poseEstimate)
                            .splineTo(new Vector2d(highShotPose.getX(), highShotPose.getY()), highShotHeading)
                            .build();

                    drive.followTrajectoryAsync(highShotPosition);
                    currentMode = Mode.AUTOMATIC_CONTROL;
                }
                break;
            case AUTOMATIC_CONTROL:
                // If x is pressed, we break out of the automatic following
                if (gamepad1.x) {
                    drive.cancelFollowing();
                    currentMode = Mode.DRIVER_CONTROL;
                }

                // If drive finishes its task, cede control to the driver
                if (!drive.isBusy()) {
                    currentMode = Mode.DRIVER_CONTROL;
                }
                break;
        }
        if (gamepad1.right_bumper) {
            drive.turn(Math.toRadians(-12));
        }
        if (gamepad1.left_bumper) {
            drive.turn(Math.toRadians(12));
        }
        //Gamepad2 Update flywheel intake - In:Right Trigger, Out:Left Trigger, Stop: Back
        if (getEnhancedGamepad2().getRight_trigger() > 0) {
            drive.robot.getIntakeMotorSubsystem().getStateMachine().updateState(IntakeMotorStateMachine.State.INTAKE);
        } else if (getEnhancedGamepad2().getLeft_trigger() > 0) {
            drive.robot.getIntakeMotorSubsystem().getStateMachine().updateState(IntakeMotorStateMachine.State.OUTTAKE);
        } else if (getEnhancedGamepad2().isBack()) {
            drive.robot.getIntakeMotorSubsystem().getStateMachine().updateState(IntakeMotorStateMachine.State.IDLE);
        }

        if (!iselevatorUp && getEnhancedGamepad1().isaJustPressed()) {
            intakeChange++;
            if (intakeChange > 4) intakeChange = 0;
            switch (intakeChange) {
                case 1:
                    drive.robot.getIntakeMotorSubsystem().getStateMachine().updateState(IntakeMotorStateMachine.State.INTAKE1);
                    break;
                case 2:
                    drive.robot.getIntakeMotorSubsystem().getStateMachine().updateState(IntakeMotorStateMachine.State.INTAKE2);
                    break;
                case 3:
                    drive.robot.getIntakeMotorSubsystem().getStateMachine().updateState(IntakeMotorStateMachine.State.INTAKE3);
                    break;
                case 4:
                    drive.robot.getIntakeMotorSubsystem().getStateMachine().updateState(IntakeMotorStateMachine.State.INTAKE4);
                    break;
                case 0:
                    drive.robot.getIntakeMotorSubsystem().getStateMachine().updateState(IntakeMotorStateMachine.State.INTAKE);
                    break;
            }
        }

        //Gamepad2 Manually move Elevator up && btnPressedA.milliseconds()>1250  && btnPressedA.milliseconds()>1250
        if (getEnhancedGamepad2().isyLast()) {
            drive.robot.getPulleySubsystem().getStateMachine().updateState(PulleyStateMachine.State.UP);
            drive.robot.getShooterSubsystem().getStateMachine().updateState(ShooterStateMachine.State.HIGHGOAL);
            name = "High";
            currSpeed = drive.robot.getShooterSubsystem().getStateMachine().getState().getCurrSpeed(name);
            drive.robot.getIntakeMotorSubsystem().getStateMachine().updateState(IntakeMotorStateMachine.State.IDLE);
            iselevatorUp = true;    //Elevator Moved Up and shooter starts
        } else if (getEnhancedGamepad2().isaLast()) {
            drive.robot.getPulleySubsystem().getStateMachine().updateState(PulleyStateMachine.State.DOWN);
            drive.robot.getShooterSubsystem().getStateMachine().updateState(ShooterStateMachine.State.IDLE);
            drive.robot.getIntakeMotorSubsystem().getStateMachine().updateState(IntakeMotorStateMachine.State.INTAKE);
            name = "Idle";
            currSpeed = drive.robot.getShooterSubsystem().getStateMachine().getState().getCurrSpeed(name);
            iselevatorUp = false;   //Elevator Moved Down
        }

        //adjust shooter speed
        if (iselevatorUp && (getEnhancedGamepad1().isxJustPressed() || getEnhancedGamepad1().isbJustPressed())) {
            if (getEnhancedGamepad1().isbJustPressed()) {
                shooterChange++;
                if (shooterChange > 4) {
                    shooterChange = 4;
                }
            } else if (getEnhancedGamepad1().isxJustPressed()) {
                shooterChange--;
                if (shooterChange < -4) {
                    shooterChange = -4;
                }
            }

            drive.robot.getShooterSubsystem().getStateMachine().getState().setSpeed(currSpeed + (600 * shooterChange));
        }

        //Toggle Shooter
        if (getEnhancedGamepad2().isDpadUpJustPressed()) {
            shooterChange = 0;
            drive.robot.getShooterSubsystem().getStateMachine().updateState(ShooterStateMachine.State.HIGHGOAL);
            name = "High";
            currSpeed = drive.robot.getShooterSubsystem().getStateMachine().getState().getCurrSpeed(name);
        } else if (getEnhancedGamepad2().isDpadRightJustPressed()) {
            shooterChange = 0;
            drive.robot.getShooterSubsystem().getStateMachine().updateState(ShooterStateMachine.State.POLESHOT);
            name = "Pole";
            currSpeed = drive.robot.getShooterSubsystem().getStateMachine().getState().getCurrSpeed(name);
        } else if (getEnhancedGamepad2().isDpadLeftJustPressed()) {
            shooterChange = 0;
            drive.robot.getShooterSubsystem().getStateMachine().updateState(ShooterStateMachine.State.MIDGOAL);
            name = "Middle";
            currSpeed = drive.robot.getShooterSubsystem().getStateMachine().getState().getCurrSpeed(name);
        } else if (getEnhancedGamepad2().isDpadDownJustPressed()) {
            shooterChange = 0;
            drive.robot.getShooterSubsystem().getStateMachine().updateState(ShooterStateMachine.State.IDLE);
            name = "Idle";
            currSpeed = drive.robot.getShooterSubsystem().getStateMachine().getState().getCurrSpeed(name);
        }

        if (getEnhancedGamepad2().isRightBumperLast()) {
            drive.robot.getFlickerSubsystem().getStateMachine().updateState(FlickerStateMachine.State.HIT);
            resetFlicker.reset();
            isFlicked = true;
            //capture the pose
            highShotPose = drive.getPoseEstimate();
            highShotHeading = drive.getPoseEstimate().getHeading();
        }
        if (isFlicked && resetFlicker.milliseconds() > 100) {
            drive.robot.getFlickerSubsystem().getStateMachine().updateState(FlickerStateMachine.State.INIT);
            isFlicked = false;
        }

        if (getEnhancedGamepad2().isLeftBumperLast() && elapsedTimeFlick.milliseconds() > 200) {
            FlickThree = FlickState.FLICK;
            elapsedTimeFlick.reset();
        }

        //automation flicking and turning for powershots
        if (gamepad1.dpad_down) {
            hitMidPowerShot = true;
            FlickPowershots = FlickState.TURN;
        }
        if (gamepad1.dpad_left) {
            hitLeftPowerShot = true;
            FlickPowershots = FlickState.TURN;
        }
        if (gamepad1.dpad_right) {
            hitRightPowerShot = true;
            FlickPowershots = FlickState.TURN;
        }

        if (gamepad2.b && resetWobble.milliseconds() > 300) {
            currentState = ArmState.PRESS_B;
            resetWobble.reset();
        } else if (gamepad2.x && resetWobble.milliseconds() > 300) {
            currentState = ArmState.PRESS_X;
            resetWobble.reset();
        }

        if (getEnhancedGamepad2().getLeft_stick_y() > 0.5d) {
            drive.robot.getGripperSubsystem().getStateMachine().updateState(GripperStateMachine.State.GRIP);
        }

        if (getEnhancedGamepad2().getLeft_stick_y() < -0.5) {
            drive.robot.getGripperSubsystem().getStateMachine().updateState(GripperStateMachine.State.INIT);
        }
        //WobbleGoal processing
        WobbleGoalV3();

        //High Goal or Mid Goal Shots
        FlickThree();

        //Powershots in end game
        FlickPowerShots();

//        if (currSpeed != 0d) {
//            double newSpeed = currSpeed + compensateVoltage();
//            drive.robot.getShooterSubsystem().getStateMachine().getState().setSpeed(newSpeed);
//            telemetry.addLine("Shooter Speed: " + newSpeed);
//        }

        telemetry.addLine("Shooter speed: " + currSpeed);
        telemetry.addLine("Shooter change: " + shooterChange);
        //telemetry.addLine("Pose"+poseEstimate.getX()+", "+poseEstimate.getY()+", "+poseEstimate.getHeading());
        telemetry.addLine("Intake Output: " + drive.robot.getIntakeMotorSubsystem().getOutput());
        telemetry.addLine("Shooter Output: " + drive.robot.getShooterSubsystem().getOutput());
        telemetry.addLine("bumper" + bumper);
        telemetry.update();
    }

    void FlickThree()
    {
        switch (FlickThree) {
            case IDLE:
                NumFlicksThree = 0;
                break;
            case FLICK:
                drive.robot.getFlickerSubsystem().getStateMachine().updateState(FlickerStateMachine.State.HIT);
                FlickThree = FlickState.WAITFLICK;
                resetFlickThree.reset();
                break;
            case WAITFLICK:
                if (resetFlickThree.milliseconds() > 100 + (NumFlicksThree * 20)) {
                    FlickThree = FlickState.BACK;
                }
                break;
            case BACK:
                drive.robot.getFlickerSubsystem().getStateMachine().updateState(FlickerStateMachine.State.INIT);
                FlickThree = FlickState.WAITBACK;
                resetFlickThree.reset();
                break;
            case WAITBACK:
                if (resetFlickThree.milliseconds() > 300 + (NumFlicksThree * 30)) {
                    if (NumFlicksThree < 2) {
                        FlickThree = FlickState.FLICK;
                    }
                    else {
                        FlickThree = FlickState.IDLE;
                    }
                    NumFlicksThree++;
                }
                break;
        }
    }

    void FlickPowerShots()
    {
        switch (FlickPowershots) {
            case IDLE:
                NumFlicks = 0;
                hitRightPowerShot = false;
                hitLeftPowerShot = false;
                hitMidPowerShot = false;
                break;
            case TURN:
                if(hitLeftPowerShot){
                    drive.turn(Math.toRadians(-8));
                    nextTurn = -8;
                }
                else if(hitMidPowerShot){
                    drive.turn(Math.toRadians(8));
                    nextTurn = -16;
                }
                else if(hitRightPowerShot){
                    drive.turn(Math.toRadians(8));
                    nextTurn = 8;
                }
                FlickPowershots = FlickState.WAITTURN;
                break;
            case WAITTURN:
                if (!drive.isBusy()) {
                    drive.robot.getFlickerSubsystem().getStateMachine().updateState(FlickerStateMachine.State.HIT);
                    resetFlickPS.reset();
                    FlickPowershots = FlickState.BACKNTURN;
                }
                break;
            case BACKNTURN:
                if (resetFlickPS.milliseconds() > 100) {
                    drive.robot.getFlickerSubsystem().getStateMachine().updateState(FlickerStateMachine.State.INIT);
                    drive.turn(Math.toRadians(nextTurn));
                    resetFlickPS.reset();
                    FlickPowershots = FlickState.FLICK2;
                }
                break;
            case FLICK2:
            if(!drive.isBusy() && resetFlickPS.milliseconds() > 300) {
                drive.robot.getFlickerSubsystem().getStateMachine().updateState(FlickerStateMachine.State.HIT);
                NumFlicks++;
                resetFlickPS.reset();
                FlickPowershots = FlickState.BACK2;
            }
            break;
            case BACK2:
                if (resetFlickPS.milliseconds() > 100) {
                    drive.robot.getFlickerSubsystem().getStateMachine().updateState(FlickerStateMachine.State.INIT);
                    resetFlickPS.reset();
                    FlickPowershots = FlickState.IDLE;
                }
                break;
        }
    }

    public void WobbleGoalV3(){
        if(getEnhancedGamepad2().getRight_stick_y() > 0.5d){
            drive.robot.getForkliftSubsystem2().setPresetMode(false);
            drive.robot.getForkliftSubsystem2().getForkliftMotor().setPower(getEnhancedGamepad2().getRight_stick_y()* -0.6);
        }
        else if(getEnhancedGamepad2().getRight_stick_y() < -0.5d) {
            drive.robot.getForkliftSubsystem2().setPresetMode(false);
            drive.robot.getForkliftSubsystem2().getForkliftMotor().setPower(-getEnhancedGamepad2().getRight_stick_y() * 0.6);
        }
        else if (Math.abs(getEnhancedGamepad2().getRight_stick_y()) < 0.5d && Math.abs(getEnhancedGamepad2().getRight_stick_y()) > 0.1d)
        {
            drive.robot.getForkliftSubsystem2().setPresetMode(false);
            drive.robot.getForkliftSubsystem2().getForkliftMotor().setPower(0);
        }
        else if (getEnhancedGamepad2().getRight_stick_x() > 0.5d){
            drive.robot.getForkliftSubsystem2().setPresetMode(false);
            drive.robot.getForkliftSubsystem2().getForkliftMotor().setPower(0);
        }

        switch (currentState) {
            case IDLE:
                //Do nothing
                break;

            case PRESS_B:
                drive.robot.getForkliftSubsystem2().setPresetMode(true);
                currentState = ArmState.IDLE;
                switch (drive.robot.getForkliftSubsystem2().getState()) {
                    case INIT:
                        drive.robot.getForkliftSubsystem2().getStateMachine().updateState(ForkliftStateMachine2.State.ALIGN);
                        break;
                    case ALIGN:
                        drive.robot.getForkliftSubsystem2().getStateMachine().updateState(ForkliftStateMachine2.State.TOP);
                        break;
                    case TOP:
                        //nothing it's already at the top
                        break;
                }
                break;

            case PRESS_X:
                drive.robot.getForkliftSubsystem2().setPresetMode(true);
                currentState = ArmState.IDLE;
                switch(drive.robot.getForkliftSubsystem2().getState()) {
                    case TOP:
                        drive.robot.getForkliftSubsystem2().getStateMachine().updateState(ForkliftStateMachine2.State.ALIGN);
                        break;
                    case ALIGN:
                        drive.robot.getForkliftSubsystem2().getStateMachine().updateState(ForkliftStateMachine2.State.INIT);
                        break;
                    case INIT:
                        //nothing it's already at the bottom
                        break;
                }
                break;
        }
    }

    double compensateVoltage(){
        double retVal = 0d;
        double voltage = getBatteryVoltage();
        //adjust lower if battery high
        if (voltage > 13d) {
            if ((voltage - 13d) > 0.5d)
                voltage = 0.5d;
            else
                voltage = voltage - 13d;

            retVal = -(600 * voltage);
        }
        //adjust higher if battery low
        else if (voltage < 12.5d) {
            if ((12.5d - voltage) > 0.5d)
                voltage = 0.5d;
            else
                voltage = 12.5 - voltage;

                retVal = (600 * voltage);
        }
        return retVal;
    }
    // Computes the current battery voltage
    double getBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }

}
