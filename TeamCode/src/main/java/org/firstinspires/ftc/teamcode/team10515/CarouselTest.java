package org.firstinspires.ftc.teamcode.team10515;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class CarouselTest extends OpMode {

    FFMapTest robot = new FFMapTest();
    double power = 0;

    @Override
    public void init() {
        robot.init(hardwareMap);

        telemetry.addData("Init", "Hello Storm Trooper");
        updateTelemetry(telemetry);
    }

    @Override
    public void loop() {
        if(gamepad1.a){
            power = 0;
        }
        else if(gamepad1.b){
            power += 0.1;
        }
        else if(gamepad1.x){
            power -= 0.1;
        }
        else if(gamepad1.y){
            power = 1;
        }
        robot.FR.setPower(power);
        telemetry.addLine("Power: " + power);
    }
}
