package org.firstinspires.ftc.teamcode.drive;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.Encoder;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class StandardTrackingWheelLocalizer extends ThreeTrackingWheelLocalizer {
    public static double TICKS_PER_REV = 8192;
    public static double WHEEL_RADIUS = 0.6889764; // in
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double LATERAL_DISTANCE = 14.3564; //14.27533333333333; // in; distance between the left and right wheels 14.025 empirical //14.375 original value
    public static double FORWARD_OFFSET = -1.875; // in; offset of the lateral wheel

    public static double X_MULTIPLIER = 1.0130233;//1.024938934547012; // Multiplier in the X direction 1.05537889, 1.0245418761, 1.0463534582 //original value from test 1: 1.04439816741
    public static double Y_MULTIPLIER = 1.0310549;//1.023261168273632; // Multiplier in the Y direction  1.016196, 1.02602629, 1.02549903 //original value from test 1: 1.02257377
    /*
    X
    Value       Measured        Telemetry
    1:          100.1           97.834              1.023161682032831
    2:          100.0           97.333              1.02740077876979
    3:          100.0           97.632              1.024254342838414
                                                    1.024938934547012

    Y
    Value       Measured        Telemetry
    1:          100.1           98.113              1.020252158225719
    2:          100.1           98.146              1.019909114991951
    3:          100.0           97.123              1.029622231603225
                                                    1.023261168273632

    6.75 + 10.5 = 17.25 Length of Robot     Midpoint: 8.625 Forward Offset = 8.625 - 6.75 = 1.875 and negative
    LATERAL_DISTANCE TUNING:
    First Value: 13.920
    Second Value: 13.931
    Third Value: 13.925
    AVG: 13.92533333333333 + 0.35 to adjust for when we tested with this value
    */


    private Encoder leftEncoder, rightEncoder, frontEncoder;

    public StandardTrackingWheelLocalizer(HardwareMap hardwareMap) {
        super(Arrays.asList(
                new Pose2d(0, LATERAL_DISTANCE / 2 + 0.125, 0), // left
                new Pose2d(0, -LATERAL_DISTANCE / 2 + 0.125, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));
        //encoder names should be same as motor names, to which they are connected
        //leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "FL"));
        //rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "RR"));
        //frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "RL"));
        DcMotorEx leftOdo, rightOdo, frontOdo;
        leftOdo = hardwareMap.get(DcMotorEx.class,"FL");
        leftOdo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftOdo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightOdo = hardwareMap.get(DcMotorEx.class,"RR");
        rightOdo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightOdo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontOdo = hardwareMap.get(DcMotorEx.class,"RL");
        frontOdo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontOdo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftEncoder = new Encoder(leftOdo);
        rightEncoder = new Encoder(rightOdo);
        frontEncoder = new Encoder(frontOdo);

        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
        //leftEncoder.setDirection(Encoder.Direction.REVERSE);
        rightEncoder.setDirection(Encoder.Direction.REVERSE);
        //frontEncoder.setDirection(Encoder.Direction.REVERSE);
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {

        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCurrentPosition()) * Y_MULTIPLIER
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        // TODO: If your encoder velocity can exceed 32767 counts / second (such as the REV Through Bore and other
        //  competing magnetic encoders), change Encoder.getRawVelocity() to Encoder.getCorrectedVelocity() to enable a
        //  compensation method

        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCorrectedVelocity()) * X_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCorrectedVelocity()) * X_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCorrectedVelocity()) * Y_MULTIPLIER
        );
    }
}
