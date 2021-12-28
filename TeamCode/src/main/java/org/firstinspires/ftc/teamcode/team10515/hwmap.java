package org.firstinspires.ftc.teamcode.team10515;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class hwmap {

    //Drive motors
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;

    //Lift motors
    public DcMotor liftL = null;
    public DcMotor liftR = null;

    //Intake/outtake servos
    public Servo outerServo = null;
    public Servo innerServo = null;

    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    public hwmap(){

    }

    public void init(HardwareMap ahwMap){
        hwMap = ahwMap;

        leftFrontDrive = hwMap.get(DcMotor.class, "FL_Drive");
        rightFrontDrive = hwMap.get(DcMotor.class, "FR_Drive");
        leftBackDrive = hwMap.get(DcMotor.class, "BL_Drive");
        rightBackDrive = hwMap.get(DcMotor.class, "BR_Drive");

        liftL = hwMap.get(DcMotor.class, "Left_Lift");
        liftR = hwMap.get(DcMotor.class, "Right_Lift");

        outerServo = hwMap.get(Servo.class, "Outer_Servo");
        innerServo = hwMap.get(Servo.class, "Inner_Servo");

        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        liftL.setDirection(DcMotor.Direction.FORWARD);
        liftR.setDirection(DcMotor.Direction.FORWARD);

        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);

        liftL.setPower(0);
        liftR.setPower(0);

        outerServo.setPosition(0);
        innerServo.setPosition(0);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
