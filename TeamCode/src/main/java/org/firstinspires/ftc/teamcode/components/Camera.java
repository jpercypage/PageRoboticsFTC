package org.firstinspires.ftc.teamcode.components;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import utils.timer;

public class Camera {

    final double DESIRED_DISTANCE = 4.0; // inches

    final double SPEED_GAIN = 0.02  ;   //  Forward Speed Control "Gain". e.g. Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN = 0.015 ;   //  Strafe Speed Control "Gain".  e.g. Ramp up to 37% power at a 25 degree Yaw error.   (0.375 / 25.0)
    final double TURN_GAIN = 0.01  ;   //  Turn Control "Gain".  e.g. Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.8;
    final double MAX_AUTO_STRAFE = 0.5;
    final double MAX_AUTO_TURN = 0.8;

    final HardwareMap map;
    final Telemetry tele;
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    public Camera(HardwareMap map, Telemetry tele) {
        this.map = map;
        this.tele = tele;

        // Initialize the Apriltag Detection process
        this.aprilTag = new AprilTagProcessor.Builder().build();

        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        aprilTag.setDecimation(2);

        // Create the vision portal by using a builder.
        this.visionPortal = new VisionPortal.Builder()
                .setCamera(map.get(WebcamName.class, "webcam"))
                .addProcessor(aprilTag)
                .enableLiveView(true)
                .setAutoStartStreamOnBuild(true)
                .setShowStatsOverlay(true)
                .build();


        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
                timer.waitish(0.02);
            }
        }


        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);

        if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
            exposureControl.setMode(ExposureControl.Mode.Manual);
            timer.waitish(0.02);
        }

        exposureControl.setExposure(250L, TimeUnit.MILLISECONDS);
        timer.waitish(0.02);

        gainControl.setGain(6);
        timer.waitish(0.02);


    }

    public double[] detectAprilTag() {

        boolean targetFound = false;
        double[] driveTurnStrafe = {0d, 0d, 0d};


        AprilTagDetection tag = null;


        List<AprilTagDetection> currentDetections = this.aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                targetFound = true;
                tag = detection;
                break;  // don't look any further.
            }
        }

        if (tag == null) {
            return driveTurnStrafe;
        }

        driveTurnStrafe[0] = tag.ftcPose.range - DESIRED_DISTANCE;
        driveTurnStrafe[1] = tag.ftcPose.bearing;
        driveTurnStrafe[2] = tag.ftcPose.yaw;;

        return driveTurnStrafe;
    }
}