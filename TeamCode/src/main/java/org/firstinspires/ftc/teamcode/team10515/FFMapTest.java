package org.firstinspires.ftc.teamcode.team10515;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.team10515.subsystems.CarouselSubsystem;


public class FFMapTest {
    public DcMotor FR = null;  //FrontRight
    public DcMotor FL = null;  //FrontLeft
    public DcMotor RR = null;  //RearRight
    public DcMotor RL = null;  //RearLeft
    public DcMotor CM = null;  //Carousel

    public BNO055IMU imu = null;

    static private final String FRONTRIGHT     = "FR";
    static private final String FRONTLEFT      = "FL";
    static private final String REARRIGHT      = "RR";
    static private final String REARLEFT       = "RL";
//    static private final String CAROUSELM      = "CM";

    static final String IMU_SENSOR = "imu";

    private CarouselSubsystem carouselMotor;

    HardwareMap hwMap = null;

    public FFMapTest(){

    }

    public void init(HardwareMap ahwMap){
        hwMap = ahwMap;

        FL      = hwMap.dcMotor.get(FRONTLEFT);
        FR      = hwMap.dcMotor.get(FRONTRIGHT);
        RR      = hwMap.dcMotor.get(REARRIGHT);
        RL      = hwMap.dcMotor.get(REARLEFT);
//        CM      = hwMap.dcMotor.get(CAROUSELM);

        FL.setDirection(DcMotor.Direction.FORWARD);
        FR.setDirection(DcMotor.Direction.FORWARD);
        RR.setDirection(DcMotor.Direction.FORWARD);
        RL.setDirection(DcMotor.Direction.FORWARD);
//        CM.setDirection(DcMotor.Direction.FORWARD);

        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        CM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        CM.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        //Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hwMap.get(BNO055IMU.class, IMU_SENSOR);
        imu.initialize(parameters);

    }

    public CarouselSubsystem getCarousel() { return carouselMotor; }

    public void setCarouselMotor(CarouselSubsystem carouselMotor){
        this.carouselMotor = carouselMotor;
    }
}
