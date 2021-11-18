package org.firstinspires.ftc.teamcode.team10515.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;


public abstract class TeamBase extends LinearOpMode {


    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = false;
    private static final String VUFORIA_KEY =
            "AVVrDYb/////AAABmT0TlZXDYE3gpf/zMjQrOgACsYT0LcTPCkhjAmq0XO3HT0RdGx2eP+Lwumhftz4e/g28CBGg1HmaFfy5kW9ioO4UGDeokDyxRfqWjNQwKG3BanmjCXxMxACaJ7iom5J3o4ylWNmuiyxsK8n1fFf2dVsTUsvUI7aRxqTahnIqqRJRsGmxld18eHy/ZhHfIjOyifi4svZUQiput21/jAloTx0sTnnrpR1Y/xGOz+68sGuXIgLZHpAQSoZnXiczGKdahGXOg3n6dXlQPIiASE1kHp253CTwO40l1HHN083m4wYjP4FCl/9TH3tb0Wj/Ccmlhfz2omhnZQKOBe7RsIxRk+PuEGkIe5hCs/lV9+yf9iBm";
    private static final float mmPerInch = 25.4f;
    private static final float mmTargetHeight = (6) * mmPerInch;          // the height of the center of the target image above the floor
    private VuforiaLocalizer vuforia = null;
    /**
     * This is the webcam we are to use. As with other hardware devices such as motors and
     * servos, this device is identified using the robot configuration tool in the FTC application.
     */
    private boolean targetVisible = false;
    private float phoneXRotate = 0;
    private float phoneYRotate = 0;
    private float phoneZRotate = 0;

    // Class Members
    private TFObjectDetector tfod;

    /**
     * This is the webcam we are to use. As with other hardware devices such as motors and
     * servos, this device is identified using the robot configuration tool in the FTC application.
     */

    /* Declare OpMode members. */
    UGMap robot = new UGMap(hardwareMap);
    ElapsedTime runtime = new ElapsedTime();
    Orientation angles;
    public Orientation orientation;
    //Create the angle tracker
    public double angle = 0;
    private double current_heading;


    //new calculation for Skystone Strafer Chassis
    private double wheel_diameter = 3.93700787; //inches
    public double ticks_per_inch = (383.6 * 2) / (wheel_diameter * Math.PI);      //wheel_encoder_ticks / (wheel_diameter * Math.PI);

    //private double wheel_encoder_ticks = ticks_per_inch * wheel_diameter * Math.PI;   //original 537.6

    double gs_previous_speed;
    double gs_previous_ticks_traveled;
    ElapsedTime gs_speed_timer = new ElapsedTime();
    boolean gs_first_run = true;
    int hard_stop = 5;  //seconds per operation i.e. move for this much or less

    public TeamBase() {
    }



    public void resetEncoders() {
        robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sleep(100);

        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void stopRobot() {
        resetEncoders();
        robot.leftRear.setPower(0.0);
        robot.rightRear.setPower(0.0);
        robot.leftFront.setPower(0.0);
        robot.rightFront.setPower(0.0);
    }

    public void shooterStartPoleShot() {
        robot.shooter1.setPower(0.5);
        robot.shooter2.setPower(0.5);

    }
    public UGCV.numRings getRingsUsingImage(boolean red) {
        //initVuforia();

        UGCV ugvuforia = new UGCV(vuforia);
        UGCV.numRings position = UGCV.numRings.ZERO;

        position = ugvuforia.GetPosition(true,red);
        return position;
    }

    public void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }
}
