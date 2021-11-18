package org.firstinspires.ftc.teamcode.team10515;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.teamcode.lib.drivers.Motor;
import org.firstinspires.ftc.teamcode.lib.drivers.RevMotor;
import org.firstinspires.ftc.teamcode.lib.drivers.RevServo;
import org.firstinspires.ftc.teamcode.lib.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.lib.util.TimeProfiler;
import org.firstinspires.ftc.teamcode.team10515.control.StackTracker;
import org.firstinspires.ftc.teamcode.team10515.subsystems.Drive;
import org.firstinspires.ftc.teamcode.team10515.subsystems.ExpansionHubs;
import org.firstinspires.ftc.teamcode.team10515.subsystems.FlickerSubsystem;
import org.firstinspires.ftc.teamcode.team10515.subsystems.ForkliftSubsystem2;
import org.firstinspires.ftc.teamcode.team10515.subsystems.IntakeMotorSubsystem;
import org.firstinspires.ftc.teamcode.team10515.subsystems.PulleySubsystem;
import org.firstinspires.ftc.teamcode.team10515.subsystems.RobotStateEstimator;
import org.firstinspires.ftc.teamcode.team10515.subsystems.ShooterSubsystem;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.ExpansionHubServo;

import java.util.Arrays;

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
public abstract class UltimateGoalRobot extends Robot {
    //private  RevBlinkinLedDriver lights;
    private TimeProfiler matchRuntime;
    private ExpansionHubs expansionHubs;
    private RobotStateEstimator robotStateEstimator;
    private Drive drive;
    private PulleySubsystem elevatorSubsystem;
    private StackTracker stackTracker;
    private FlickerSubsystem flickerSubsystem;
    private ShooterSubsystem shooterMotors;
    private ForkliftSubsystem2 forkliftSubsystem;
    private IntakeMotorSubsystem intakeMotorSubsystem;

    @Override
    public void init() {
        super.init();
        setExpansionHubs(new ExpansionHubs(this,
                hardwareMap.get(ExpansionHubEx.class, "Control Hub"),
                hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 1"))
        );

        setMotors(new RevMotor[] {
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("RL")), true, true, true, true, Motor.GOBILDA_435_RPM.getENCODER_TICKS_PER_REVOLUTION(), getWheelDiameter(), 2d),
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("FL")), true, true, true, true, Motor.GOBILDA_435_RPM.getENCODER_TICKS_PER_REVOLUTION(), getWheelDiameter(), 2d),
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("RR")), false, true, true, false, Motor.GOBILDA_435_RPM.getENCODER_TICKS_PER_REVOLUTION(), getWheelDiameter(), 2d),
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("FR")), false, true, true, false, Motor.GOBILDA_435_RPM.getENCODER_TICKS_PER_REVOLUTION(), getWheelDiameter(), 2d),
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("Shooter 1")), false, false, false, true, Motor.GOBILDA_6000_RPM.getENCODER_TICKS_PER_REVOLUTION(), 120, 1d),
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("Shooter 2")), false, false, false, true),

                new RevMotor((ExpansionHubMotor)(hardwareMap.get("Intake Motor")), true, false, false, false, Motor.GOBILDA_1150_RPM.getENCODER_TICKS_PER_REVOLUTION(), 50.8, 2d),
                new RevMotor((ExpansionHubMotor)(hardwareMap.get("Forklift Motor")), true, true, true, false, Motor.GOBILDA_60_RPM.getENCODER_TICKS_PER_REVOLUTION()),
//                new RevMotor((ExpansionHubMotor)(hardwareMap.get("LL")), true, true, false, true, Motor.GOBILDA_312_RPM.getENCODER_TICKS_PER_REVOLUTION(), 38d / 25.4d),
//                new RevMotor((ExpansionHubMotor)(hardwareMap.get("LR")), false, true, false, false, Motor.GOBILDA_312_RPM.getENCODER_TICKS_PER_REVOLUTION(), 38d / 25.4d)
        });

        setServos(new RevServo[] {
                new RevServo((ExpansionHubServo)(hardwareMap.get("Elevator Servo"))),
                new RevServo((ExpansionHubServo)(hardwareMap.get("Flicker 1"))),
                new RevServo((ExpansionHubServo)(hardwareMap.get("Flicker 2"))),
                new RevServo((ExpansionHubServo)(hardwareMap.get("Gripper"))),

        });

        setRobotStateEstimator(new RobotStateEstimator(this, hardwareMap.get(BNO055IMU.class, "imu"), new Pose2d()));
        //setElevatorSensor(hardwareMap.get(Rev2mDistanceSensor.class, "Elevator Sensor"));
        setDrive(new Drive(getRobotStateEstimator(), getMotors()[0], getMotors()[1], getMotors()[2], getMotors()[3]));
        setShooterSubsystem(new ShooterSubsystem(getMotors()[4], getMotors()[5]));
        setPulleySubsystem(new PulleySubsystem(getServos()[0]));
        setIntakeMotorSubsystem(new IntakeMotorSubsystem(getMotors()[6]));
        setForkliftSubsystem2(new ForkliftSubsystem2(getMotors()[7]));
        setFlickerSubsystem(new FlickerSubsystem(getServos()[1], getServos()[2]));
        setMatchRuntime(new TimeProfiler(false));
    }

    @Override
    public void init_loop() {
        super.init_loop();
    }

    @Override
    public void start() {
        super.start();
        getExpansionHubs().start();
        getDrive().start();
        Arrays.stream(getMotors()).forEach(RevMotor::resetEncoder);
        getMatchRuntime().start();
    }

    @Override
    public void loop() {
        super.loop();
        getExpansionHubs().update(getDt());
        getDrive().update(getDt());
        getIntakeMotorSubsystem().update(getDt());
        getShooterSubsystem().update(getDt());
        getFlickerSubsystem().update(getDt());
        getPulleySubsystem().update(getDt());
        getForkliftSubsystem2().update(getDt());


    }
//        getEndGameExtensionSubsystem().update(getDt());
//        if(getMatchRuntime().getDeltaTime(TimeUnits.SECONDS, false) >= 90d &&
//                getMatchRuntime().getDeltaTime(TimeUnits.SECONDS, false) <= 95d) {
//            //Set lights to give crazy patterns during endgame.
//            getLights().setPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_PARTY_PALETTE);
//        } else if(Feeder.getFeederExtensionStateMachine().hasReachedStateGoal(FeederExtensionStateMachine.State.EXTEND)) {
//            getLights().setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
//        } else if(Feeder.getFeederStoneGripperStateMachine().hasReachedStateGoal(FeederStoneGripperStateMachine.State.GRIP)) {
//            getLights().setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
//        } else if(getFlywheels().getStateMachine().hasReachedStateGoal(FlywheelStateMachine.State.INTAKE)) {
//            getLights().setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
//        }
//    }

    @Override
    public void stop() {
        super.stop();
        getExpansionHubs().stop();
        //Yogesh Commented This
      //  getRobotStateEstimator().stop();
        getDrive().stop();
        getForkliftSubsystem2().stop();
        getPulleySubsystem().stop();
        getFlickerSubsystem().stop();
        getShooterSubsystem().stop();
        getIntakeMotorSubsystem().stop();

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


    public ForkliftSubsystem2 getForkliftSubsystem2() {
        return forkliftSubsystem;
    }

    public void setForkliftSubsystem2(ForkliftSubsystem2 forkliftSubsystem){
        this.forkliftSubsystem = forkliftSubsystem;
    }
    /*public  RevBlinkinLedDriver getLights() {
        return lights;
    }

    public void setLights(RevBlinkinLedDriver lights) {
        this.lights = lights;
    }*/

//    public void setElevatorSensor(Rev2mDistanceSensor range) {
//        this.elevatorSensor = range;
//    }

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
}
