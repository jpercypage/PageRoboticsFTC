package org.firstinspires.ftc.teamcode;


import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.Range;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Autonomous(name = "Auto")
public class AutonomousMode extends LinearOpMode {


    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain".
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".

    final double DESIRED_DISTANCE = 2;
    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value
    final double MAX_AUTO_STRAFE= 0.5;   //  Clip the strafing speed to this max value
    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    WebcamName camera; // webcam object

    private AprilTagProcessor aprilTag;
    private AprilTagDetection desiredTag = null;

    private VisionPortal visionPortal;


    private void initMotors() {
        leftFront  = hardwareMap.get(DcMotor.class, "frontLeft");
        rightFront = hardwareMap.get(DcMotor.class, "frontRight");
        leftRear  = hardwareMap.get(DcMotor.class, "rearLeft");
        rightRear = hardwareMap.get(DcMotor.class, "rearLeft");
    }


    private void initAprilTag() {
        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // e.g. Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.

        camera = hardwareMap.get(WebcamName.class, "Webcam");

        // Used to manage the video source.
        visionPortal = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(aprilTag)
                .setAutoStartStreamOnBuild(true)
                .build();

        visionPortal.setProcessorEnabled(aprilTag, true);





        //ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        //exposureControl.setExposure(6, TimeUnit.MILLISECONDS);

        //GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        //gainControl.setGain(250);


        telemetry.addData("System", "Running");
        telemetry.update();
    }




    @Override public void runOpMode() {


        telemetry.addData("System", "Running");
        telemetry.update();

        boolean targetFound     = false;    // Set to true when an AprilTag target is detected
        double  drive;                      // Desired forward power/speed (-1 to +1)
        double  strafe;                     // Desired strafe power/speed (-1 to +1)
        double  turn;                       // Desired turning power/speed (-1 to +1)

        initMotors();





        initAprilTag();

        waitForStart();

        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        exposureControl.setExposure(6, TimeUnit.MILLISECONDS);

        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(250);



        while (opModeIsActive()) {
            // Step through the list of detected tags and look for a matching tag
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                telemetry.addData("AprilTag: ", "Searching");
                // Look to see if we have size info on this tag.
                if (detection.metadata != null) {
                    telemetry.addData("AprilTag: ", "AprilTag Found!!");
                    targetFound = true;
                    desiredTag = detection;
                    break;  // don't look any further

                } else {
                    // This tag is NOT in the library, so we don't have enough information to track to it.
                    telemetry.addData("AprilTag: ", "detected april tag metadata == Null");
                }
                telemetry.update();
            }

            if (targetFound) {
                double rangeError = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
                double headingError = desiredTag.ftcPose.bearing;
                double yawError = desiredTag.ftcPose.yaw;




                drive = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
                turn = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
                strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);





                moveRobot(drive, strafe, turn);
            }


        }
    }



    private void moveRobot(double drive, double strafe, double turn) {
        // Calculate wheel powers.
        double leftFrontPower    =  drive +strafe +turn;
        double rightFrontPower   =  drive -strafe -turn;
        double leftRearPower     =  drive -strafe +turn;
        double rightRearPower    =  drive +strafe -turn;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftRearPower));
        max = Math.max(max, Math.abs(rightRearPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftRearPower /= max;
            rightRearPower /= max;
        }


        telemetry.addData("leftF: ", leftFrontPower);
        telemetry.addData("leftR: ", leftRearPower);
        telemetry.addData("rightF: ", rightFrontPower);
        telemetry.addData("rightR: ", rightRearPower);


        telemetry.update();


        // Send powers to the wheels.
        leftFront.setPower(-leftFrontPower);
        rightFront.setPower(-rightFrontPower);
        leftRear.setPower(-leftRearPower);
        rightRear.setPower(-rightRearPower);
    }

}
