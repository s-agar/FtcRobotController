package org.firstinspires.ftc.teamcode.team10515;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.lib.drivers.Motor;
import org.firstinspires.ftc.teamcode.lib.drivers.RevMotor;
import org.firstinspires.ftc.teamcode.lib.drivers.RevServo;
import org.firstinspires.ftc.teamcode.lib.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.lib.util.TimeProfiler;
import org.firstinspires.ftc.teamcode.team10515.subsystems.Drive;
import org.firstinspires.ftc.teamcode.team10515.subsystems.ExpansionHubs;
import org.firstinspires.ftc.teamcode.team10515.subsystems.FlickerSubsystem;
import org.firstinspires.ftc.teamcode.team10515.subsystems.ForkliftSubsystem2;
import org.firstinspires.ftc.teamcode.team10515.subsystems.GripperSubsystem;
import org.firstinspires.ftc.teamcode.team10515.subsystems.IntakeMotorSubsystem;
import org.firstinspires.ftc.teamcode.team10515.subsystems.PulleySubsystem;
import org.firstinspires.ftc.teamcode.team10515.subsystems.RobotStateEstimator;
import org.firstinspires.ftc.teamcode.team10515.subsystems.ShooterSubsystem;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.ExpansionHubServo;

/**
 * Motor naming convention:
 *     Drivetrain
 *         Back Right Wheel  -> RR
 *         Back Left Wheel   -> RL
 *     Intake
 *         Left Flywheel  -> INL
 *         Right Flywheel -> INR
 *     Outtake
 *         Left Extension  -> LL
 *         Right Extension -> LR
 * Servo naming convention:
 *     Outtake
 *
 *     End game
 *         Extension Blocker -> EXT
 * Misc. sensors naming convention:

 */
public class UGAutoRobot  {
    private TimeProfiler matchRuntime;
    //protected static Rev2mDistanceSensor elevatorSensor;
    private ExpansionHubs expansionHubs;
    private RobotStateEstimator robotStateEstimator;
    private Drive drive;
    private PulleySubsystem elevatorSubsystem;
    private FlickerSubsystem flickerSubsystem;
    private GripperSubsystem gripperSubsystem;
    private ShooterSubsystem shooterMotors;
    private ForkliftSubsystem2 forkliftSubsystem2;
    private IntakeMotorSubsystem intakeMotorSubsystem;
    private RevMotor[]   motors;
    private RevServo[]   servos;

    public void init(HardwareMap hardwareMap) {

//        setExpansionHubs(new ExpansionHubs(this, hardwareMap.get(ExpansionHubEx.class, "Control Hub"),
//                hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 1"))
//        );

        setMotors(new RevMotor[] {
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("Shooter 1")), false, false, false, true, Motor.GOBILDA_6000_RPM.getENCODER_TICKS_PER_REVOLUTION(), 120, 1d),
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("Shooter 2")), false, false, false, true),
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("Intake Motor")), true, false, false, false, Motor.GOBILDA_1150_RPM.getENCODER_TICKS_PER_REVOLUTION(), 50.8, 2d),
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("Forklift Motor")), true, true, true, false, Motor.GOBILDA_60_RPM.getENCODER_TICKS_PER_REVOLUTION()),
        });

        setServos(new RevServo[] {
                new RevServo((ExpansionHubServo)(hardwareMap.get("Elevator Servo"))),
                new RevServo((ExpansionHubServo)(hardwareMap.get("Flicker 1"))),
                new RevServo((ExpansionHubServo)(hardwareMap.get("Flicker 2"))),
                new RevServo((ExpansionHubServo)(hardwareMap.get("Gripper"))),
        });

        //setElevatorSensor(hardwareMap.get(Rev2mDistanceSensor.class, "Elevator Sensor"));
        setShooterSubsystem(new ShooterSubsystem(getMotors()[0], getMotors()[1]));
        setPulleySubsystem(new PulleySubsystem(getServos()[0]));
        setIntakeMotorSubsystem(new IntakeMotorSubsystem(getMotors()[2]));
        setForkliftSubsystem2(new ForkliftSubsystem2(getMotors()[3]));
        setFlickerSubsystem(new FlickerSubsystem(getServos()[1], getServos()[2]));
        setGripperSubsystem(new GripperSubsystem(getServos()[3]));
        setMatchRuntime(new TimeProfiler(false));
    }


    public ExpansionHubs getExpansionHubs() {
        return expansionHubs;
    }

    public void setExpansionHubs(ExpansionHubs expansionHubs) {
        this.expansionHubs = expansionHubs;
    }

    public RobotStateEstimator getRobotStateEstimator() {
        return robotStateEstimator;
    }

    public void setRobotStateEstimator(RobotStateEstimator robotStateEstimator) {
        this.robotStateEstimator = robotStateEstimator;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    public IntakeMotorSubsystem getIntakeMotorSubsystem() {
        return intakeMotorSubsystem;
    }

    public void setIntakeMotorSubsystem(IntakeMotorSubsystem intakeMotorSubsystem){
        this.intakeMotorSubsystem = intakeMotorSubsystem;
    }

    public ShooterSubsystem getShooterSubsystem() { return shooterMotors; }

    public void setShooterSubsystem(ShooterSubsystem shooterMotors){ this.shooterMotors = shooterMotors; }

    public PulleySubsystem getPulleySubsystem() {
        return elevatorSubsystem;
    }

    public void setPulleySubsystem(PulleySubsystem elevatorSubsystem) {
        this.elevatorSubsystem = elevatorSubsystem;
    }

    public FlickerSubsystem getFlickerSubsystem(){
        return flickerSubsystem;
    }

    public void setFlickerSubsystem(FlickerSubsystem flickerSubsystem){
        this.flickerSubsystem = flickerSubsystem;
    }

    public GripperSubsystem getGripperSubsystem(){
        return gripperSubsystem;
    }

    public void setGripperSubsystem(GripperSubsystem gripperSubsystem){
        this.gripperSubsystem = gripperSubsystem;
    }

    public ForkliftSubsystem2 getForkliftSubsystem2() {
        return forkliftSubsystem2;
    }

    public void setForkliftSubsystem2(ForkliftSubsystem2 forkliftSubsystem2){
        this.forkliftSubsystem2 = forkliftSubsystem2;
    }
    //public void setElevatorSensor(Rev2mDistanceSensor range) {
    //    this.elevatorSensor = range;
    //}

    public TimeProfiler getMatchRuntime() {
        return matchRuntime;
    }

    public void setMatchRuntime(TimeProfiler matchRuntime) {
        this.matchRuntime = matchRuntime;
    }

    public Pose2d getRobotPose() {
        return getRobotStateEstimator().getPose();
    }

    public double getRobotSpeed() {
        return getRobotStateEstimator().getVelocityPose().getTranslation().norm() +
                Math.abs(getRobotStateEstimator().getVelocityPose().getRotation().getRadians());
    }
    public RevMotor[] getMotors() {
        return motors;
    }

    public void setMotors(RevMotor[] motors) {
        this.motors = motors;
    }

    public RevServo[] getServos() {
        return servos;
    }

    public void setServos(RevServo[] servos) {
        this.servos = servos;
    }
}
