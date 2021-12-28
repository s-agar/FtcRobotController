package org.firstinspires.ftc.teamcode.team10515.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.drivers.RevMotor;
import org.firstinspires.ftc.teamcode.team10515.states.CarouselStateMachine;

public class CarouselSubsystem implements ISubsystem<CarouselStateMachine, CarouselStateMachine.State> {
    private static CarouselStateMachine carouselStateMachine;
    private RevMotor carouselMotor;

    public CarouselSubsystem(RevMotor carouselMotor){
        setCarouselStateMachine(new CarouselStateMachine());
        setCarouselMotor(carouselMotor);
    }

    @Override
    public CarouselStateMachine getStateMachine() {
        return carouselStateMachine;
    }

    @Override
    public CarouselStateMachine.State getState() {
        return getStateMachine().getState();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        getCarouselMotor().setPower(0d);
    }

    @Override
    public String getName() {
        return "Carousel Subsystem";
    }

    @Override
    public void writeToTelemetry(Telemetry telemetry) {

    }

    @Override
    public void update(double dt) {
        getStateMachine().update(dt);
        getCarouselMotor().setPower(dt);
    }

    private static void setCarouselStateMachine(CarouselStateMachine carouselStateMachine){
        CarouselSubsystem.carouselStateMachine = carouselStateMachine;
    }

    private void setCarouselMotor(RevMotor carouselMotor){
        this.carouselMotor = carouselMotor;
    }

    public RevMotor getCarouselMotor(){
        return carouselMotor;
    }
}
