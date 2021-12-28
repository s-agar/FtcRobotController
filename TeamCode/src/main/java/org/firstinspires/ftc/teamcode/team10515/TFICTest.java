package org.firstinspires.ftc.teamcode.team10515;

import org.outoftheboxrobotics.tensorflowapi.ImageClassification.TFICBuilder;
import org.outoftheboxrobotics.tensorflowapi.ImageClassification.TensorImageClassifier;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.io.IOException;

@TeleOp(name="TFIC Test", group="Test")
public class TFICTest extends LinearOpMode {

    /* Declare OpMode members. */
    FFMapTest robot = new FFMapTest();
    OpenCvWebcam webcam;
    SamplePipeline pipeline = new SamplePipeline();

    @Override
    public void runOpMode(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        webcam.setPipeline(pipeline);

        webcam.setMillisecondsPermissionTimeout(2500);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Frame Count", webcam.getFrameCount());
            telemetry.addData("FPS", String.format("%.2f", webcam.getFps()));
            telemetry.addData("Total frame time ms", webcam.getTotalFrameTimeMs());
            telemetry.addData("Pipeline time ms", webcam.getPipelineTimeMs());
            telemetry.addData("Overhead time ms", webcam.getOverheadTimeMs());
            telemetry.addData("Theoretical max FPS", webcam.getCurrentPipelineMaxFps());
            telemetry.update();

            if (gamepad1.a){
                webcam.stopStreaming();
            }
            else if (gamepad1.y){
                try {
                    TensorImageClassifier tfic = new TFICBuilder(robot.hwMap, "model.tflite", "left", "center", "right").build();
                    tfic.recognize(pipeline.getMat());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SamplePipeline extends OpenCvPipeline {
        boolean viewportPaused;

        /*
         * This is the mat object into which we will save the mat passed by the backend to processFrame()
         * It will be returned by getMat()
         */
        Mat mat = new Mat();

        @Override
        public Mat processFrame(Mat input) {
            mat = input;
            return input;
        }

        @Override
        public void onViewportTapped(){
            viewportPaused = !viewportPaused;

            if (viewportPaused){
                webcam.pauseViewport();
            }
            else {
                webcam.resumeViewport();
            }
        }

       Mat getMat(){
            return mat;
        }
    }
}